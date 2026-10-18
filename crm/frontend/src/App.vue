<script setup>
import { ref, computed, onMounted, watch } from 'vue'
import { translate, preferredLanguage } from './language.js'
import { opportunitiesCsv } from './export.js'
const language = ref(preferredLanguage())
const t = text => translate(text, language.value)
watch(language, value => {
  document.documentElement.lang = value === 'zh' ? 'zh-CN' : 'en'
  document.title = t('客户关系工作台') + ' · orbit'
  try { localStorage.setItem('orbit-language', value) } catch { /* Storage can be disabled. */ }
}, { immediate: true })
const customers = ref([]), opportunities = ref([]), stats = ref({ customers: 0, active: 0, won: 0, pipeline: 0 })
const selected = ref(null), activities = ref([]), tab = ref('商机看板'), modal = ref(''), busy = ref(false), error = ref(''), search = ref(''), note = ref('')
const editingCustomerId = ref(null)
const customerForm = ref({ name: '', contact: '', industry: '', owner: '' })
const dealForm = ref({ customerId: '', title: '', amount: '', owner: '' })
const stages = ['发现需求', '方案沟通', '商务谈判', '已赢单', '已流失']
const activeDeals = computed(() => opportunities.value.filter(o => !o.stage.startsWith('已')))
const visibleDeals = computed(() => opportunities.value.filter(o => `${o.title} ${customerName(o.customerId)} ${o.owner}`.toLocaleLowerCase().includes(search.value.toLocaleLowerCase())))
const currency = n => new Intl.NumberFormat(language.value === 'zh' ? 'zh-CN' : 'en-US', { style: 'currency', currency: 'CNY', maximumFractionDigits: 0 }).format(Number(n || 0))
const customerName = id => customers.value.find(c => c.id === id)?.name || t('未知客户')
async function api(path, options) {
  const response = await fetch('/api' + path, { headers: { 'Content-Type': 'application/json' }, ...options })
  if (!response.ok) throw new Error(`${t('操作失败')} (HTTP ${response.status}). ${t('请检查输入或当前状态')}`)
  return response.json()
}
async function load() {
  try {
    error.value = ''
    ;[customers.value, opportunities.value, stats.value] = await Promise.all([api('/customers'), api('/opportunities'), api('/stats')])
    if (selected.value) await selectDeal(opportunities.value.find(o => o.id === selected.value.id) || null)
  } catch (e) { error.value = e.message }
}
async function selectDeal(deal) {
  selected.value = deal
  if (!deal) { activities.value = []; return }
  try { activities.value = await api(`/opportunities/${deal.id}/activities`) } catch (e) { error.value = e.message }
}
async function act(work) { busy.value = true; error.value = ''; try { await work(); await load() } catch (e) { error.value = e.message } finally { busy.value = false } }
function openCustomer(customer = null) {
  editingCustomerId.value = customer?.id ?? null
  customerForm.value = customer
    ? { name: customer.name, contact: customer.contact, industry: customer.industry, owner: customer.owner }
    : { name: '', contact: '', industry: '', owner: '' }
  modal.value = 'customer'
}
async function saveCustomer() {
  await act(async () => {
    const id = editingCustomerId.value
    await api(id === null ? '/customers' : `/customers/${id}`, {
      method: id === null ? 'POST' : 'PUT', body: JSON.stringify(customerForm.value),
    })
    modal.value = ''
  })
}
async function createDeal() { await act(async () => { await api('/opportunities', { method: 'POST', body: JSON.stringify({ ...dealForm.value, customerId: Number(dealForm.value.customerId), amount: Number(dealForm.value.amount) }) }); dealForm.value = { customerId: '', title: '', amount: '', owner: '' }; modal.value = '' }) }
async function advance(deal, stage) { await act(() => api(`/opportunities/${deal.id}/stage`, { method: 'PATCH', body: JSON.stringify({ stage }) })) }
async function followUp() { if (!selected.value) return; await act(async () => { await api(`/opportunities/${selected.value.id}/activities`, { method: 'POST', body: JSON.stringify({ note: note.value }) }); note.value = '' }) }
function exportDeals() {
  const csv = opportunitiesCsv(visibleDeals.value, customers.value, t)
  const url = URL.createObjectURL(new Blob([csv], { type: 'text/csv;charset=utf-8' }))
  const link = document.createElement('a')
  link.href = url
  link.download = 'orbit-opportunities.csv'
  link.click()
  setTimeout(() => URL.revokeObjectURL(url), 1000)
}
onMounted(load)
</script>

<template>
<div class="app-shell">
  <aside class="sidebar">
    <div class="brand"><span class="brand-mark">◉</span><div><strong>orbit</strong><small>CRM WORKSPACE</small></div></div>
    <div class="workspace"><span class="workspace-icon">O</span><div><b>{{ t('星轨增长团队') }}</b><small>{{ t('演示工作空间') }}</small></div><span>⌄</span></div>
    <div class="nav-label">{{ t('工作空间') }}</div>
    <button :class="['nav-item', { active: tab === '商机看板' }]" @click="tab = '商机看板'"><span>▦</span> {{ t('商机看板') }}</button>
    <button :class="['nav-item', { active: tab === '客户列表' }]" @click="tab = '客户列表'"><span>♧</span> {{ t('客户列表') }}</button>
    <button :class="['nav-item', { active: tab === '全部商机' }]" @click="tab = '全部商机'"><span>◇</span> {{ t('全部商机') }}</button>
    <div class="nav-label secondary-label">{{ t('业务提醒') }}</div><div class="sidebar-note">{{ t('✦ 每一次跟进，都是关系的积累。') }}<small>{{ t('从客户到成交，记录完整业务轨迹。') }}</small></div>
    <div class="sidebar-bottom"><span class="avatar">{{ t('林') }}</span><div><b>{{ t('林知夏') }}</b><small>{{ t('演示用户 · 无真实鉴权') }}</small></div><span>···</span></div>
  </aside>
  <main class="main">
    <header class="topbar"><div>{{ t('工作空间') }} <span> / </span> {{ t(tab) }}</div><div class="top-actions"><select v-model="language" aria-label="Language / 语言" class="language-select"><option value="en">English</option><option value="zh">中文</option></select><span class="live-dot"></span> {{ t('本机演示数据') }} <span class="avatar small">{{ t('林') }}</span></div></header>
    <div class="content">
      <div v-if="error" class="alert">{{ error }} <button @click="load">{{ t('重试') }}</button><button @click="error = ''">×</button></div>
      <div class="page-heading"><div><div class="eyebrow">CUSTOMER RELATIONSHIP · 2026</div><h1>{{ t(tab === '商机看板' ? '让每个机会，向前一步。' : tab) }}</h1><p>{{ t(tab === '商机看板' ? '掌握销售节奏，聚焦值得投入的每一次对话。' : '清晰管理客户与商机，持续推动业务增长。') }}</p></div><button class="primary" @click="tab === '客户列表' ? openCustomer() : modal = 'deal'">＋ {{ t(tab === '客户列表' ? '新增客户' : '新建商机') }}</button></div>
      <div class="stat-grid"><div class="stat-card"><span>{{ t('客户总数') }}</span><strong>{{ stats.customers }}</strong><small>{{ t('当前工作空间') }}</small><i>♧</i></div><div class="stat-card"><span>{{ t('进行中商机') }}</span><strong>{{ stats.active }}</strong><small>{{ t('持续跟进中') }}</small><i>◇</i></div><div class="stat-card"><span>{{ t('预计商机金额') }}</span><strong>{{ currency(stats.pipeline) }}</strong><small>{{ t('未结束商机汇总') }}</small><i>↗</i></div><div class="stat-card"><span>{{ t('已赢单') }}</span><strong>{{ stats.won }}</strong><small>{{ t('累计转化项目') }}</small><i>✦</i></div></div>
      <template v-if="tab === '商机看板'"><div class="section-title"><div><h2>{{ t('销售漏斗') }}</h2><p>{{ t('跟随商机从发现需求走向成交') }}</p></div><button class="text-button" @click="tab = '全部商机'">{{ t('查看全部 →') }}</button></div><div class="board"><div v-for="(stage, index) in stages.slice(0,3)" :key="stage" class="stage-column"><div class="stage-head"><span class="stage-icon">{{ ['◌','◈','◆'][index] }}</span><b>{{ t(stage) }}</b><em>{{ activeDeals.filter(o => o.stage === stage).length }}</em></div><div v-for="deal in activeDeals.filter(o => o.stage === stage)" :key="deal.id" class="deal-card" @click="selectDeal(deal)"><div class="deal-customer">{{ customerName(deal.customerId) }}</div><h3>{{ deal.title }}</h3><strong>{{ currency(deal.amount) }}</strong><div class="deal-foot"><span class="mini-avatar">{{ deal.owner[0] }}</span>{{ deal.owner }}<span class="right">{{ deal.createdAt }}</span></div></div><div v-if="!activeDeals.some(o => o.stage === stage)" class="column-empty">{{ t('暂无商机 · 等待新的机会') }}</div></div></div></template>
      <template v-else-if="tab === '客户列表'"><div class="panel"><div class="panel-head"><h2>{{ t('客户档案') }} <em>{{ customers.length }}</em></h2><span>{{ t('建立连接，记录每一位合作伙伴') }}</span></div><div class="table-wrap"><table><thead><tr><th>{{ t('客户名称') }}</th><th>{{ t('联系人') }}</th><th>{{ t('所属行业') }}</th><th>{{ t('负责人') }}</th><th>{{ t('关联商机') }}</th><th>{{ t('操作') }}</th></tr></thead><tbody><tr v-for="customer in customers" :key="customer.id"><td><b class="customer-cell"><span class="customer-icon">{{ customer.name[0] }}</span>{{ customer.name }}</b></td><td>{{ customer.contact }}</td><td>{{ customer.industry }}</td><td>{{ customer.owner }}</td><td>{{ opportunities.filter(o => o.customerId === customer.id).length }}</td><td><button class="text-button" :disabled="busy" @click="openCustomer(customer)" :aria-label="t('编辑客户') + ': ' + customer.name">{{ t('编辑') }}</button></td></tr></tbody></table></div></div></template>
      <template v-else><div class="panel"><div class="panel-head"><button class="outline" :disabled="busy || !visibleDeals.length" @click="exportDeals">{{ t('导出 CSV') }}</button><h2>{{ t('全部商机') }} <em>{{ opportunities.length }}</em></h2><input v-model="search" :placeholder="t('搜索商机、客户或负责人')" /></div><div class="table-wrap"><table><thead><tr><th>{{ t('商机名称') }}</th><th>{{ t('客户') }}</th><th>{{ t('金额') }}</th><th>{{ t('阶段') }}</th><th>{{ t('负责人') }}</th><th>{{ t('操作') }}</th></tr></thead><tbody><tr v-for="deal in visibleDeals" :key="deal.id"><td><b>{{ deal.title }}</b></td><td>{{ customerName(deal.customerId) }}</td><td>{{ currency(deal.amount) }}</td><td><span :class="['badge', deal.stage.startsWith('已') ? 'muted' : '']">{{ t(deal.stage) }}</span></td><td>{{ deal.owner }}</td><td><button class="text-button" @click="selectDeal(deal)">{{ t('查看详情 →') }}</button></td></tr></tbody></table><div v-if="!visibleDeals.length" class="empty">{{ t('没有匹配的商机') }}</div></div></div></template>
      <footer>orbit CRM · Vue 3 + Java 21 <span>{{ t('演示记录保存在本机 data/crm.json') }}</span></footer>
    </div>
  </main>
  <div v-if="selected" class="overlay" @click.self="selected = null"><div class="drawer"><button class="close" @click="selected = null">×</button><div class="eyebrow">OPPORTUNITY / #{{ selected.id }}</div><h2>{{ selected.title }}</h2><p>{{ customerName(selected.customerId) }} · {{ selected.owner }}</p><div class="drawer-amount">{{ currency(selected.amount) }}<small>{{ t('预计成交金额') }}</small></div><div class="drawer-section"><h3>{{ t('商机阶段') }} <span class="badge">{{ t(selected.stage) }}</span></h3><div v-if="!selected.stage.startsWith('已')" class="action-row"><button v-if="selected.stage !== '商务谈判'" class="primary" :disabled="busy" @click="advance(selected, stages[stages.indexOf(selected.stage)+1])">{{ t('推进到下一阶段 →') }}</button><button v-if="selected.stage === '商务谈判'" class="primary" :disabled="busy" @click="advance(selected, '已赢单')">{{ t('标记赢单 ✓') }}</button><button class="outline" :disabled="busy" @click="advance(selected, '已流失')">{{ t('标记流失') }}</button></div></div><div class="drawer-section"><h3>{{ t('跟进记录') }} <span>{{ activities.length }}</span></h3><div v-if="!activities.length" class="empty">{{ t('尚无跟进记录') }}</div><div v-for="item in activities" :key="item.id" class="activity"><span>●</span><div><b>{{ item.note }}</b><small>{{ item.date }}</small></div></div><form v-if="!selected.stage.startsWith('已')" @submit.prevent="followUp"><textarea v-model="note" required :placeholder="t('记录一次沟通或下一步计划…')"></textarea><button class="primary" :disabled="busy">{{ t('添加跟进') }}</button></form></div></div></div>
  <div v-if="modal" class="overlay" @click.self="modal = ''"><form class="dialog" @submit.prevent="modal === 'customer' ? saveCustomer() : createDeal()"><button type="button" class="close" @click="modal = ''">×</button><div class="eyebrow">{{ modal === 'customer' && editingCustomerId !== null ? 'EDIT' : 'NEW' }} {{ modal === 'customer' ? 'CUSTOMER' : 'OPPORTUNITY' }}</div><h2>{{ t(modal === 'customer' ? (editingCustomerId === null ? '新增客户' : '编辑客户') : '新建商机') }}</h2><p>{{ t(modal === 'customer' && editingCustomerId !== null ? '更新客户资料，已有商机保持关联。' : '填写必要信息，开始一段新的业务旅程。') }}</p><p v-if="error" class="alert" role="alert">{{ error }}</p><template v-if="modal === 'customer'"><label>{{ t('客户名称') }}<input v-model="customerForm.name" required :placeholder="t('例如：青禾科技')" /></label><label>{{ t('联系人') }}<input v-model="customerForm.contact" required :placeholder="t('姓名与联系方式')" /></label><label>{{ t('所属行业') }}<input v-model="customerForm.industry" required :placeholder="t('例如：科技互联网')" /></label><label>{{ t('负责人') }}<input v-model="customerForm.owner" required :placeholder="t('例如：林知夏')" /></label></template><template v-else><label>{{ t('关联客户') }}<select v-model="dealForm.customerId" required><option value="" disabled>{{ t('选择客户') }}</option><option v-for="c in customers" :key="c.id" :value="c.id">{{ c.name }}</option></select></label><label>{{ t('商机名称') }}<input v-model="dealForm.title" required :placeholder="t('例如：年度服务采购')" /></label><label>{{ t('预计金额（元）') }}<input v-model="dealForm.amount" required type="number" min="1" placeholder="50000" /></label><label>{{ t('负责人') }}<input v-model="dealForm.owner" required :placeholder="t('例如：林知夏')" /></label></template><div class="dialog-actions"><button type="button" class="outline" @click="modal = ''">{{ t('取消') }}</button><button class="primary" :disabled="busy">{{ t(modal === 'customer' && editingCustomerId !== null ? '保存修改' : '确认创建') }}</button></div></form></div>
</div>
</template>
