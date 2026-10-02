<script setup>
import { computed, onMounted, reactive, ref } from 'vue'
import config from './config'

const active = ref('overview')
const rows = reactive({})
const loading = ref(true)
const error = ref('')
const notice = ref('')
const search = ref('')
const editorOpen = ref(false)
const editId = ref(null)
const form = reactive({})
const today = () => { const d = new Date(); return `${d.getFullYear()}-${String(d.getMonth()+1).padStart(2,'0')}-${String(d.getDate()).padStart(2,'0')}` }
const now = () => `${today()}T09:00`
const isSchedule = config.entities.length === 1
const nav = computed(() => [{ key: 'overview', title: '总览', icon: '⌂' }, ...config.entities.map(e => ({key:e.key,title:e.title,icon:e.icon})), ...(isSchedule ? [{key:'week',title:'未来安排',icon:'▦'}] : [])])
const currentEntity = computed(() => config.entities.find(e => e.key === active.value) || config.entities[0])
const filtered = computed(() => (rows[currentEntity.value.key] || []).filter(item => Object.values(item).some(value => String(value).toLowerCase().includes(search.value.trim().toLowerCase()))))
const activityLabel = item => {
  const entity = config.entities.at(-1)
  const field = entity.fields.find(f => !['relation','date','select','textarea'].includes(f[2]))
  return `${entity.title} · ${item[field?.[0]] ?? item.id}`
}
const recent = computed(() => [...(rows[config.entities.at(-1).key] || [])].sort((a,b) => Number(b.id)-Number(a.id)).slice(0,5))
const weekItems = computed(() => (rows.events || []).filter(x => x.startAt >= today()).sort((a,b) => a.startAt.localeCompare(b.startAt)))
const weekGroups = computed(() => Object.entries(Object.groupBy(weekItems.value, x => x.startAt.slice(0,10))))
const count = key => (rows[key] || []).length
const fieldName = key => config.entities.flatMap(e => e.fields).find(f => f[0] === key)?.[1] || key
const display = (key, value) => {
  if (key === 'done') return value ? '已完成' : '待完成'
  if (/Id$/.test(key)) {
    const relation = config.entities.flatMap(e => e.fields).find(f => f[0] === key && f[2] === 'relation')
    const parent = config.entities.find(e => e.key === relation?.[4])
    const found = (rows[parent?.key] || []).find(row => String(row.id) === String(value))
    return found?.[parent?.fields[0][0]] || '已删除档案'
  }
  return value ?? '—'
}
async function api(url, options) {
  const res = await fetch(`/api/${url}`, { headers: {'Content-Type':'application/json'}, ...options })
  if (!res.ok) {
    const result = await res.json().catch(() => ({}))
    throw new Error(result.detail || result.message || `请求失败（${res.status}）`)
  }
  return res.status === 204 ? null : res.json()
}
async function load() {
  loading.value = true
  try { for (const e of config.entities) rows[e.key] = await api(e.key); error.value = '' }
  catch (e) { error.value = `服务未连接：${e.message}` }
  finally { loading.value = false }
}
function navigate(key) { active.value=key; editorOpen.value=false; search.value=''; notice.value='' }
function openEditor(entity, item=null) {
  navigate(entity.key)
  editId.value = item?.id || null
  for (const field of entity.fields) {
    const [key,,type,,options] = field
    form[key] = item?.[key] ?? (type === 'checkbox' ? false : type === 'date' ? today() : type === 'datetime-local' ? now() : type === 'select' ? options[0] : type === 'relation' ? (rows[options]?.[0]?.id ?? '') : '')
  }
  editorOpen.value = true
}
async function save() {
  const entity=currentEntity.value
  error.value=''
  try {
    const body=Object.fromEntries(entity.fields.map(([key,,type]) => [key, type === 'relation' || type === 'number' || type === 'money' ? Number(form[key]) : form[key]]))
    await api(entity.key+(editId.value ? '/'+editId.value : ''), {method:editId.value ? 'PUT':'POST',body:JSON.stringify(body)})
    rows[entity.key]=await api(entity.key)
    editorOpen.value=false; notice.value=editId.value ? '修改已保存' : '新记录已保存'
  } catch(e) { error.value=e.message }
}
async function remove(entity, item) {
  if (!confirm(`确定删除这条${entity.singular}吗？此操作不可撤销。`)) return
  try { await api(`${entity.key}/${item.id}`,{method:'DELETE'}); rows[entity.key]=await api(entity.key); notice.value='已删除'; error.value='' }
  catch(e) { error.value=e.message }
}
async function toggle(item) {
  try { await api(`events/${item.id}`,{method:'PUT',body:JSON.stringify({...item,done:!item.done})}); rows.events=await api('events'); error.value='' }
  catch(e) { error.value=e.message }
}
onMounted(load)
</script>

<template>
<div class="app" :style="{'--accent':config.accent,'--soft':config.soft}">
  <aside class="sidebar">
    <div class="brand"><span class="brand-mark">✦</span><div><strong>{{config.en}}</strong><small>AI-HUB / INDUSTRY STUDIO</small></div></div>
    <p class="nav-caption">WORKSPACE</p>
    <nav><button v-for="n in nav" :key="n.key" :class="{selected:active===n.key}" @click="navigate(n.key)"><span class="nav-icon">{{n.icon}}</span>{{n.title}}<span v-if="n.key!=='overview' && n.key!=='week'" class="nav-count">{{count(n.key)}}</span></button></nav>
    <div class="sidebar-bottom"><span class="online-dot"></span> 本机演示环境 <small>仅绑定 127.0.0.1 · 数据保存在本机</small></div>
  </aside>
  <div class="main-area">
    <header class="topbar"><span>AI-HUB <b>/</b> {{active==='overview'?'总览':active==='week'?'未来安排':currentEntity.title}}</span><div class="top-right"><span class="status"><i></i> 系统运行中</span><span class="avatar">{{config.cn.slice(0,1)}}</span></div></header>
    <main>
      <div class="heading"><div><div class="eyebrow">{{config.en}} · LOCAL WORKSPACE</div><h1>{{active==='overview'?config.cn:active==='week'?'未来安排':currentEntity.title}}</h1><p>{{config.subtitle}}</p></div><button class="primary" @click="openEditor(active==='overview'||active==='week'?config.entities.at(-1):currentEntity)">＋ 新增{{(active==='overview'||active==='week'?config.entities.at(-1):currentEntity).singular}}</button></div>
      <div v-if="error" role="alert" class="alert">{{error}} <button @click="load">重试</button></div>
      <div v-if="notice" class="toast">✓ {{notice}}</div>
      <div v-if="loading" class="loading">正在加载记录…</div>
      <template v-else-if="active==='overview'">
        <div class="hero"><div><span class="hero-label">WELCOME TO {{config.en}}</span><h2>从用例到执行，<br>每一步都可追踪。</h2><p>{{config.subtitle}} · 从这里开始管理。</p><button @click="openEditor(config.entities.at(-1))">新增执行 <span>↗</span></button></div><div class="hero-art"><span class="orbit one"></span><span class="orbit two"></span><span class="hero-glyph">✳</span></div></div>
        <div class="stats"><article><span>记录总数</span><strong>{{config.entities.reduce((n,e)=>n+count(e.key),0)}}</strong><small>已保存到本机</small></article><article v-for="e in config.entities" :key="e.key"><span>{{e.title}}</span><strong>{{count(e.key)}}</strong><small>查看和管理 <b>→</b></small></article><article v-if="isSchedule"><span>未来安排</span><strong>{{weekItems.length}}</strong><small>待到来的日程</small></article><article v-else><span>最近更新</span><strong>{{recent.length}}</strong><small>快速查看记录</small></article></div>
        <div class="overview-grid"><section class="panel"><div class="panel-title"><div><span class="kicker">YOUR ACTIVITY</span><h3>最近记录</h3></div><button class="text-action" @click="navigate(config.entities.at(-1).key)">查看全部 ↗</button></div><div v-if="!recent.length" class="empty">还没有记录，点击右上角新增第一条。</div><div v-for="item in recent" :key="item.id" class="activity"><div class="activity-icon">{{config.entities.at(-1).icon}}</div><div><strong>{{activityLabel(item)}}</strong><small>{{item.eventDate||item.date||item.startAt||''}} · {{item.status||item.category||'已保存'}}</small></div><span class="activity-arrow">↗</span></div></section><section class="panel quick-panel"><div class="panel-title"><div><span class="kicker">QUICK ACTIONS</span><h3>快捷入口</h3></div></div><button v-for="e in config.entities" :key="e.key" class="quick" @click="navigate(e.key)"><span class="quick-icon">{{e.icon}}</span><span><b>{{e.title}}</b><small>查看、编辑与新增{{e.singular}}</small></span><em>→</em></button><div class="privacy"><b>◈ 你的数据，留在本机</b><span>演示版本不提供账号同步，请自行备份项目 data 文件。</span></div></section></div>
      </template>
      <template v-else-if="active==='week'"><section class="panel calendar-panel"><div class="panel-title"><div><span class="kicker">UPCOMING</span><h3>按日期查看未来安排</h3></div><span class="pill">{{weekItems.length}} 条日程</span></div><div v-if="!weekGroups.length" class="empty">近期没有安排，可以新增一条日程。</div><div v-for="[date,items] in weekGroups" :key="date" class="day-group"><h4>{{date}} <small>{{items.length}} 项</small></h4><div v-for="item in items" :key="item.id" class="event-row"><time>{{item.startAt.slice(11,16)}}<span>{{item.endAt.slice(11,16)}}</span></time><span class="event-line"></span><div><strong>{{item.title}}</strong><small>{{item.category}} · {{item.notes || '无备注'}}</small></div><span class="pill" :class="{muted:item.done}">{{item.done?'已完成':'待完成'}}</span><button class="text-action" @click="openEditor(config.entities[0],item)">编辑</button></div></div></section></template>
      <template v-else><section class="panel records-panel"><div class="panel-title"><div><span class="kicker">{{config.en}} / RECORDS</span><h3>{{currentEntity.title}} <small>{{count(currentEntity.key)}} 条</small></h3></div><button class="secondary" @click="openEditor(currentEntity)">＋ 添加{{currentEntity.singular}}</button></div><div class="toolbar"><input v-model="search" type="search" placeholder="搜索记录关键词…" aria-label="搜索记录"><span>支持新增、修改和删除 · 自动保存</span></div>
        <div v-if="!filtered.length" class="empty">{{search?'没有匹配的记录':'暂无记录，点击上方按钮开始。'}}</div>
        <div v-else class="table-wrap"><table><thead><tr><th v-for="col in currentEntity.columns" :key="col">{{fieldName(col)}}</th><th>操作</th></tr></thead><tbody><tr v-for="item in filtered" :key="item.id"><td v-for="(col,index) in currentEntity.columns" :key="col"><strong v-if="index===0">{{display(col,item[col])}}</strong><span v-else-if="col==='category'||col==='type'||col==='done'" class="pill">{{display(col,item[col])}}</span><span v-else>{{display(col,item[col])}}</span></td><td class="actions"><button @click="openEditor(currentEntity,item)">编辑</button><button v-if="currentEntity.key==='events'" @click="toggle(item)">{{item.done?'撤销完成':'完成'}}</button><button class="danger" @click="remove(currentEntity,item)">删除</button></td></tr></tbody></table></div>
      </section></template>
      <footer>AI-HUB · Vue 3 + Java 21 <span>本机单用户演示 · 不建议直接用于生产</span></footer>
    </main>
  </div>
  <div v-if="editorOpen" class="overlay" @click.self="editorOpen=false"><section class="drawer"><div class="drawer-head"><div><span class="kicker">RECORD EDITOR</span><h2>{{editId?'编辑':'新增'}}{{currentEntity.singular}}</h2></div><button class="close" aria-label="关闭" @click="editorOpen=false">×</button></div><form @submit.prevent="save"><label v-for="field in currentEntity.fields" :key="field[0]" class="field"><span>{{field[1]}} <b v-if="field[3]">*</b></span><select v-if="field[2]==='select'" v-model="form[field[0]]" :required="field[3]"><option v-for="x in field[4]" :key="x" :value="x">{{x}}</option></select><select v-else-if="field[2]==='relation'" v-model="form[field[0]]" required><option disabled value="">请选择</option><option v-for="x in rows[field[4]]||[]" :key="x.id" :value="x.id">{{x[config.entities.find(e=>e.key===field[4]).fields[0][0]]}}</option></select><textarea v-else-if="field[2]==='textarea'" v-model="form[field[0]]" rows="3" maxlength="500" placeholder="可选备注"></textarea><input v-else-if="field[2]==='checkbox'" v-model="form[field[0]]" type="checkbox" class="check"><input v-else v-model="form[field[0]]" :type="field[2]==='money'?'number':field[2]" :step="field[2]==='money'?'0.01':undefined" :min="field[2]==='money'||field[2]==='number'?'0':undefined" :required="field[3]" maxlength="500"></label><p v-if="currentEntity.fields.some(f=>f[2]==='relation')" class="form-help">请先创建对应档案，再添加记录。</p><div class="drawer-actions"><button type="button" class="cancel" @click="editorOpen=false">取消</button><button type="submit" class="primary">保存记录</button></div></form></section></div>
</div>
</template>
