const { chromium } = require('playwright-core')
const { spawn } = require('child_process')
const fs = require('fs')
const os = require('os')
const path = require('path')
const net = require('net')
const assert = require('node:assert/strict')

const root = path.resolve(__dirname, '..')
const sleep = ms => new Promise(resolve => setTimeout(resolve, ms))
const marker = '端到端验收'
const projects = fs.readdirSync(root, { withFileTypes: true })
  .filter(x => x.isDirectory() && fs.existsSync(path.join(root, x.name, 'frontend', 'src', 'config.js')))
  .map(x => x.name).sort()
const selected = process.argv.slice(2).filter(x => !x.startsWith('--'))
const output = process.argv.find(x => x.startsWith('--report='))?.slice(9)
const chrome = process.env.CHROMIUM_PATH || chromium.executablePath()

async function freePort() {
  const server = net.createServer()
  await new Promise(resolve => server.listen(0, '127.0.0.1', resolve))
  const port = server.address().port
  await new Promise(resolve => server.close(resolve))
  return port
}
async function start(slug, file, port, resource) {
  const jar = path.join(root, slug, 'backend', 'target', `${slug}-api-0.1.0.jar`)
  assert.ok(fs.existsSync(jar), `${slug}: missing jar; run test-all first`)
  const proc = spawn('java', ['-jar', jar, `--server.port=${port}`, `--aihub.data-file=${file}`], {
    cwd: path.join(root, slug), windowsHide: true, stdio: 'ignore'
  })
  for (let i = 0; i < 120; i++) {
    if (proc.exitCode !== null) throw new Error(`${slug}: Java exited ${proc.exitCode}`)
    try {
      if ((await fetch(`http://127.0.0.1:${port}/api/${resource}`)).ok) return proc
    } catch {}
    await sleep(250)
  }
  await stop(proc)
  throw new Error(`${slug}: Java did not start within 30 seconds`)
}
async function stop(proc) {
  if (proc.exitCode !== null) return
  await new Promise(resolve => {
    const timeout = setTimeout(() => { if (proc.exitCode === null) proc.kill('SIGKILL') }, 10000)
    proc.once('exit', () => { clearTimeout(timeout); resolve() })
    proc.kill()
  })
}
async function request(base, method, route, body, expected) {
  const r = await fetch(`${base}/api/${route}`, {
    method, headers: body ? { 'content-type': 'application/json' } : {},
    body: body ? JSON.stringify(body) : undefined
  })
  if (r.status !== expected) throw new Error(`${method} ${route}: expected ${expected}, got ${r.status}: ${await r.text()}`)
  return r.status === 204 ? null : r.json()
}
function payload(entity, relatedId, suffix) {
  return Object.fromEntries(entity.fields.map(([key, , type, , extra]) => [key,
    type === 'relation' ? relatedId : type === 'date' ? '2026-10-03' :
    type === 'datetime-local' ? (key === 'endAt' ? '2026-10-03T10:00' : '2026-10-03T09:00') : type === 'checkbox' ? false :
    type === 'select' ? extra[0] : type === 'number' || type === 'money' ? 12 : `${marker}${suffix}`
  ]))
}
async function browserCheck(browser, slug, base, first, second) {
  const page = await browser.newPage({ viewport: { width: 1440, height: 900 } })
  const errors = []
  page.on('pageerror', error => errors.push(error.message))
  try {
    await page.goto(base, { waitUntil: 'networkidle' })
    await page.locator('.stats strong').first().waitFor()
    for (const [index, entity] of [[1, first], [2, second]].filter(x => x[1])) {
      await page.locator('nav button').nth(index).click()
      await page.locator('tbody tr').first().waitFor()
      assert.ok((await page.locator('tbody tr').count()) >= 1, `${slug}: ${entity.key} empty`)
    }
    await page.locator('nav button').nth(1).click()
    const before = await page.locator('tbody tr').count()
    await page.locator('.records-panel .secondary').click()
    await page.locator('.drawer').waitFor()
    for (const input of await page.locator('.drawer input[type=text]').all()) await input.fill(marker)
    for (const input of await page.locator('.drawer input[type=number]').all()) await input.fill('12')
    for (const input of await page.locator('.drawer input[type=date]').all()) await input.fill('2026-10-03')
    for (const input of await page.locator('.drawer input[type=datetime-local]').all()) await input.fill((await input.getAttribute('type')) && (await input.locator('xpath=..').innerText()).includes('结束') ? '2026-10-03T10:00' : '2026-10-03T09:00')
    for (const area of await page.locator('.drawer textarea').all()) await area.fill('完整的操作和预期结果')
    await page.getByRole('button', { name: '保存记录' }).click()
    await page.locator('.drawer').waitFor({ state: 'hidden', timeout: 5000 })
    assert.equal(await page.locator('tbody tr').count(), before + 1, `${slug}: UI create not visible`)
    await page.getByRole('searchbox').fill(marker)
    const matches = await page.locator('tbody tr').count()
    assert.equal(matches, 1, `${slug}: UI search should find created record`)
    await page.locator('tbody tr').first().getByRole('button', { name: '编辑' }).click()
    await page.locator('.drawer input[type=text]').first().fill(marker + '修改')
    await page.getByRole('button', { name: '保存记录' }).click()
    await page.locator('.drawer').waitFor({ state: 'hidden', timeout: 5000 })
    await page.getByRole('searchbox').fill(marker + '修改')
    assert.equal(await page.locator('tbody tr').count(), 1, `${slug}: UI edit not visible`)
    page.once('dialog', dialog => dialog.accept())
    await page.locator('tbody tr').first().getByRole('button', { name: '删除' }).click()
    await page.locator('.empty').waitFor({ timeout: 5000 })
    await page.getByRole('searchbox').fill('')
    assert.equal(await page.locator('tbody tr').count(), before, `${slug}: UI delete not visible`)
    assert.deepEqual(errors, [], `${slug}: uncaught browser errors`)
  } finally { await page.close() }
}
async function verify(browser, slug) {
  const config = JSON.parse(fs.readFileSync(path.join(root, slug, 'frontend', 'src', 'config.js'), 'utf8').replace(/^export default\s*/, ''))
  const [first, second] = config.entities
  const dir = fs.mkdtempSync(path.join(os.tmpdir(), `aihub-test-${slug}-`))
  const file = path.join(dir, 'records.json')
  const port = await freePort()
  const base = `http://127.0.0.1:${port}`
  let proc
  try {
    proc = await start(slug, file, port, first.key)
    await browserCheck(browser, slug, base, first, second)
    for (const entity of config.entities) {
      await request(base, 'POST', entity.key, {}, 400)
      await request(base, 'PUT', `${entity.key}/999999`, payload(entity, 1, 'missing'), 404)
    }
    await request(base, 'GET', 'not-a-resource', undefined, 404)
    const parent = await request(base, 'POST', first.key, payload(first, 1, '主'), 201)
    assert.ok(parent.id, `${slug}: no created id`)
    let child
    if (second) {
      const relatedField = second.fields.find(x => x[2] === 'relation')
      assert.equal(relatedField?.[4], first.key, `${slug}: unexpected relation`)
      const invalid = payload(second, 999999, '错误关联')
      await request(base, 'POST', second.key, invalid, 400)
      child = await request(base, 'POST', second.key, payload(second, parent.id, '从'), 201)
      await request(base, 'DELETE', `${first.key}/${parent.id}`, undefined, 409)
    }
    const change = payload(first, 1, '更新')
    await request(base, 'PUT', `${first.key}/${parent.id}`, change, 200)
    assert.ok((await request(base, 'GET', first.key, undefined, 200)).some(x => x.id === parent.id), `${slug}: new parent absent`)
    await stop(proc); proc = null
    assert.ok(fs.existsSync(file), `${slug}: no persisted file`)
    proc = await start(slug, file, port, first.key)
    const parents = await request(base, 'GET', first.key, undefined, 200)
    assert.ok(parents.some(x => x.id === parent.id), `${slug}: restart lost parent`)
    if (child) {
      assert.ok((await request(base, 'GET', second.key, undefined, 200)).some(x => x.id === child.id), `${slug}: restart lost child`)
      await request(base, 'DELETE', `${second.key}/${child.id}`, undefined, 204)
    }
    await request(base, 'DELETE', `${first.key}/${parent.id}`, undefined, 204)
    assert.ok(!(await request(base, 'GET', first.key, undefined, 200)).some(x => x.id === parent.id), `${slug}: delete failed`)
    return { project: slug, result: 'pass', checks: ['browser navigation', 'browser CRUD/search', 'validation', 'API CRUD', 'referential integrity', 'restart persistence'] }
  } finally {
    if (proc) await stop(proc)
    fs.rmSync(dir, { recursive: true, force: true })
  }
}
;(async () => {
  const names = selected.length ? selected : projects
  for (const name of names) assert.ok(projects.includes(name), `Not a config-based project: ${name}`)
  const browser = await chromium.launch({ executablePath: chrome, headless: true })
  const results = []
  try {
    for (const slug of names) {
      const started = Date.now()
      try {
        const result = await verify(browser, slug)
        results.push({ ...result, seconds: Math.round((Date.now() - started) / 1000) })
        console.log(`${slug} PASS (${results.at(-1).seconds}s)`)
      } catch (error) {
        const message = error.stack || String(error)
        results.push({ project: slug, result: 'fail', seconds: Math.round((Date.now() - started) / 1000), error: message })
        console.error(`${slug} FAIL: ${message}`)
      }
    }
  } finally { await browser.close() }
  if (output) fs.writeFileSync(path.resolve(output), JSON.stringify(results, null, 2) + '\n')
  console.log(`${results.filter(x => x.result === 'pass').length}/${results.length} config-driven projects passed detailed browser/API/restart checks`)
  if (results.some(x => x.result !== 'pass')) process.exitCode = 1
})().catch(error => { console.error(error); process.exitCode = 1 })
