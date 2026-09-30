<script setup>
import { computed, onMounted, ref } from 'vue'
const products = ref([])
const category = ref('全部商品')
const query = ref('')
const cart = ref({})
const cartOpen = ref(false)
const checkoutOpen = ref(false)
const completed = ref(null)
const loading = ref(false)
const error = ref('')
const customer = ref('')
const email = ref('')
const categories = ['全部商品', '家居生活', '生活好物', '文具办公', '数码配件']
const filtered = computed(() => products.value.filter(p => (category.value === '全部商品' || p.category === category.value) && (p.name.toLowerCase().includes(query.value.toLowerCase()) || p.description.includes(query.value))))
const lines = computed(() => products.value.filter(p => cart.value[p.id]).map(p => ({ ...p, quantity: cart.value[p.id] })))
const count = computed(() => lines.value.reduce((n, p) => n + p.quantity, 0))
const total = computed(() => lines.value.reduce((n, p) => n + p.price * p.quantity, 0))
async function refresh() { try { const r = await fetch('/api/products'); if (!r.ok) throw Error(); products.value = await r.json(); error.value = '' } catch { error.value = '商品服务暂不可用，请启动 shop/backend 后刷新。' } }
function add(p) { if ((cart.value[p.id] || 0) >= p.stock) return; cart.value = { ...cart.value, [p.id]: (cart.value[p.id] || 0) + 1 }; cartOpen.value = true }
function change(p, delta) { const next = Math.max(0, Math.min(p.stock, (cart.value[p.id] || 0) + delta)); cart.value = { ...cart.value, [p.id]: next } }
async function checkout() { loading.value = true; error.value = ''; try { const r = await fetch('/api/orders', { method:'POST', headers:{'Content-Type':'application/json'}, body: JSON.stringify({ customer: customer.value, email: email.value, items: lines.value.map(p => ({productId:p.id,quantity:p.quantity})) }) }); if (!r.ok) throw Error(r.status === 409 ? '商品库存已变化，请刷新后重试。' : '下单失败，请检查姓名和邮箱。'); completed.value = await r.json(); cart.value = {}; checkoutOpen.value = false; cartOpen.value = false; await refresh() } catch(e) { error.value = e.message } finally { loading.value = false } }
onMounted(refresh)
</script>

<template>
  <div class="announcement">日常有好物 · 满 ¥299 包邮 <span>✦</span> 一起发现生活的小确幸</div>
  <header class="site-header"><div class="header-inner"><a class="brand" href="#home"><span class="brand-mark">m.</span><span>mori<small>the little things</small></span></a><nav><a href="#products">所有商品</a><a href="#story">品牌故事</a><a href="#footer">联系我们</a></nav><div class="header-actions"><label class="search"><span>⌕</span><input v-model="query" placeholder="搜索心动好物..." aria-label="搜索商品"/></label><button class="cart-button" @click="cartOpen=true" aria-label="打开购物车">🛒 <b>{{ count }}</b></button></div></div></header>
  <main id="home"><section class="hero"><div class="hero-inner"><div class="hero-copy"><div class="eyebrow"><i></i> CURATED FOR EVERYDAY JOY</div><h1>把日子，<br/>过成<span>喜欢的样子。</span></h1><p>用一点点精心挑选的好物，让寻常的每一天，<br/>都多一点值得期待的小美好。</p><a class="primary-button" href="#products">探索精选好物 <span>↗</span></a><div class="hero-proof"><div class="avatars">👩🏻‍🦰 👨🏻‍🦱 👩🏽</div><div><strong>2,000+</strong><small>和你一样热爱生活的人</small></div><span class="stars">★★★★★</span></div></div><div class="hero-art"><div class="art-orbit orbit-one"></div><div class="art-orbit orbit-two"></div><div class="art-leaf leaf-one">✦</div><div class="art-leaf leaf-two">✳</div><div class="hero-card"><div class="hero-object">💡</div><span>mori essentials</span><strong>一点光，刚刚好。</strong></div><div class="floating-label"><span>✺</span><div>好物相伴<small>Make everyday lovely</small></div></div></div></div></section>
  <section class="features"><div><span>♧</span><b>用心挑选</b><small>每一件都值得被喜欢</small></div><div><span>♧</span><b>安心品质</b><small>让好体验陪伴更久</small></div><div><span>♧</span><b>温暖服务</b><small>你的满意是我们的心意</small></div><div><span>♧</span><b>轻松购物</b><small>找到属于你的生活灵感</small></div></section>
  <section class="products-section" id="products"><div class="section-head"><div><div class="eyebrow">THE MORI COLLECTION</div><h2>为生活加点<span>小心意</span></h2><p>那些让你忍不住微笑的小东西，都在这里。</p></div><span class="product-count">{{ filtered.length }} 件精选好物</span></div><div class="filters"><button v-for="c in categories" :key="c" :class="{active:category===c}" @click="category=c">{{ c }}</button></div><div v-if="error && !products.length" class="notice">{{ error }}</div><div class="product-grid"><article v-for="p in filtered" :key="p.id" class="product-card"><div class="product-visual" :class="p.color"><span v-if="p.badge" class="badge">{{ p.badge }}</span><div class="product-shape">{{ p.icon }}</div><button class="quick-add" :disabled="p.stock===0" @click="add(p)">＋ 加入购物车</button></div><div class="product-meta"><span>{{ p.category }}</span><span>♡</span></div><h3>{{ p.name }}</h3><p>{{ p.description }}</p><div class="price-row"><strong>¥{{ Number(p.price).toFixed(2) }}</strong><small>库存 {{ p.stock }}</small></div></article></div><div v-if="!filtered.length && products.length" class="empty">没有找到相关好物，试试其他关键词吧。</div></section>
  <section class="story" id="story"><span>✦ MORI STORIES</span><h2>生活不是等待特别的日子，<br/>而是让每个日子都变得特别。</h2><p>我们相信，好的设计不必遥远。一盏灯、一只杯子、一本手帐，<br/>都可以成为认真生活的小小注脚。</p><a href="#products">继续探索 →</a></section></main>
  <footer id="footer"><b>mori<span>.</span></b><span>把日子过成喜欢的样子 · 联系 3174667330@qq.com</span><small>© 2026 mori · ai-hub demo</small></footer>
  <div v-if="cartOpen" class="overlay" @click.self="cartOpen=false"><aside class="drawer"><div class="drawer-head"><div><small>YOUR BAG</small><h2>我的购物车 <span>({{ count }})</span></h2></div><button @click="cartOpen=false">✕</button></div><div class="cart-lines"><div v-if="!lines.length" class="cart-empty">购物车还空着，去挑点喜欢的吧 ♡</div><div v-for="p in lines" :key="p.id" class="cart-line"><div class="mini-visual" :class="p.color">{{ p.icon }}</div><div><b>{{ p.name }}</b><small>¥{{ Number(p.price).toFixed(2) }}</small><div class="qty"><button @click="change(p,-1)">−</button><span>{{ p.quantity }}</span><button @click="change(p,1)">+</button></div></div><strong>¥{{ (p.price*p.quantity).toFixed(2) }}</strong></div></div><div class="drawer-bottom"><div>商品合计 <strong>¥{{ total.toFixed(2) }}</strong></div><small>演示订单，不会产生真实付款或物流</small><button class="primary-button" :disabled="!lines.length" @click="checkoutOpen=true">去结算 →</button></div></aside></div>
  <div v-if="checkoutOpen" class="overlay top" @click.self="checkoutOpen=false"><form class="modal" @submit.prevent="checkout"><button type="button" class="close" @click="checkoutOpen=false">✕</button><div class="eyebrow">CHECKOUT</div><h2>完成演示订单</h2><p>填写联系信息，确认 ¥{{ total.toFixed(2) }} 的商品。</p><label>称呼<input v-model="customer" required placeholder="你的名字"/></label><label>邮箱<input v-model="email" required type="email" placeholder="you@example.com"/></label><p v-if="error" class="form-error">{{ error }}</p><button class="primary-button" :disabled="loading">{{ loading ? '提交中...' : '确认下单' }}</button></form></div>
  <div v-if="completed" class="overlay top" @click.self="completed=null"><div class="modal success"><div class="success-icon">✓</div><h2>下单成功！</h2><p>演示订单 #{{ completed.id }} 已创建，金额 ¥{{ Number(completed.total).toFixed(2) }}。</p><button class="primary-button" @click="completed=null">继续逛逛</button></div></div>
</template>
