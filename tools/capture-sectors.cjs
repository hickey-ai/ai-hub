const { chromium } = require('playwright-core')
const { spawn } = require('child_process')
const path = require('path')
const fs = require('fs')
const root = path.resolve(__dirname, '..')
const chrome = process.env.CHROMIUM_PATH || chromium.executablePath()
const projects = [["erp", 8101, "products"], ["manufacturing", 8102, "materials"], ["logistics", 8103, "vehicles"], ["property", 8104, "units"], ["agriculture", 8105, "plots"], ["construction", 8106, "projects"], ["hospitality", 8107, "rooms"], ["hrm", 8108, "employees"], ["service", 8109, "customers"], ["energy", 8110, "assets"], ["legal", 8111, "clients"], ["culture", 8112, "venues"], ["community", 8113, "programs"], ["cms", 8114, "sections"], ["wms", 8115, "bins"], ["b2b", 8116, "suppliers"], ["eldercare", 8117, "residents"], ["pharmacy", 8118, "medicines"], ["insurance", 8119, "policies"], ["rental", 8120, "assets"], ["homeservice", 8121, "customers"], ["water", 8122, "stations"], ["sanitation", 8123, "routes"], ["mining", 8124, "sites"], ["forestry", 8125, "parcels"], ["fishery", 8126, "ponds"], ["telecom", 8127, "sites"], ["itops", 8128, "assets"], ["civic", 8129, "services"]]
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
    for (const [slug,port,endpoint] of projects) {
      const jar=path.join(root,slug,'backend','target',slug+'-api-0.1.0.jar')
      const proc=spawn('java',['-jar',jar],{cwd:path.join(root,slug),windowsHide:true,stdio:'ignore'})
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
        if (errors.length) throw new Error(slug + ' page errors: ' + errors.join('; '))
        const files=fs.readdirSync(dir).filter(x=>x.endsWith('.png'))
        console.log(slug,files.length,'screenshots',files.map(x=>fs.statSync(path.join(dir,x)).size).join(','))
        await page.close()
      } finally { proc.kill() }
    }
  } finally { await browser.close() }
})().catch(e=>{console.error(e);process.exitCode=1})
