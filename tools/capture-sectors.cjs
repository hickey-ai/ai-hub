const { chromium } = require('playwright-core')
const { spawn } = require('child_process')
const path = require('path')
const fs = require('fs')
const os = require('os')
const root = path.resolve(__dirname, '..')
const chrome = process.env.CHROMIUM_PATH || chromium.executablePath()
const projects = [["erp", 8101, "products"], ["manufacturing", 8102, "materials"], ["logistics", 8103, "vehicles"], ["property", 8104, "units"], ["agriculture", 8105, "plots"], ["construction", 8106, "projects"], ["hospitality", 8107, "rooms"], ["hrm", 8108, "employees"], ["service", 8109, "customers"], ["energy", 8110, "assets"], ["legal", 8111, "clients"], ["culture", 8112, "venues"], ["community", 8113, "programs"], ["cms", 8114, "sections"], ["wms", 8115, "bins"], ["b2b", 8116, "suppliers"], ["eldercare", 8117, "residents"], ["pharmacy", 8118, "medicines"], ["insurance", 8119, "policies"], ["rental", 8120, "assets"], ["homeservice", 8121, "customers"], ["water", 8122, "stations"], ["sanitation", 8123, "routes"], ["mining", 8124, "sites"], ["forestry", 8125, "parcels"], ["fishery", 8126, "ponds"], ["telecom", 8127, "sites"], ["itops", 8128, "assets"], ["civic", 8129, "services"], ["parking", 8130, "lots"], ["charging", 8131, "stations"], ["parkops", 8132, "parks"], ["fleet", 8133, "vehicles"], ["scenic", 8134, "spots"], ["clinic", 8135, "clinics"], ["dental", 8136, "chairs"], ["aesthetics", 8137, "services"], ["rehab", 8138, "programs"], ["lis", 8139, "assays"], ["kindergarten", 8140, "classes"], ["training", 8141, "courses"], ["elearning", 8142, "courses"], ["exam", 8143, "exams"], ["library", 8144, "books"], ["petcare", 8145, "pets"], ["petboarding", 8146, "rooms"], ["petgrooming", 8147, "packages"], ["veterinary", 8148, "clinics"], ["pos", 8149, "counters"], ["loyalty", 8150, "tiers"], ["laundry", 8151, "machines"], ["gym", 8152, "classes"], ["photography", 8153, "packages"], ["wedding", 8154, "plans"], ["accounting", 8155, "ledgers"], ["contracts", 8156, "templates"], ["projectops", 8157, "projects"], ["maintenance", 8158, "assets"], ["qms", 8159, "standards"], ["coldchain", 8160, "containers"], ["freshdelivery", 8161, "routes"], ["crossborder", 8162, "products"], ["returns", 8163, "products"], ["realestate", 8164, "listings"], ["testops", 8165, "testcases"], ["ticketops", 8166, "queues"], ["bugtrack", 8167, "projects"]]
const sleep = ms => new Promise(r => setTimeout(r, ms))
async function waitReady(port, endpoint, proc) {
  for (let i=0; i<80; i++) {
    if (proc.exitCode !== null) throw new Error('Java exited early: ' + proc.exitCode)
    try { const r=await fetch(`http://127.0.0.1:${port}/api/` + endpoint); if (r.ok) return }
    catch {}
    await sleep(250)
  }
  throw new Error(`Service ${port} never became ready`)
}
;(async()=>{
  const browser=await chromium.launch({executablePath:chrome,headless:true})
  try {
    for (const [slug,port,endpoint] of (process.argv.includes('--platforms') ? projects.filter(([,port]) => port >= 8165) : process.argv.includes('--new') ? projects.filter(([,port]) => port >= 8130) : projects)) {
      const jar=path.join(root,slug,'backend','target',slug+'-api-0.1.0.jar')
      const dataFile=path.join(os.tmpdir(),`aihub-capture-${slug}-${process.pid}.json`)
      const proc=spawn('java',['-jar',jar,`--aihub.data-file=${dataFile}`],{cwd:path.join(root,slug),windowsHide:true,stdio:'ignore'})
      try {
        await waitReady(port,endpoint,proc)
        const page=await browser.newPage({viewport:{width:1440,height:900},deviceScaleFactor:1})
        const errors=[]
        page.on('pageerror',e=>errors.push(e.message))
        await page.goto(`http://127.0.0.1:${port}/`,{waitUntil:'networkidle'})
        await page.locator('.stats strong').first().waitFor()
        const dir=path.join(root,slug,'screenshots')
        await page.screenshot({path:path.join(dir,'overview.png'),fullPage:true})
        await page.locator('nav button').nth(1).click()
        await page.locator('tbody tr').first().waitFor()
        await page.screenshot({path:path.join(dir,'primary.png'),fullPage:true})
        await page.locator('nav button').nth(2).click()
        await page.locator('tbody tr').first().waitFor()
        await page.screenshot({path:path.join(dir,'secondary.png'),fullPage:true})
        await page.getByRole('button',{name:/新增/}).first().click()
        await page.locator('.drawer').waitFor()
        await page.screenshot({path:path.join(dir,'editor.png'),fullPage:true})
        if (port >= 8130) {
          // Exercise the real Vue form and API, not merely a static render.
          for (const input of await page.locator('.drawer input[type=text]').all()) await input.fill('演示测试记录')
          for (const input of await page.locator('.drawer input[type=number]').all()) await input.fill('12')
          await page.getByRole('button',{name:'保存记录'}).click()
          await page.locator('.drawer').waitFor({state:'hidden'})
          if (await page.locator('tbody tr').count() !== 2) throw new Error(slug + ' UI create did not add a row')
        }
        if (errors.length) throw new Error(slug + ' page errors: ' + errors.join('; '))
        const files=fs.readdirSync(dir).filter(x=>x.endsWith('.png'))
        console.log(slug,files.length,'screenshots',files.map(x=>fs.statSync(path.join(dir,x)).size).join(','))
        await page.close()
      } finally {
        if (proc.exitCode === null) await new Promise(resolve => { proc.once('exit', resolve); proc.kill() })
        try { fs.unlinkSync(dataFile) } catch {}
      }
    }
  } finally { await browser.close() }
})().catch(e=>{console.error(e);process.exitCode=1})
