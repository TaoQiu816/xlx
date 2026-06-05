<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { siteApi } from '../api/site'

defineProps<{
  config?: Record<string, string>
}>()

const categories = ref<any[]>([])
const featured = ref<any[]>([])
const certificates = ref<any[]>([])

const faqs = [
  { q: '支持哪些付款方式？', a: '我们支持T/T、L/C、Western Union等多种国际付款方式，具体可根据订单金额协商。', open: false },
  { q: '最小起订量是多少？', a: '不同产品起订量不同，常规产品一般100公斤起订，具体请联系我们获取报价。', open: false },
  { q: '交货周期多长？', a: '常规产品7-15个工作日，定制产品15-30个工作日，具体视订单数量和规格而定。', open: false },
  { q: '可以提供样品吗？', a: '可以提供免费样品（运费到付），样品一般3-5个工作日寄出。', open: false },
  { q: '产品有质量保证吗？', a: '所有产品均经过严格质检，可提供材质证书和检测报告，如有质量问题可退换。', open: false },
]

const toggleFaq = (index: number) => {
  faqs[index].open = !faqs[index].open
}

onMounted(async () => {
  try {
    const [catRes, prodRes, certRes] = await Promise.all([
      siteApi.categories(),
      siteApi.featuredProducts(8),
      siteApi.certificates(),
    ])
    categories.value = (catRes as any).data || []
    featured.value = (prodRes as any).data || []
    certificates.value = (certRes as any).data || []
  } catch {
    // silently fail, sections will show empty
  }
})
</script>

<template>
  <div>
    <!-- Hero Banner -->
    <section class="relative bg-primary-900 text-white overflow-hidden">
      <div class="absolute inset-0 opacity-10">
        <div class="absolute inset-0" style="background-image: repeating-linear-gradient(45deg, transparent, transparent 35px, rgba(255,255,255,0.05) 35px, rgba(255,255,255,0.05) 70px);"></div>
      </div>
      <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-20 md:py-32 relative">
        <div class="max-w-2xl">
          <h1 class="text-4xl md:text-5xl lg:text-6xl font-bold leading-tight mb-6">
            {{ config?.banner_title || '鑫连鑫丝网厂' }}
          </h1>
          <p class="text-lg md:text-xl text-blue-200 mb-8 leading-relaxed">
            {{ config?.banner_subtitle || '专业丝网与金属丝产品制造商，23年生产经验，支持标准规格供应与定制加工。' }}
          </p>
          <div class="flex flex-wrap gap-4">
            <router-link to="/products" class="inline-flex items-center px-8 py-3 bg-white text-primary-700 font-semibold rounded hover:bg-blue-50 transition-colors text-lg">
              浏览产品
            </router-link>
            <router-link to="/contact" class="inline-flex items-center px-8 py-3 border-2 border-white text-white font-semibold rounded hover:bg-white/10 transition-colors text-lg">
              立即咨询
            </router-link>
          </div>
        </div>
      </div>
    </section>

    <!-- Selling Points -->
    <section class="py-16 bg-white">
      <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
        <div class="grid grid-cols-1 md:grid-cols-4 gap-8">
          <div class="text-center p-6">
            <div class="w-14 h-14 bg-primary-100 rounded-lg flex items-center justify-center mx-auto mb-4">
              <svg class="w-7 h-7 text-primary-600" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M19.428 15.428a2 2 0 00-1.022-.547l-2.387-.477a6 6 0 00-3.86.517l-.318.158a6 6 0 01-3.86.517L6.05 15.21a2 2 0 00-1.806.547M8 4h8l-1 1v5.172a2 2 0 00.586 1.414l5 5c1.26 1.26.367 3.414-1.415 3.414H4.828c-1.782 0-2.674-2.154-1.414-3.414l5-5A2 2 0 009 10.172V5L8 4z"/></svg>
            </div>
            <h3 class="font-bold text-steel-800 mb-2">自有工厂</h3>
            <p class="text-sm text-steel-500">南通市产业带源头工厂，产能充足，交期可控</p>
          </div>
          <div class="text-center p-6">
            <div class="w-14 h-14 bg-primary-100 rounded-lg flex items-center justify-center mx-auto mb-4">
              <svg class="w-7 h-7 text-primary-600" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 12l2 2 4-4m5.618-4.016A11.955 11.955 0 0112 2.944a11.955 11.955 0 01-8.618 3.04A12.02 12.02 0 003 9c0 5.591 3.824 10.29 9 11.622 5.176-1.332 9-6.03 9-11.622 0-1.042-.133-2.052-.382-3.016z"/></svg>
            </div>
            <h3 class="font-bold text-steel-800 mb-2">品质保障</h3>
            <p class="text-sm text-steel-500">严格质量管控体系，每批产品均附检测报告</p>
          </div>
          <div class="text-center p-6">
            <div class="w-14 h-14 bg-primary-100 rounded-lg flex items-center justify-center mx-auto mb-4">
              <svg class="w-7 h-7 text-primary-600" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 6V4m0 2a2 2 0 100 4m0-4a2 2 0 110 4m-6 8a2 2 0 100-4m0 4a2 2 0 110-4m0 4v2m0-6V4m6 6v10m6-2a2 2 0 100-4m0 4a2 2 0 110-4m0 4v2m0-6V4"/></svg>
            </div>
            <h3 class="font-bold text-steel-800 mb-2">定制加工</h3>
            <p class="text-sm text-steel-500">支持来图来样定制，灵活满足非标需求</p>
          </div>
          <div class="text-center p-6">
            <div class="w-14 h-14 bg-primary-100 rounded-lg flex items-center justify-center mx-auto mb-4">
              <svg class="w-7 h-7 text-primary-600" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M3.055 11H5a2 2 0 012 2v1a2 2 0 002 2 2 2 0 012 2v2.945M8 3.935V5.5A2.5 2.5 0 0010.5 8h.5a2 2 0 012 2 2 2 0 104 0 2 2 0 012-2h1.064M15 20.488V18a2 2 0 012-2h3.064M21 12a9 9 0 11-18 0 9 9 0 0118 0z"/></svg>
            </div>
            <h3 class="font-bold text-steel-800 mb-2">出口经验</h3>
            <p class="text-sm text-steel-500">产品远销海外多国，熟悉出口包装与物流</p>
          </div>
        </div>
      </div>
    </section>

    <!-- Product Categories -->
    <section class="py-16 bg-steel-50">
      <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
        <div class="text-center mb-12">
          <h2 class="text-3xl font-bold text-steel-900 mb-4">产品分类</h2>
          <p class="text-steel-500 max-w-2xl mx-auto">涵盖金属丝、丝网两大品类，多种材质规格可选</p>
        </div>
        <div class="grid grid-cols-2 md:grid-cols-4 lg:grid-cols-5 gap-4">
          <router-link v-for="cat in categories" :key="cat.id" :to="`/products?category=${cat.id}`"
            class="bg-white rounded-lg p-6 text-center hover:shadow-lg transition-shadow group">
            <div class="w-12 h-12 bg-primary-50 rounded-full flex items-center justify-center mx-auto mb-3 group-hover:bg-primary-100 transition-colors">
              <svg class="w-6 h-6 text-primary-600" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M4 6h16M4 10h16M4 14h16M4 18h16"/></svg>
            </div>
            <h3 class="font-semibold text-steel-800 text-sm">{{ cat.name }}</h3>
          </router-link>
        </div>
      </div>
    </section>

    <!-- Featured Products -->
    <section class="py-16 bg-white">
      <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
        <div class="flex items-center justify-between mb-12">
          <div>
            <h2 class="text-3xl font-bold text-steel-900 mb-2">热门产品</h2>
            <p class="text-steel-500">精选畅销产品，品质与性价比兼具</p>
          </div>
          <router-link to="/products" class="hidden md:inline-flex items-center text-primary-600 hover:text-primary-700 font-medium">
            查看全部
            <svg class="w-4 h-4 ml-1" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 5l7 7-7 7"/></svg>
          </router-link>
        </div>
        <div class="grid grid-cols-2 md:grid-cols-3 lg:grid-cols-4 gap-6">
          <router-link v-for="p in featured" :key="p.id" :to="`/products/${p.slug}`"
            class="bg-white rounded-lg border border-steel-100 overflow-hidden hover:shadow-lg transition-shadow group">
            <div class="aspect-square bg-steel-100 overflow-hidden">
              <img v-if="p.mainImage" :src="p.mainImage" :alt="p.name" class="w-full h-full object-cover group-hover:scale-105 transition-transform duration-300" />
              <div v-else class="w-full h-full flex items-center justify-center text-steel-400">
                <svg class="w-12 h-12" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="1.5" d="M4 16l4.586-4.586a2 2 0 012.828 0L16 16m-2-2l1.586-1.586a2 2 0 012.828 0L20 14m-6-6h.01M6 20h12a2 2 0 002-2V6a2 2 0 00-2-2H6a2 2 0 00-2 2v12a2 2 0 002 2z"/></svg>
              </div>
            </div>
            <div class="p-4">
              <h3 class="font-semibold text-steel-800 text-sm mb-1 line-clamp-2 group-hover:text-primary-600 transition-colors">{{ p.name }}</h3>
              <p v-if="p.categoryName" class="text-xs text-steel-400">{{ p.categoryName }}</p>
            </div>
          </router-link>
        </div>
        <div class="text-center mt-8 md:hidden">
          <router-link to="/products" class="inline-flex items-center text-primary-600 hover:text-primary-700 font-medium">
            查看全部产品
            <svg class="w-4 h-4 ml-1" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 5l7 7-7 7"/></svg>
          </router-link>
        </div>
      </div>
    </section>

    <!-- Factory Preview -->
    <section class="py-16 bg-steel-50">
      <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
        <div class="text-center mb-12">
          <h2 class="text-3xl font-bold text-steel-900 mb-4">工厂实力</h2>
          <p class="text-steel-500 max-w-2xl mx-auto">现代化生产设备，严格的质量管理体系</p>
        </div>
        <div class="grid grid-cols-2 md:grid-cols-4 gap-4">
          <div class="aspect-[4/3] bg-steel-200 rounded-lg overflow-hidden">
            <img :src="'/uploads/factory/factory-1.jpg'" alt="工厂车间" class="w-full h-full object-cover" loading="lazy" onerror="this.style.display='none'" />
          </div>
          <div class="aspect-[4/3] bg-steel-200 rounded-lg overflow-hidden">
            <img :src="'/uploads/factory/factory-2.jpg'" alt="生产设备" class="w-full h-full object-cover" loading="lazy" onerror="this.style.display='none'" />
          </div>
          <div class="aspect-[4/3] bg-steel-200 rounded-lg overflow-hidden">
            <img :src="'/uploads/factory/factory-3.jpg'" alt="质检流程" class="w-full h-full object-cover" loading="lazy" onerror="this.style.display='none'" />
          </div>
          <div class="aspect-[4/3] bg-steel-200 rounded-lg overflow-hidden flex items-center justify-center">
            <router-link to="/factory" class="text-primary-600 hover:text-primary-700 font-semibold">
              查看更多 &rarr;
            </router-link>
          </div>
        </div>
      </div>
    </section>

    <!-- Certificates Preview -->
    <section v-if="certificates.length" class="py-16 bg-white">
      <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
        <div class="text-center mb-12">
          <h2 class="text-3xl font-bold text-steel-900 mb-4">资质证书</h2>
          <p class="text-steel-500 max-w-2xl mx-auto">通过多项质量认证，值得信赖的合作伙伴</p>
        </div>
        <div class="grid grid-cols-2 md:grid-cols-4 gap-6">
          <div v-for="cert in certificates.slice(0, 4)" :key="cert.id" class="text-center">
            <div class="aspect-[3/4] bg-steel-50 rounded-lg overflow-hidden mb-3 border border-steel-100">
              <img v-if="cert.imageUrl" :src="cert.imageUrl" :alt="cert.name" class="w-full h-full object-contain" loading="lazy" />
            </div>
            <h4 class="font-medium text-steel-800 text-sm">{{ cert.name }}</h4>
          </div>
        </div>
        <div class="text-center mt-8">
          <router-link to="/certificates" class="inline-flex items-center text-primary-600 hover:text-primary-700 font-medium">
            查看全部证书
            <svg class="w-4 h-4 ml-1" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 5l7 7-7 7"/></svg>
          </router-link>
        </div>
      </div>
    </section>

    <!-- FAQ Preview -->
    <section class="py-16 bg-steel-50">
      <div class="max-w-3xl mx-auto px-4 sm:px-6 lg:px-8">
        <div class="text-center mb-12">
          <h2 class="text-3xl font-bold text-steel-900 mb-4">常见问题</h2>
          <p class="text-steel-500">关于产品、交期、付款等常见问题解答</p>
        </div>
        <div class="space-y-3">
          <div v-for="(faq, idx) in faqs" :key="idx" class="bg-white rounded-lg overflow-hidden">
            <button @click="toggleFaq(idx)" class="w-full px-6 py-4 text-left flex items-center justify-between hover:bg-steel-50 transition-colors">
              <span class="font-medium text-steel-800 pr-4">{{ faq.q }}</span>
              <svg class="w-5 h-5 text-steel-400 flex-shrink-0 transition-transform" :class="{ 'rotate-180': faq.open }" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M19 9l-7 7-7-7"/></svg>
            </button>
            <div v-if="faq.open" class="px-6 pb-4 text-steel-600 text-sm leading-relaxed">
              {{ faq.a }}
            </div>
          </div>
        </div>
        <div class="text-center mt-8">
          <router-link to="/faq" class="inline-flex items-center text-primary-600 hover:text-primary-700 font-medium">
            查看更多问题
            <svg class="w-4 h-4 ml-1" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 5l7 7-7 7"/></svg>
          </router-link>
        </div>
      </div>
    </section>

    <!-- Contact CTA -->
    <section class="py-16 bg-primary-800 text-white">
      <div class="max-w-4xl mx-auto px-4 sm:px-6 lg:px-8 text-center">
        <h2 class="text-3xl font-bold mb-4">需要报价？</h2>
        <p class="text-blue-200 mb-8 text-lg">告诉我们您的需求，我们将在24小时内为您提供专业报价</p>
        <div class="flex flex-wrap justify-center gap-4">
          <router-link to="/contact" class="inline-flex items-center px-8 py-3 bg-white text-primary-700 font-semibold rounded hover:bg-blue-50 transition-colors">
            在线询盘
          </router-link>
          <a v-if="config?.phone" :href="`tel:${config.phone}`" class="inline-flex items-center px-8 py-3 border-2 border-white text-white font-semibold rounded hover:bg-white/10 transition-colors">
            电话咨询
          </a>
        </div>
      </div>
    </section>
  </div>
</template>
