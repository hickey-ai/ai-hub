const { chromium } = require('playwright-core')
const { spawn } = require('child_process')
const fs = require('fs')
const os = require('os')
const path = require('path')
const net = require('net')
const assert = require('node:assert/strict')
const root = path.resolve(__dirname, '..')
const endpoints = { shop:'products', manage:'users', crm:'customers', oa:'requests', finance:'accounts', health:'members', wellness:'plans', hospital:'patients', school:'students', access:'doors', ai:'skills', html:'directory', crawler:'overview', gridops:'cases', labbook:'experiments', barber:'services', dining:'dishes', selfshop:'products' }
const mini = new Set(['shop','barber','dining','selfshop'])
const apiOnly = new Set(['barber','dining','selfshop'])
const legacy = new Set(['finance','health','wellness','hospital','school','access'])
const screenshots = process.argv.includes('--screenshots')
const names = process.argv.slice(2).filter(x => !x.startsWith('--'))
const selected = names.length ? names : Object.keys(endpoints)
const output = process.argv.find(x => x.startsWith('--report='))?.slice(9)
const sleep = ms => new Promise(resolve => setTimeout(resolve, ms))
async function port() { const server = net.createServer(); await new Promise(r=>server.listen(0,'127.0.0.1',r)); const value=server.address().port; await new Promise(r=>server.close(r)); return value }
async function stop(proc) {
  if (proc.exitCode !== null) return
  await new Promise(resolve => { const timeout=setTimeout(()=>{ if(proc.exitCode===null) proc.kill('SIGKILL') },10000); proc.once('exit',()=>{clearTimeout(timeout);resolve()});proc.kill() })
}
async function verify(browser, slug) {
  const jar=path.join(root,slug,'backend','target',`${slug}-api-0.1.0.jar`)
  assert.ok(fs.existsSync(jar),`${slug}: missing jar`)
  if(mini.has(slug)) {
    const miniApp=path.join(root,slug,'miniprogram','dist','build','mp-weixin','app.json')
    assert.ok(fs.existsSync(miniApp),`${slug}: missing WeChat mini-program output`)
    assert.ok(JSON.parse(fs.readFileSync(miniApp,'utf8')).pages.length,`${slug}: no mini-program pages`)
  }
  const dir=fs.mkdtempSync(path.join(os.tmpdir(),`aihub-special-${slug}-`))
  const file=path.join(dir,'data.json'), listen=await port(), base=`http://127.0.0.1:${listen}`
  const proc=spawn('java',['-jar',jar,`--server.port=${listen}`,`--aihub.data-file=${file}`],{cwd:path.join(root,slug),windowsHide:true,stdio:'ignore'})
  try {
    let response
    for(let i=0;i<120;i++) {
      if(proc.exitCode!==null) throw new Error(`${slug}: Java exited ${proc.exitCode}`)
      try { response=await fetch(`${base}/api/${endpoints[slug]}`); if(response.ok) break } catch {}
      await sleep(250)
    }
    assert.ok(response?.ok,`${slug}: API did not start within 30 seconds`)
    const data=await response.json()
    assert.ok(data!==null,`${slug}: API returned null`)
    const checks=['isolated Java startup','live API response']
    if(mini.has(slug)) checks.push('mini-program build pages')
    if(!apiOnly.has(slug)) {
      const page=await browser.newPage({viewport:{width:1440,height:900}})
      const errors=[], failures=[]
      page.on('pageerror',e=>errors.push(e.message))
      page.on('response',r=>{if(r.status()>=500 && r.url().startsWith(base)) failures.push(`${r.status()} ${r.url()}`)})
      try {
        const loaded=await page.goto(base,{waitUntil:'networkidle'})
        assert.equal(loaded.status(),200,`${slug}: page load`)
        assert.ok((await page.locator('body').innerText()).length>80,`${slug}: blank page`)
        await page.waitForTimeout(300)
        if(screenshots && slug==='crm') await page.screenshot({path:path.join(root,'crm','screenshots','pipeline.png'),fullPage:true})
        if(screenshots && slug==='oa') {
          await page.screenshot({path:path.join(root,'oa','screenshots','approvals.png'),fullPage:true})
          await page.locator('.sidebar .nav-item').nth(2).click()
          await page.screenshot({path:path.join(root,'oa','screenshots','requests.png'),fullPage:true})
        }        if(['ai','html','crawler','gridops','labbook'].includes(slug)) {
          const tabs=page.locator('nav button')
          const count=await tabs.count()
          assert.ok(count>=2,`${slug}: missing workspace navigation`)
          for(let i=0;i<count;i++) {
            await tabs.nth(i).click()
            assert.ok((await page.locator('main').innerText()).trim().length>40,`${slug}: blank workspace tab ${i}`)
          }
          checks.push(`${count} browser workspace tabs`)
        }        if(slug==='shop') {
          const catalog=await (await fetch(`${base}/api/products`)).json()
          const stock=catalog[0].stock
          await page.locator('.product-card').first().click()
          await page.locator('.detail-modal').waitFor()
          await page.locator('.detail-modal .primary-button').click()
          await page.locator('.drawer .cart-line').waitFor()
          await page.locator('.drawer-bottom .primary-button').click()
          await page.locator('.modal input[placeholder="你的名字"]').fill('浏览器订单验收')
          await page.locator('.modal input[type=email]').fill('browser@example.com')
          await page.locator('form.modal .primary-button').click()
          await page.locator('.modal.success').waitFor()
          assert.equal((await (await fetch(`${base}/api/products`)).json())[0].stock,stock-1,`${slug}: checkout stock not decremented`)
          checks.push('browser product detail/cart/checkout and API stock')
        }        if(legacy.has(slug)) {
          const shotDir=path.join(root,slug,'screenshots')
          if(screenshots) await page.screenshot({path:path.join(shotDir,'overview.png'),fullPage:true})
          await page.locator('nav button').nth(1).click()
          await page.locator('tbody tr').first().waitFor({timeout:8000})
          if(screenshots) await page.screenshot({path:path.join(shotDir,'primary.png'),fullPage:true})
          const before=await page.locator('tbody tr').count()
          await page.locator('nav button').nth(2).click()
          await page.locator('.timeline article').first().waitFor({timeout:8000})
          if(screenshots) await page.screenshot({path:path.join(shotDir,'secondary.png'),fullPage:true})
          await page.locator('nav button').nth(1).click()
          await page.locator('.add input').fill('浏览器持久化测试')
          await page.locator('.add button').click()
          await page.getByText('浏览器持久化测试').first().waitFor()
          assert.equal(await page.locator('tbody tr').count(),before+1,`${slug}: UI create`)
          await page.reload({waitUntil:'networkidle'})
          await page.locator('nav button').nth(1).click()
          assert.equal(await page.locator('tbody tr').count(),before+1,`${slug}: UI refresh persistence`)
          const row=page.locator('tbody tr').filter({hasText:'浏览器持久化测试'})
          page.once('dialog',dialog=>dialog.accept('浏览器编辑结果'))
          await row.getByRole('button',{name:'编辑'}).click()
          await page.getByText('浏览器编辑结果').first().waitFor()
          page.once('dialog',dialog=>dialog.accept())
          await page.locator('tbody tr').filter({hasText:'浏览器编辑结果'}).getByRole('button',{name:'删除'}).click()
          await page.getByText('记录已删除').waitFor()
          assert.equal(await page.locator('tbody tr').count(),before,`${slug}: UI delete`)
          assert.ok(fs.existsSync(file),`${slug}: no persisted JSON`)
          checks.push('browser create/edit/delete','refresh persistence')
        }
        assert.deepEqual(errors,[],`${slug}: browser JS errors`)
        assert.deepEqual(failures,[],`${slug}: browser server errors`)
        checks.push('real browser render','no JS/HTTP 5xx errors')
      } finally {await page.close()}
    }
    return {project:slug,result:'pass',checks}
  } finally {await stop(proc);fs.rmSync(dir,{recursive:true,force:true})}
}
;(async()=>{
  for(const slug of selected) assert.ok(endpoints[slug],`Unknown special project: ${slug}`)
  const browser=await chromium.launch({executablePath:process.env.CHROMIUM_PATH || chromium.executablePath(),headless:true})
  const results=[]
  try {
    for(const slug of selected) {
      const start=Date.now()
      try { const item=await verify(browser,slug);results.push({...item,seconds:Math.round((Date.now()-start)/1000)});console.log(`${slug} PASS`) }
      catch(e){const error=e.stack||String(e);results.push({project:slug,result:'fail',error});console.error(`${slug} FAIL: ${error}`)}
    }
  } finally {await browser.close()}
  if(output)fs.writeFileSync(path.resolve(output),JSON.stringify(results,null,2)+'\n')
  console.log(`${results.filter(x=>x.result==='pass').length}/${results.length} special project runtime checks passed`)
  if(results.some(x=>x.result!=='pass'))process.exitCode=1
})().catch(e=>{console.error(e);process.exitCode=1})
