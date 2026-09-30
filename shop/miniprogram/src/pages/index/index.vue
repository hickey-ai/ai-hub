<script setup>
import { computed, ref } from 'vue'
import { onLoad, onShow } from '@dcloudio/uni-app'
import { request } from '../../services/api'

const products = ref([])
const categories = ['全部商品', '家居生活', '生活好物', '文具办公', '数码配件']
const category = ref('全部商品')
const keyword = ref('')
const sort = ref('default')
const loading = ref(false)
const error = ref('')
const cart = ref({})

const count = computed(() => Object.values(cart.value).reduce((sum, n) => sum + n, 0))
async function load() {
  loading.value = true
  try {
    const params = [`keyword=${encodeURIComponent(keyword.value)}`, `category=${encodeURIComponent(category.value === '全部商品' ? '' : category.value)}`, `sort=${sort.value}`, 'size=100'].join('&')
    const result = await request(`/api/products/search?${params}`)
    if (result.statusCode !== 200) throw new Error()
    products.value = result.data.items || []
    error.value = ''
  } catch { error.value = '商品服务暂不可用，请检查 API 地址和后端服务。' } finally { loading.value = false }
}
function search() { load() }
function add(product) { if ((cart.value[product.id] || 0) >= product.stock) return; cart.value = { ...cart.value, [product.id]: (cart.value[product.id] || 0) + 1 }; uni.setStorageSync('shop-cart', cart.value); uni.showToast({ title: '已加入购物车', icon: 'success' }) }
function open(product) { uni.navigateTo({ url: `/pages/detail/detail?id=${product.id}` }) }
function goCart() { uni.switchTab({ url: '/pages/cart/cart' }) }
onLoad(load)
onShow(() => { cart.value = uni.getStorageSync('shop-cart') || {}; if (!products.value.length) load() })
</script>

<template>
  <view class="page">
    <view class="topbar"><text>mori 生活好物</text><text>满 ¥299 包邮</text></view>
    <view class="header"><view class="logo">m.</view><view class="search"><input v-model="keyword" confirm-type="search" placeholder="搜索商品、品牌或关键词" @confirm="search"/><text @tap="search">搜索</text></view><view class="cart" @tap="goCart">🛒<text v-if="count" class="count-badge">{{ count }}</text></view></view>
    <scroll-view class="categories" scroll-x><view v-for="item in categories" :key="item" :class="['category', {active: category===item}]" @tap="category=item; load()">{{ item }}</view></scroll-view>
    <view class="banner"><text class="banner-title">精选好物<br/><text>让每一天更值得期待</text></text><text class="banner-icon">✦</text></view>
    <view class="toolbar"><text>共 {{ products.length }} 件商品</text><picker :range="['综合排序','新品优先','价格从低到高','价格从高到低']" @change="sort=['default','newest','price_asc','price_desc'][$event.detail.value]; load()"><text class="sort">{{ sort==='default'?'综合排序':sort==='newest'?'新品优先':sort==='price_asc'?'价格从低到高':'价格从高到低' }}　⌄</text></picker></view>
    <view v-if="error" class="notice">{{ error }}</view><view v-if="loading" class="loading">加载中...</view>
    <view class="grid"><view v-for="product in products" :key="product.id" class="card" @tap="open(product)"><view :class="['visual', product.color]"><text class="icon">{{ product.icon }}</text><text v-if="product.badge" class="badge">{{ product.badge }}</text></view><text class="category-label">{{ product.category }}</text><text class="name">{{ product.name }}</text><text class="desc">{{ product.description }}</text><view class="price-line"><text class="price">¥{{ Number(product.price).toFixed(2) }}</text><button class="add" :disabled="!product.stock" @tap.stop="add(product)">+</button></view></view></view>
    <view v-if="!loading && !products.length && !error" class="empty">没有找到相关商品</view>
  </view>
</template>

<style scoped>
.page{min-height:100vh;background:#f5f5f5;padding-bottom:30rpx}.topbar{height:48rpx;background:#3b3b3b;color:#ddd;display:flex;justify-content:space-between;align-items:center;padding:0 28rpx;font-size:22rpx}.header{height:112rpx;background:#fff;display:flex;align-items:center;padding:0 24rpx;gap:18rpx}.logo{width:72rpx;height:72rpx;border-radius:50%;background:#e1251b;color:#fff;font:italic 52rpx Georgia;display:flex;align-items:center;justify-content:center}.search{height:68rpx;flex:1;border:2rpx solid #e1251b;display:flex;align-items:center;background:#fff}.search input{flex:1;padding:0 18rpx;font-size:24rpx}.search text{height:68rpx;width:92rpx;background:#e1251b;color:#fff;display:flex;align-items:center;justify-content:center;font-size:24rpx}.cart{font-size:38rpx;position:relative}.cart .count-badge{position:absolute;right:-10rpx;top:-12rpx;background:#e1251b;color:#fff;font-size:18rpx;min-width:28rpx;height:28rpx;border-radius:50%;text-align:center;line-height:28rpx}.categories{white-space:nowrap;background:#fff;height:76rpx}.category{display:inline-flex;height:76rpx;padding:0 27rpx;align-items:center;font-size:25rpx;color:#555}.category.active{color:#e1251b;border-bottom:4rpx solid #e1251b;font-weight:bold}.banner{margin:20rpx 24rpx;background:linear-gradient(110deg,#f7e2d3,#dce8d7);height:235rpx;padding:44rpx 38rpx;display:flex;justify-content:space-between;align-items:center}.banner-title{font-size:44rpx;line-height:1.5;font-weight:bold;color:#31523f}.banner-title text{font-size:25rpx;font-weight:normal}.banner-icon{font-size:140rpx;color:#d88968}.toolbar{height:90rpx;padding:0 28rpx;display:flex;justify-content:space-between;align-items:center;font-size:24rpx;color:#666}.sort{background:#fff;padding:13rpx 18rpx;border:1rpx solid #ddd}.grid{display:grid;grid-template-columns:1fr 1fr;gap:18rpx;padding:0 20rpx}.card{background:#fff;padding-bottom:20rpx;overflow:hidden}.visual{height:260rpx;display:flex;align-items:center;justify-content:center;position:relative}.icon{font-size:120rpx;filter:drop-shadow(12rpx 14rpx 8rpx #5554)}.peach{background:#f4dfcb}.sage{background:#dce9d5}.lilac{background:#e4dfeb}.sand{background:#ede5d4}.blue{background:#d6e2e9}.rose{background:#f2e0df}.badge{position:absolute;left:14rpx;top:14rpx;background:#fff;padding:5rpx 10rpx;font-size:18rpx;color:#a86d52}.category-label,.name,.desc{display:block;padding:0 18rpx}.category-label{font-size:19rpx;color:#9a6b52;margin-top:16rpx}.name{font-size:27rpx;font-weight:bold;margin-top:8rpx}.desc{font-size:20rpx;color:#999;white-space:nowrap;overflow:hidden;text-overflow:ellipsis;margin-top:8rpx}.price-line{padding:0 18rpx;display:flex;justify-content:space-between;align-items:center;margin-top:16rpx}.price{color:#e1251b;font-size:30rpx;font-weight:bold}.add{margin:0;background:#e1251b;color:#fff;border-radius:50%;width:52rpx;height:52rpx;line-height:48rpx;padding:0;font-size:38rpx}.notice,.empty,.loading{text-align:center;padding:50rpx;color:#888;font-size:24rpx}
</style>
