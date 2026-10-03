<script setup>
import { computed, onMounted, reactive, ref } from 'vue'
const rows = ref([]), active = ref('overview'), selected = ref(null), loading = ref(false), error = ref(''), notice = ref('')
const search = ref(''), editor = ref(false), transitionNote = ref('')
const form = reactive({number:'',grid:'',category:'设施维护',title:'',description:'',assignee:''})
const nav = [{id:'overview',label:'运营总览',icon:'◫'},{id:'cases',label:'事件列表',icon:'▤'},{id:'trace',label:'流转轨迹',icon:'◷'}]
const states = ['待受理','处理中','待核验','已办结']
const next = {'待受理':['处理中'],'处理中':['待核验'],'待核验':['已办结','处理中']}
const filtered = computed(() => rows.value.filter(r => [r.number,r.grid,r.title,r.status,r.assignee].some(v=>String(v).toLowerCase().includes(search.value.toLowerCase()))))
const stats = computed(()=> states.map(name=>({name,count:rows.value.filter(row=>row.status===name).length})))
const chosen = computed(()=> rows.value.find(row=>row.id===selected.value) || rows.value[0])
async function api(path, options={}) {
  const response=await fetch('/api/'+path,{headers:{'Content-Type':'application/json'},...options})
  if(!response.ok) { let data={}; try { data=await response.json() } catch{}; throw new Error(data.detail||data.message||`请求失败 (${response.status})`) }
  return response.status===204?null:response.json()
}
async function load(){ loading.value=true; try {rows.value=await api('cases'); error.value=''}catch(e){error.value=e.message}finally{loading.value=false} }
function newCase(){Object.assign(form,{number:'',grid:'',category:'设施维护',title:'',description:'',assignee:''});editor.value=true;error.value=''}
async function save(){try{const created=await api('cases',{method:'POST',body:JSON.stringify(form)});await load();selected.value=created.id;editor.value=false;notice.value='事件已登记';active.value='cases'}catch(e){error.value=e.message}}
async function advance(to){if(!chosen.value)return;try{const id=chosen.value.id;await api(`cases/${id}/transition`,{method:'PUT',body:JSON.stringify({to,note:transitionNote.value})});transitionNote.value='';await load();selected.value=id;notice.value='流转记录已保存';error.value=''}catch(e){error.value=e.message}}
async function remove(row){if(!confirm(`删除事件 ${row.number}？此操作无法撤销。`))return;try{await api(`cases/${row.id}`,{method:'DELETE'});await load();selected.value=null;notice.value='事件已删除'}catch(e){error.value=e.message}}
function inspect(row){selected.value=row.id;active.value='trace';notice.value=''}
onMounted(load)
</script>

<template>
<div class="shell">
  <aside class="rail">
    <div class="identity"><div class="symbol">✳</div><div><strong>GRID<span>OPS</span></strong><small>AI-HUB / LOCAL STUDIO</small></div></div>
    <div class="rail-label">WORKSPACE / 01</div>
    <nav><button v-for="item in nav" :key="item.id" :class="{current:active===item.id}" @click="active=item.id"><span class="nav-icon">{{item.icon}}</span>{{item.label}}<span v-if="item.id==='cases'" class="nav-count">{{rows.length}}</span></button></nav>
    <div class="rail-bottom"><div class="pulse"></div><div><b>本机演示环境</b><small>仅使用虚构数据 · 无公网鉴权</small></div></div>
  </aside>
  <div class="workspace">
    <header class="topbar"><div>AI-HUB <span>/</span> 网格事件工作台 <span>/</span> {{nav.find(n=>n.id===active)?.label}}</div><div class="top-meta"><span class="online">● 服务运行中</span><span class="avatar">G</span></div></header>
    <main>
      <div class="page-heading"><div><div class="eyebrow">COMMUNITY OPERATIONS · ORIGINAL DEMO</div><h1>{{active==='overview'?'让每一件小事，有清晰的进度':active==='cases'?'事件登记与处置':'每一步处理，都有迹可循'}}</h1><p>登记 → 受理 → 处理 → 核验 → 办结。用一个可复现的本机流程体验事件管理。</p></div><button class="primary" @click="newCase">＋ 登记事件</button></div>
      <div v-if="error" class="alert" role="alert">{{error}} <button @click="error=''">关闭</button></div><div v-if="notice" class="notice">✓ {{notice}}</div>
      <section v-if="active==='overview'" class="overview">
        <div class="hero"><div><div class="eyebrow light">TODAY'S WORKSPACE</div><h2>从发现，到闭环。</h2><p>把需要跟进的事项放在可见、可追溯的队列里。这里只演示虚构网格事件，不处理真实居民信息。</p><button @click="active='cases'">查看事件队列 <span>↗</span></button></div><div class="hero-art"><div class="orb"></div><div class="mini-card"><span>处置流程</span><strong>04 <small>STAGES</small></strong><div class="progress"><i></i><i></i><i></i><i></i></div></div></div></div>
        <div class="metric-grid"><div class="metric" v-for="(stat,i) in stats" :key="stat.name"><span class="metric-icon">{{['◌','↗','◎','✓'][i]}}</span><small>{{stat.name}}</small><strong>{{String(stat.count).padStart(2,'0')}}</strong><p>当前事件</p></div></div>
        <div class="panel"><div class="panel-head"><div><div class="eyebrow">RECENT CASES</div><h3>最近登记</h3></div><button class="text-button" @click="active='cases'">全部事件 →</button></div><div v-for="row in rows.slice().reverse().slice(0,5)" :key="row.id" class="recent" @click="inspect(row)"><span class="recent-icon">⌁</span><div><b>{{row.title}}</b><small>{{row.number}} · {{row.grid}}</small></div><span class="badge" :class="row.status">{{row.status}}</span><span>↗</span></div></div>
      </section>
      <section v-if="active==='cases'" class="panel list-panel"><div class="panel-head"><div><div class="eyebrow">CASE DESK / {{rows.length}} RECORDS</div><h3>事件队列</h3></div><input v-model="search" class="search" placeholder="搜索编号、网格、标题或处理人" aria-label="搜索事件"></div><div v-if="loading" class="empty">正在加载…</div><div v-else-if="!filtered.length" class="empty">没有匹配的事件，试试登记一条虚构记录。</div><div v-for="row in filtered" :key="row.id" class="case-row"><div class="case-main"><span class="case-number">{{row.number}}</span><h4>{{row.title}}</h4><p>{{row.grid}} <span>·</span> {{row.category}} <span>·</span> 处理人 {{row.assignee}}</p></div><span class="badge" :class="row.status">{{row.status}}</span><button class="subtle" @click="inspect(row)">查看轨迹 →</button><button v-if="row.status==='待受理'" class="danger" @click="remove(row)">删除</button></div></section>
      <section v-if="active==='trace'" class="trace-layout"><div class="panel picker"><div class="panel-head"><div><div class="eyebrow">SELECT A CASE</div><h3>事件目录</h3></div></div><button v-for="row in rows" :key="row.id" :class="['pick', {picked:chosen?.id===row.id}]" @click="selected=row.id"><b>{{row.title}}</b><small>{{row.number}} · {{row.grid}}</small></button></div><div class="panel detail" v-if="chosen"><div class="eyebrow">CASE / {{chosen.number}}</div><div class="detail-title"><h2>{{chosen.title}}</h2><span class="badge" :class="chosen.status">{{chosen.status}}</span></div><p class="description">{{chosen.description}}</p><div class="detail-meta"><span>网格 <b>{{chosen.grid}}</b></span><span>分类 <b>{{chosen.category}}</b></span><span>处理人 <b>{{chosen.assignee}}</b></span></div><h3>处理时间线 <small>· {{chosen.history.length}} 步</small></h3><div class="timeline"><div v-for="(step,i) in chosen.history" :key="i" class="timeline-item"><i></i><div><b>{{step.to}}</b><small>{{step.at}}</small><p>{{step.note}}</p></div></div></div><div v-if="next[chosen.status]" class="transition"><label for="note">处理备注（必填）</label><textarea id="note" v-model="transitionNote" maxlength="300" placeholder="简述本次处置或核验情况"></textarea><div class="actions"><button v-for="to in next[chosen.status]" :key="to" class="primary" :disabled="!transitionNote.trim()" @click="advance(to)">流转至{{to}} →</button></div></div><div v-else class="complete">✓ 事件已办结，时间线保留供本机查阅。</div></div><div v-else class="panel empty">先登记事件，再查看处理轨迹。</div></section>
    </main>
  </div>
  <div v-if="editor" class="modal-backdrop" @click.self="editor=false"><form class="modal" @submit.prevent="save"><div class="modal-head"><div><div class="eyebrow">NEW CASE</div><h2>登记一件事</h2></div><button type="button" @click="editor=false" aria-label="关闭">✕</button></div><p>仅输入虚构演示信息，不要录入真实个人或敏感资料。</p><div class="form-grid"><label>事件编号<input v-model="form.number" maxlength="40" required placeholder="GRID-2026-001"></label><label>所在网格<input v-model="form.grid" maxlength="60" required placeholder="演示 B 网格"></label><label>事件分类<select v-model="form.category"><option>设施维护</option><option>环境治理</option><option>公共服务</option><option>其他事项</option></select></label><label>处理人<input v-model="form.assignee" maxlength="60" required placeholder="演示处理人"></label><label class="span-2">事件标题<input v-model="form.title" maxlength="120" required placeholder="简要描述问题"></label><label class="span-2">情况说明<textarea v-model="form.description" maxlength="500" required placeholder="虚构事件详情"></textarea></label></div><div class="modal-actions"><button type="button" class="subtle" @click="editor=false">取消</button><button class="primary" type="submit">保存事件 →</button></div></form></div>
</div>
</template>
