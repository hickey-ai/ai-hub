<script setup>
import {computed, onMounted, ref} from 'vue'

const active = ref('overview')
const url = ref('https://example.com')
const loading = ref(false)
const error = ref('')
const result = ref(null)
const jobs = ref([])
const overview = ref({jobs: 0, maxBody: '1.5 MB', redirects: '手动确认', robots: '请遵守目标站点规则'})

const nav = [
  {id: 'overview', label: '总览', icon: '⌂'},
  {id: 'crawl', label: '开始抓取', icon: '↗'},
  {id: 'history', label: '抓取记录', icon: '◷'}
]
const validUrl = computed(() => /^https?:\/\/[^\s]+$/i.test(url.value.trim()))

async function loadData() {
  const [overviewRes, jobsRes] = await Promise.all([fetch('/api/overview'), fetch('/api/jobs')])
  overview.value = await overviewRes.json()
  jobs.value = await jobsRes.json()
}
async function crawl() {
  error.value = ''
  result.value = null
  if (!validUrl.value) { error.value = '请输入完整的 http:// 或 https:// 地址'; return }
  loading.value = true
  try {
    const response = await fetch('/api/crawl', {method: 'POST', headers: {'Content-Type': 'application/json'}, body: JSON.stringify({url: url.value.trim()})})
    const body = await response.json()
    if (!response.ok) throw new Error(body.detail || body.message || '抓取失败，请检查地址和目标站点')
    result.value = body
    await loadData()
    active.value = 'crawl'
  } catch (e) { error.value = e.message || '抓取失败' }
  finally { loading.value = false }
}
function useExample(value) { url.value = value; active.value = 'crawl'; error.value = ''; result.value = null }
function formatDate(value) { return value ? new Date(value).toLocaleString('zh-CN', {hour12: false}) : '—' }
function shortUrl(value) { return value?.replace(/^https?:\/\//, '').replace(/\/$/, '') }
onMounted(loadData)
</script>

<template>
  <div class="app-shell">
    <aside class="sidebar">
      <div class="brand"><div class="brand-mark">⌁</div><div><b>ai-hub</b><span>crawler studio</span></div></div>
      <div class="workspace"><small>WORKSPACE</small><strong>公开网页研究台</strong><span>安全、克制、可复核</span></div>
      <nav><button v-for="item in nav" :key="item.id" :class="{active: active === item.id}" @click="active = item.id"><i>{{item.icon}}</i><span>{{item.label}}</span><em v-if="item.id === 'history'">{{jobs.length}}</em></button></nav>
      <div class="sidebar-foot"><div class="status-dot"></div><span>本机服务运行中</span><small>127.0.0.1 · 8100</small></div>
    </aside>
    <main class="main">
      <header><div><label>AI-HUB / WEB RESEARCH</label><h1>把网页变成可读的研究素材<span>.</span></h1></div><div class="header-chip"><b>SAFE FETCH</b><small>仅公开 HTTP / HTTPS</small></div></header>

      <section v-if="active === 'overview'" class="page">
        <div class="hero"><div><p class="eyebrow">LOCAL-FIRST CRAWLER</p><h2>先看清网页，<br><strong>再开始分析。</strong></h2><p class="hero-copy">输入一个公开网页地址，提取标题、摘要、正文片段、标题层级和链接。适合调研、竞品记录和内容整理，不自动执行批量任务。</p><button class="primary" @click="active = 'crawl'">开始一次抓取 <span>→</span></button></div><div class="orb"><div class="orb-inner">HTML<br><small>TO SIGNAL</small></div><i class="orbit orbit-a"></i><i class="orbit orbit-b"></i></div></div>
        <div class="stats"><article><small>已完成抓取</small><strong>{{overview.jobs}}</strong><span>本机记录，不上传云端</span></article><article><small>单页大小上限</small><strong>{{overview.maxBody}}</strong><span>避免意外消耗资源</span></article><article><small>跳转策略</small><strong>手动</strong><span>不会盲目跟随跳转</span></article><article><small>输出字段</small><strong>06</strong><span>标题 · 摘要 · 链接 · 正文</span></article></div>
        <div class="lower-grid"><div class="panel"><div class="panel-heading"><div><label>QUICK START</label><h3>试试这些公开页面</h3></div><button class="text-button" @click="active = 'history'">查看记录 →</button></div><div class="example-list"><button @click="useExample('https://example.com')"><span class="favicon">E</span><b>Example Domain</b><small>用于测试网络抓取</small><i>↗</i></button><button @click="useExample('https://www.iana.org/domains/example')"><span class="favicon blue">I</span><b>IANA Example</b><small>标准示例域名说明</small><i>↗</i></button></div></div><div class="panel note-panel"><label>RESEARCH ETHICS</label><h3>小而可靠的抓取器</h3><p>默认只抓取单页、拒绝本机和内网地址、不跟随重定向、不执行页面脚本。使用前请确认目标站点的 robots.txt、服务条款和适用法律。</p><div class="pill-row"><span>SSRF 防护</span><span>1.5 MB 上限</span><span>无脚本执行</span></div></div></div>
      </section>

      <section v-else-if="active === 'crawl'" class="page">
        <div class="page-title"><div><label>CRAWLER / FETCH</label><h2>开始一次抓取<span>.</span></h2><p>单页、可复核、以研究为目的。抓取只返回 HTML 结构化摘要。</p></div><div class="security-badge">◉ 安全边界已启用</div></div>
        <div class="crawl-layout"><div class="panel crawl-form"><label>公开网页地址</label><div class="url-input"><span>⌁</span><input v-model="url" @keyup.enter="crawl" placeholder="https://example.com"><button @click="crawl" :disabled="loading">{{loading ? '抓取中…' : '抓取'}}</button></div><p class="hint">支持 http:// 与 https://；禁止 localhost、内网 IP、文件地址和带账号密码的 URL。</p><div v-if="error" class="error">{{error}}</div><div class="guard-grid"><div><b>01</b><span>校验域名</span><small>拒绝本机与内网地址</small></div><div><b>02</b><span>获取页面</span><small>10 秒超时 / 1.5 MB</small></div><div><b>03</b><span>提取结构</span><small>标题、摘要、链接、正文</small></div></div></div><div class="panel protocol"><label>FETCH PROTOCOL</label><h3>让每一次请求<br>都可以解释。</h3><ul><li><b>GET</b><span>只读请求，不提交表单</span></li><li><b>NO JS</b><span>不运行目标网页脚本</span></li><li><b>NO REDIRECT</b><span>跳转地址由你确认</span></li><li><b>LOCAL</b><span>结果保存在当前进程内</span></li></ul></div></div>
        <div v-if="result" class="result-panel panel"><div class="result-head"><div><label>CRAWL RESULT / {{result.status}}</label><h3>{{result.title}}</h3><p>{{result.url}}</p></div><div class="result-metrics"><span><b>{{result.words}}</b> words</span><span><b>{{result.links.length}}</b> links</span><span><b>{{result.durationMs}}</b> ms</span></div></div><div class="result-grid"><div><label>摘要</label><p class="description">{{result.description || '页面没有 meta description，已使用正文片段。'}}</p><label>正文片段</label><p class="excerpt">{{result.excerpt || '没有提取到可见正文。'}}</p></div><div><label>标题层级</label><div class="tag-list"><span v-for="heading in result.headings" :key="heading">{{heading}}</span><small v-if="!result.headings.length">没有 h1-h3 标题</small></div><label>发现的链接</label><div class="link-list"><a v-for="link in result.links.slice(0, 6)" :key="link.url" :href="link.url" target="_blank" rel="noreferrer">{{link.label}} <span>↗</span></a></div></div></div></div>
        <div v-else class="empty-state"><span>⌁</span><h3>等待一个公开网页</h3><p>抓取完成后，结构化结果会显示在这里。</p></div>
      </section>

      <section v-else class="page"><div class="page-title"><div><label>CRAWLER / HISTORY</label><h2>抓取记录<span>.</span></h2><p>只保留当前服务进程内最近 20 条记录；重启后自动清空。</p></div><button class="primary small" @click="active = 'crawl'">新建抓取 <span>+</span></button></div><div v-if="jobs.length" class="history-list"><article v-for="job in jobs" :key="job.id" @click="result = job; active = 'crawl'"><div class="job-icon">↗</div><div class="job-main"><b>{{job.title}}</b><span>{{shortUrl(job.url)}}</span></div><div class="job-stat"><b>{{job.words}}</b><small>words</small></div><div class="job-stat"><b>{{job.links.length}}</b><small>links</small></div><time>{{formatDate(job.createdAt)}}</time><i>→</i></article></div><div v-else class="empty-state large"><span>◷</span><h3>还没有抓取记录</h3><p>完成第一次抓取后，它会出现在这里。</p><button class="primary" @click="active = 'crawl'">开始抓取</button></div></section>
      <footer>AI-HUB CRAWLER · 公开网页研究台 <span>仅用于合法、合规、克制的单页研究</span></footer>
    </main>
  </div>
</template>
