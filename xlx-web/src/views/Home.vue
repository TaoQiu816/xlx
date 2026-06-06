<script setup lang="ts">
import { ref, onMounted, watch, computed } from 'vue'
import { useI18n } from 'vue-i18n'
import { siteApi } from '../api/site'
import SkeletonCard from '../components/SkeletonCard.vue'
import { useSEO } from '../composables/useSEO'
import { useOrganizationSchema, useBreadcrumbSchema } from '../composables/useJsonLd'
import { langField } from '../composables/useLangField'
import { getLocale } from '../i18n'

const { t } = useI18n()

useSEO({})

useOrganizationSchema({
  address: {
    '@type': 'PostalAddress',
    addressCountry: 'CN',
    addressRegion: '河北省',
    addressLocality: '衡水市安平县',
    streetAddress: '安平县丝网工业园区',
  },
  contactPoint: {
    '@type': 'ContactPoint',
    telephone: '+86-316-8888888',
    contactType: 'sales',
  },
})

useBreadcrumbSchema([
  { name: '首页', url: '/' },
])

useOrganizationSchema({
  address: {
    '@type': 'PostalAddress',
    addressCountry: 'CN',
    addressRegion: '河北省',
    addressLocality: '衡水市安平县',
    streetAddress: '安平县丝网工业园区',
  },
  contactPoint: {
    '@type': 'ContactPoint',
    telephone: '+86-316-8888888',
    contactType: 'sales',
  },
})

useBreadcrumbSchema([
  { name: '首页', url: '/' },
])

const props = defineProps<{
  config?: Record<string, string>
}>()

const categories = ref<any[]>([])
const featured = ref<any[]>([])
const certificates = ref<any[]>([])
const factoryImages = ref<any[]>([])
const catsLoading = ref(true)
const productsLoading = ref(true)

const lang = computed(() => getLocale())

const defaultFaqs = [
  { q: '支持哪些付款方式？', a: '我们支持T/T、L/C、Western Union等多种国际付款方式，具体可根据订单金额协商。', qEn: 'What payment methods do you accept?', aEn: 'We accept T/T, L/C, Western Union and other international payment methods. Specific terms can be negotiated based on order amount.', open: false },
  { q: '最小起订量是多少？', a: '不同产品起订量不同，常规产品一般100公斤起订，具体请联系我们获取报价。', qEn: 'What is the minimum order quantity?', aEn: 'MOQ varies by product. Standard products typically have a 100kg minimum. Please contact us for a quote.', open: false },
  { q: '交货周期多长？', a: '常规产品7-15个工作日，定制产品15-30个工作日，具体视订单数量和规格而定。', qEn: 'What is the delivery lead time?', aEn: 'Standard products: 7-15 business days. Custom products: 15-30 business days, depending on order quantity and specifications.', open: false },
  { q: '可以提供样品吗？', a: '可以提供免费样品（运费到付），样品一般3-5个工作日寄出。', qEn: 'Can you provide samples?', aEn: 'Yes, free samples are available (freight collect). Samples are typically shipped within 3-5 business days.', open: false },
  { q: '产品有质量保证吗？', a: '所有产品均经过严格质检，可提供材质证书和检测报告，如有质量问题可退换。', qEn: 'Do products come with quality assurance?', aEn: 'All products undergo strict quality inspection. Material certificates and test reports are available. Defective products can be returned or replaced.', open: false },
]

const faqs = ref(defaultFaqs)

const homeFaqs = computed(() => faqs.value.map(f => ({
  q: lang.value === 'en' ? (f.qEn || f.q) : f.q,
  a: lang.value === 'en' ? (f.aEn || f.a) : f.a,
  open: f.open,
})))

watch(() => props.config?.home_faq_items, (val) => {
  if (val) {
    try {
      const parsed = JSON.parse(val)
      if (Array.isArray(parsed)) {
        faqs.value = parsed.map((item: any) => ({ q: item.q, a: item.a, qEn: item.qEn || '', aEn: item.aEn || '', open: false }))
      }
    } catch {}
  }
}, { immediate: true })

const categoryIcons: Record<string, string> = {
  '丝网': 'M4 6h16M4 10h16M4 14h16M4 18h16',
  '铁丝': 'M13 10V3L4 14h7v7l9-11h-7z',
  '护栏': 'M9 12l2 2 4-4m5.618-4.016A11.955 11.955 0 0112 2.944a11.955 11.955 0 01-8.618 3.04A12.02 12.02 0 003 9c0 5.591 3.824 10.29 9 11.622 5.176-1.332 9-6.03 9-11.622 0-1.042-.133-2.052-.382-3.016z',
  '钢筋': 'M19.428 15.428a2 2 0 00-1.022-.547l-2.387-.477a6 6 0 00-3.86.517l-.318.158a6 6 0 01-3.86.517L6.05 15.21a2 2 0 00-1.806.547M8 4h8l-1 1v5.172a2 2 0 00.586 1.414l5 5c1.26 1.26.367 3.414-1.415 3.414H4.828c-1.782 0-2.674-2.154-1.414-3.414l5-5A2 2 0 009 10.172V5L8 4z',
}
const defaultIcon = 'M4 6h16M4 10h16M4 14h16M4 18h16'
const getCategoryIcon = (name: string) => {
  for (const [key, icon] of Object.entries(categoryIcons)) {
    if (name.includes(key)) return icon
  }
  return defaultIcon
}

// 各区块淡入动画
const visibleSections = ref<Record<string, boolean>>({})
const sectionRefs: Record<string, Element | null> = {}
const setSectionRef = (key: string) => (el: any) => { sectionRefs[key] = el }

onMounted(() => {
  Object.entries(sectionRefs).forEach(([key, el]) => {
    if (!el) return
    const observer = new IntersectionObserver(
      ([entry]) => { if (entry.isIntersecting) { visibleSections.value[key] = true; observer.disconnect() } },
      { threshold: 0.1 }
    )
    observer.observe(el)
  })
})

const toggleFaq = (index: number) => {
  faqs.value[index].open = !faqs.value[index].open
}

onMounted(async () => {
  try {
    const [catRes, prodRes, certRes, factoryRes] = await Promise.all([
      siteApi.categories(),
      siteApi.featuredProducts(8),
      siteApi.certificates(),
      siteApi.factoryImages(),
    ])
    categories.value = (catRes as any).data || []
    featured.value = (prodRes as any).data || []
    certificates.value = (certRes as any).data || []
    factoryImages.value = ((factoryRes as any).data || []).slice(0, 3)
  } catch {
    // silently fail
  } finally {
    catsLoading.value = false
    productsLoading.value = false
  }
})
</script>

<template>
  <div>
    <!-- Hero Banner -->
    <section class="hero">
      <div class="hero-mesh"></div>
      <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-20 md:py-32 relative z-10">
        <div class="max-w-2xl">
          <h1 class="text-4xl md:text-5xl lg:text-6xl font-bold leading-tight mb-6 text-white">
            {{ config?.[`banner_title${lang === 'en' ? '_en' : ''}`] || config?.banner_title || t('home.heroTitle') }}
          </h1>
          <p class="text-lg md:text-xl text-blue-200 mb-8 leading-relaxed">
            {{ config?.[`banner_subtitle${lang === 'en' ? '_en' : ''}`] || config?.banner_subtitle || t('home.heroSubtitle') }}
          </p>
          <div class="flex flex-wrap gap-4">
            <router-link to="/products" class="hero-btn hero-btn--primary">{{ t('products.allProducts') }}</router-link>
            <router-link to="/contact" class="hero-btn hero-btn--secondary">{{ t('nav.contactUs') }}</router-link>
          </div>
        </div>
        <!-- 统计数字 -->
        <div class="hero-stats">
          <div class="hero-stat">
            <span class="hero-stat-value">{{ config?.[`hero_stat1_value${lang === 'en' ? '_en' : ''}`] || config?.hero_stat1_value || '23年' }}</span>
            <span class="hero-stat-label">{{ config?.[`hero_stat1_label${lang === 'en' ? '_en' : ''}`] || config?.hero_stat1_label || '生产经验' }}</span>
          </div>
          <div class="hero-stat-divider"></div>
          <div class="hero-stat">
            <span class="hero-stat-value">{{ config?.[`hero_stat2_value${lang === 'en' ? '_en' : ''}`] || config?.hero_stat2_value || '50+' }}</span>
            <span class="hero-stat-label">{{ config?.[`hero_stat2_label${lang === 'en' ? '_en' : ''}`] || config?.hero_stat2_label || '产品型号' }}</span>
          </div>
          <div class="hero-stat-divider"></div>
          <div class="hero-stat">
            <span class="hero-stat-value">{{ config?.[`hero_stat3_value${lang === 'en' ? '_en' : ''}`] || config?.hero_stat3_value || '30+' }}</span>
            <span class="hero-stat-label">{{ config?.[`hero_stat3_label${lang === 'en' ? '_en' : ''}`] || config?.hero_stat3_label || '出口国家' }}</span>
          </div>
          <div class="hero-stat-divider"></div>
          <div class="hero-stat">
            <span class="hero-stat-value">{{ config?.[`hero_stat4_value${lang === 'en' ? '_en' : ''}`] || config?.hero_stat4_value || '1000+' }}</span>
            <span class="hero-stat-label">{{ config?.[`hero_stat4_label${lang === 'en' ? '_en' : ''}`] || config?.hero_stat4_label || '服务客户' }}</span>
          </div>
        </div>
      </div>
    </section>

    <!-- Selling Points -->
    <section :ref="setSectionRef('points')" class="py-16 bg-white"
      :class="visibleSections.points ? 'section-visible' : 'section-hidden'">
      <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
        <div class="grid grid-cols-1 md:grid-cols-4 gap-8">
          <div class="sp-card" v-for="(sp, i) in [
            { icon: 'M19.428 15.428a2 2 0 00-1.022-.547l-2.387-.477a6 6 0 00-3.86.517l-.318.158a6 6 0 01-3.86.517L6.05 15.21a2 2 0 00-1.806.547M8 4h8l-1 1v5.172a2 2 0 00.586 1.414l5 5c1.26 1.26.367 3.414-1.415 3.414H4.828c-1.782 0-2.674-2.154-1.414-3.414l5-5A2 2 0 009 10.172V5L8 4z' },
            { icon: 'M9 12l2 2 4-4m5.618-4.016A11.955 11.955 0 0112 2.944a11.955 11.955 0 01-8.618 3.04A12.02 12.02 0 003 9c0 5.591 3.824 10.29 9 11.622 5.176-1.332 9-6.03 9-11.622 0-1.042-.133-2.052-.382-3.016z' },
            { icon: 'M12 6V4m0 2a2 2 0 100 4m0-4a2 2 0 110 4m-6 8a2 2 0 100-4m0 4a2 2 0 110-4m0 4v2m0-6V4m6 6v10m6-2a2 2 0 100-4m0 4a2 2 0 110-4m0 4v2m0-6V4' },
            { icon: 'M3.055 11H5a2 2 0 012 2v1a2 2 0 002 2 2 2 0 012 2v2.945M8 3.935V5.5A2.5 2.5 0 0010.5 8h.5a2 2 0 012 2 2 2 0 104 0 2 2 0 012-2h1.064M15 20.488V18a2 2 0 012-2h3.064M21 12a9 9 0 11-18 0 9 9 0 0118 0z' },
          ]" :key="i">
            <div class="sp-icon">
              <svg class="w-7 h-7" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" :d="sp.icon"/></svg>
            </div>
            <div class="sp-num">{{ config?.[`sp${i+1}_num${lang === 'en' ? '_en' : ''}`] || config?.[`sp${i+1}_num`] || ['23年','100%','48h','30+'][i] }}</div>
            <h3 class="sp-title">{{ config?.[`sp${i+1}_title${lang === 'en' ? '_en' : ''}`] || config?.[`sp${i+1}_title`] || ['自有工厂','品质保障','定制加工','出口经验'][i] }}</h3>
            <p class="sp-desc">{{ config?.[`sp${i+1}_desc${lang === 'en' ? '_en' : ''}`] || config?.[`sp${i+1}_desc`] || ['南通市产业带源头工厂，产能充足，交期可控','严格质量管控体系，每批产品均附检测报告','支持来图来样定制，灵活满足非标需求','产品远销海外多国，熟悉出口包装与物流'][i] }}</p>
          </div>
        </div>
      </div>
    </section>

    <!-- Product Categories -->
    <section :ref="setSectionRef('cats')" class="py-16 bg-steel-50"
      :class="visibleSections.cats ? 'section-visible' : 'section-hidden'">
      <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
        <div class="text-center mb-12">
          <h2 class="text-3xl font-bold text-steel-900 mb-4">{{ t('products.title') }}</h2>
          <p class="text-steel-500 max-w-2xl mx-auto">{{ t('products.subtitle') }}</p>
        </div>
        <!-- Skeleton -->
        <div v-if="catsLoading" class="grid grid-cols-2 md:grid-cols-4 lg:grid-cols-5 gap-4">
          <div v-for="n in 5" :key="'sk-cat-'+n" class="cat-card">
            <div class="cat-icon"><div class="w-6 h-6 bg-steel-200 rounded animate-pulse"></div></div>
            <div class="h-4 bg-steel-200 rounded w-16 mx-auto animate-pulse"></div>
          </div>
        </div>
        <!-- Real -->
        <div v-else class="grid grid-cols-2 md:grid-cols-4 lg:grid-cols-5 gap-4">
          <router-link v-for="cat in categories" :key="cat.id" :to="`/products?category=${cat.id}`"
            class="cat-card group">
            <div class="cat-icon">
              <svg class="w-6 h-6 text-primary-600" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" :d="getCategoryIcon(langField(cat, 'name') || '')"/></svg>
            </div>
            <h3 class="font-semibold text-steel-800 text-sm">{{ langField(cat, 'name') }}</h3>
          </router-link>
        </div>
      </div>
    </section>

    <!-- Featured Products -->
    <section :ref="setSectionRef('products')" class="py-16 bg-white"
      :class="visibleSections.products ? 'section-visible' : 'section-hidden'">
      <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
        <div class="flex items-center justify-between mb-12">
          <div>
            <h2 class="text-3xl font-bold text-steel-900 mb-2">{{ t('home.ourProducts') }}</h2>
            <p class="text-steel-500">{{ t('products.subtitle') }}</p>
          </div>
          <router-link to="/products" class="view-all-link">
            {{ t('home.viewAll') }}
            <svg class="w-4 h-4 ml-1" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 5l7 7-7 7"/></svg>
          </router-link>
        </div>
        <!-- Skeleton -->
        <div v-if="productsLoading" class="product-grid">
          <SkeletonCard v-for="n in 8" :key="'sk-prod-'+n" :image="true" :lines="2" />
        </div>
        <!-- Real -->
        <div v-else class="product-grid">
          <router-link v-for="p in featured" :key="p.id" :to="`/products/${p.slug}`"
            class="product-card group">
            <div class="product-img-wrap">
              <img v-if="p.mainImage" :src="p.mainImage" :alt="p.name" class="product-img" loading="lazy" />
              <div v-else class="product-img-placeholder">
                <svg class="w-12 h-12" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="1.5" d="M4 16l4.586-4.586a2 2 0 012.828 0L16 16m-2-2l1.586-1.586a2 2 0 012.828 0L20 14m-6-6h.01M6 20h12a2 2 0 002-2V6a2 2 0 00-2-2H6a2 2 0 00-2 2v12a2 2 0 002 2z"/></svg>
              </div>
              <div class="product-overlay">
                <span class="product-overlay-btn">{{ t('productDetail.detailIntro') }}</span>
              </div>
              <span v-if="p.categoryName" class="product-badge">{{ p.categoryName }}</span>
            </div>
            <div class="p-4">
              <h3 class="font-semibold text-steel-800 text-sm mb-1 line-clamp-2 group-hover:text-primary-600 transition-colors">{{ langField(p, 'name') || p.name }}</h3>
            </div>
          </router-link>
        </div>
        <div class="text-center mt-8 md:hidden">
          <router-link to="/products" class="view-all-link">
            {{ t('products.allProducts') }}
            <svg class="w-4 h-4 ml-1" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 5l7 7-7 7"/></svg>
          </router-link>
        </div>
      </div>
    </section>

    <!-- Factory Preview -->
    <section :ref="setSectionRef('factory')" class="py-16 bg-steel-50"
      :class="visibleSections.factory ? 'section-visible' : 'section-hidden'">
      <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
        <div class="text-center mb-12">
          <h2 class="text-3xl font-bold text-steel-900 mb-4">{{ t('home.factoryStrength') }}</h2>
          <p class="text-steel-500 max-w-2xl mx-auto">{{ t('factory.subtitle') }}</p>
        </div>
        <div class="grid grid-cols-2 md:grid-cols-4 gap-4">
          <div v-for="(img, i) in factoryImages" :key="img.id || i" class="factory-img aspect-[4/3] bg-steel-200 rounded-lg overflow-hidden relative">
            <video v-if="img.mediaType === 'video'" :src="img.imageUrl"
              class="w-full h-full object-cover" muted preload="metadata" />
            <img v-else :src="img.imageUrl" :alt="langField(img, 'title') || t('home.factoryStrength')" class="w-full h-full object-cover" loading="lazy" />
            <span v-if="img.mediaType === 'video'" class="absolute bottom-2 right-2 bg-black/50 rounded-full p-1.5">
              <svg class="w-4 h-4 text-white" fill="currentColor" viewBox="0 0 24 24"><path d="M8 5v14l11-7z"/></svg>
            </span>
          </div>
          <div v-if="factoryImages.length === 0" v-for="n in 3" :key="'ph-'+n" class="factory-img aspect-[4/3] bg-steel-200 rounded-lg overflow-hidden"></div>
          <div class="aspect-[4/3] bg-steel-100 rounded-lg overflow-hidden flex items-center justify-center hover:bg-steel-200 transition-colors">
            <router-link to="/factory" class="text-primary-600 hover:text-primary-700 font-semibold">
              {{ t('home.viewAll') }} &rarr;
            </router-link>
          </div>
        </div>
      </div>
    </section>

    <!-- Certificates Preview -->
    <section v-if="certificates.length" :ref="setSectionRef('certs')" class="py-16 bg-white"
      :class="visibleSections.certs ? 'section-visible' : 'section-hidden'">
      <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
        <div class="text-center mb-12">
          <h2 class="text-3xl font-bold text-steel-900 mb-4">{{ t('home.certificates') }}</h2>
          <p class="text-steel-500 max-w-2xl mx-auto">{{ t('certificates.subtitle') }}</p>
        </div>
        <div class="grid grid-cols-2 md:grid-cols-4 gap-6">
          <div v-for="cert in certificates.slice(0, 4)" :key="cert.id" class="text-center group">
            <div class="aspect-[3/4] bg-steel-50 rounded-lg overflow-hidden mb-3 border border-steel-100 group-hover:shadow-md transition-shadow">
              <img v-if="cert.imageUrl" :src="cert.imageUrl" :alt="cert.name" class="w-full h-full object-contain" loading="lazy" />
            </div>
            <h4 class="font-medium text-steel-800 text-sm">{{ langField(cert, 'title') }}</h4>
          </div>
        </div>
        <div class="text-center mt-8">
          <router-link to="/certificates" class="view-all-link">
            {{ t('home.viewAll') }}
            <svg class="w-4 h-4 ml-1" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 5l7 7-7 7"/></svg>
          </router-link>
        </div>
      </div>
    </section>

    <!-- FAQ Preview -->
    <section :ref="setSectionRef('faq')" class="py-16 bg-steel-50"
      :class="visibleSections.faq ? 'section-visible' : 'section-hidden'">
      <div class="max-w-3xl mx-auto px-4 sm:px-6 lg:px-8">
        <div class="text-center mb-12">
          <h2 class="text-3xl font-bold text-steel-900 mb-4">{{ t('home.faqTitle') }}</h2>
          <p class="text-steel-500">{{ t('faq.subtitle') }}</p>
        </div>
        <div class="space-y-3">
          <div v-for="(faq, idx) in homeFaqs" :key="idx" class="faq-item">
            <button @click="toggleFaq(idx)" class="faq-trigger">
              <span class="font-medium text-steel-800 pr-4">{{ faq.q }}</span>
              <svg class="w-5 h-5 text-steel-400 flex-shrink-0 transition-transform duration-300" :class="{ 'rotate-180': faq.open }" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M19 9l-7 7-7-7"/></svg>
            </button>
            <div class="faq-answer" :class="{ 'faq-answer--open': faq.open }">
              <div class="faq-answer-inner">{{ faq.a }}</div>
            </div>
          </div>
        </div>
        <div class="text-center mt-8">
          <router-link to="/faq" class="view-all-link">
            {{ t('home.viewAll') }}
            <svg class="w-4 h-4 ml-1" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 5l7 7-7 7"/></svg>
          </router-link>
        </div>
      </div>
    </section>

    <!-- Contact CTA -->
    <section class="py-16 bg-primary-800 text-white">
      <div class="max-w-4xl mx-auto px-4 sm:px-6 lg:px-8 text-center">
        <h2 class="text-3xl font-bold mb-4">{{ t('home.ctaTitle') }}</h2>
        <p class="text-blue-200 mb-8 text-lg">{{ t('home.ctaSubtitle') }}</p>
        <div class="flex flex-wrap justify-center gap-4">
          <router-link to="/contact" class="inline-flex items-center px-8 py-3 bg-white text-primary-700 font-semibold rounded-lg hover:bg-blue-50 transition-all hover:shadow-lg">
            {{ t('home.contactNow') }}
          </router-link>
          <a v-if="config?.phone" :href="`tel:${config.phone}`" class="inline-flex items-center px-8 py-3 border-2 border-white text-white font-semibold rounded-lg hover:bg-white/10 transition-all">
            {{ t('productDetail.callConsult') }}
          </a>
        </div>
      </div>
    </section>
  </div>
</template>

<style scoped>
/* ===== Animations ===== */
.section-hidden {
  opacity: 0;
  transform: translateY(20px);
  transition: opacity 0.5s ease, transform 0.5s ease;
}

.section-visible {
  opacity: 1;
  transform: translateY(0);
}

/* ===== Hero ===== */
.hero {
  position: relative;
  background: linear-gradient(135deg, #0f172a 0%, #1e3a5f 50%, #0f172a 100%);
  overflow: hidden;
}

.hero-mesh {
  position: absolute;
  inset: 0;
  background-image:
    repeating-linear-gradient(0deg, transparent, transparent 40px, rgba(255,255,255,0.04) 40px, rgba(255,255,255,0.04) 41px),
    repeating-linear-gradient(90deg, transparent, transparent 40px, rgba(255,255,255,0.04) 40px, rgba(255,255,255,0.04) 41px);
  opacity: 0.8;
}

.hero-btn {
  display: inline-flex;
  align-items: center;
  padding: 0.875rem 2rem;
  font-weight: 600;
  border-radius: 8px;
  transition: all 0.2s;
  font-size: 1.05rem;
}

.hero-btn--primary {
  background: white;
  color: #1e40af;
}

.hero-btn--primary:hover {
  background: #eff6ff;
  transform: translateY(-2px);
  box-shadow: 0 4px 16px rgba(0, 0, 0, 0.15);
}

.hero-btn--secondary {
  border: 2px solid white;
  color: white;
}

.hero-btn--secondary:hover {
  background: rgba(255, 255, 255, 0.1);
  transform: translateY(-2px);
}

.hero-stats {
  display: flex;
  align-items: center;
  gap: 2rem;
  margin-top: 3rem;
  padding-top: 2rem;
  border-top: 1px solid rgba(255, 255, 255, 0.15);
  flex-wrap: wrap;
}

.hero-stat {
  text-align: center;
}

.hero-stat-value {
  display: block;
  font-size: 1.75rem;
  font-weight: 700;
  color: #60a5fa;
}

.hero-stat-label {
  font-size: 0.8rem;
  color: rgba(255, 255, 255, 0.6);
  margin-top: 0.25rem;
}

.hero-stat-divider {
  width: 1px;
  height: 40px;
  background: rgba(255, 255, 255, 0.15);
}

/* ===== Selling Points ===== */
.sp-card {
  text-align: center;
  padding: 1.5rem;
  border-radius: 12px;
  transition: box-shadow 0.2s, transform 0.2s;
}

.sp-card:hover {
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.06);
  transform: translateY(-2px);
}

.sp-icon {
  width: 56px;
  height: 56px;
  border-radius: 12px;
  background: linear-gradient(135deg, #dbeafe, #eff6ff);
  display: flex;
  align-items: center;
  justify-content: center;
  margin: 0 auto 1rem;
  color: #1e40af;
}

.sp-num {
  font-size: 1.25rem;
  font-weight: 700;
  color: #1e40af;
  margin-bottom: 0.25rem;
}

.sp-title {
  font-weight: 700;
  color: #1e293b;
  margin-bottom: 0.5rem;
}

.sp-desc {
  font-size: 0.85rem;
  color: #64748b;
  line-height: 1.5;
}

/* ===== Categories ===== */
.cat-card {
  display: block;
  background: white;
  border-radius: 12px;
  padding: 1.5rem;
  text-align: center;
  transition: all 0.2s;
  box-shadow: 0 1px 3px rgba(0, 0, 0, 0.04);
}

.cat-card:hover {
  box-shadow: 0 4px 16px rgba(0, 0, 0, 0.08);
  transform: translateY(-2px);
}

.cat-icon {
  width: 48px;
  height: 48px;
  border-radius: 50%;
  background: #eff6ff;
  display: flex;
  align-items: center;
  justify-content: center;
  margin: 0 auto 0.75rem;
  transition: background 0.2s;
}

.cat-card:hover .cat-icon {
  background: #dbeafe;
}

/* ===== Product Cards ===== */
.product-grid {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 1.5rem;
}

@media (min-width: 768px) {
  .product-grid { grid-template-columns: repeat(3, 1fr); }
}

@media (min-width: 1024px) {
  .product-grid { grid-template-columns: repeat(4, 1fr); }
}

.product-card {
  display: block;
  background: white;
  border-radius: 12px;
  border: 1px solid #f1f5f9;
  overflow: hidden;
  transition: all 0.25s;
}

.product-card:hover {
  box-shadow: 0 8px 24px rgba(0, 0, 0, 0.08);
  transform: translateY(-4px);
}

.product-img-wrap {
  position: relative;
  aspect-ratio: 1;
  background: #f8fafc;
  overflow: hidden;
}

.product-img {
  width: 100%;
  height: 100%;
  object-fit: cover;
  transition: transform 0.4s ease;
}

.product-card:hover .product-img {
  transform: scale(1.08);
}

.product-img-placeholder {
  width: 100%;
  height: 100%;
  display: flex;
  align-items: center;
  justify-content: center;
  color: #94a3b8;
}

.product-overlay {
  position: absolute;
  inset: 0;
  background: linear-gradient(to top, rgba(15, 23, 42, 0.7), transparent);
  display: flex;
  align-items: flex-end;
  justify-content: center;
  padding-bottom: 1.5rem;
  opacity: 0;
  transition: opacity 0.3s;
}

.product-card:hover .product-overlay {
  opacity: 1;
}

.product-overlay-btn {
  background: white;
  color: #1e40af;
  padding: 0.5rem 1.25rem;
  border-radius: 6px;
  font-size: 0.8rem;
  font-weight: 600;
  transform: translateY(8px);
  transition: transform 0.3s;
}

.product-card:hover .product-overlay-btn {
  transform: translateY(0);
}

.product-badge {
  position: absolute;
  top: 0.75rem;
  left: 0.75rem;
  background: rgba(30, 64, 175, 0.9);
  color: white;
  padding: 0.25rem 0.625rem;
  border-radius: 6px;
  font-size: 0.7rem;
  font-weight: 500;
}

/* ===== Factory ===== */
.factory-img {
  transition: transform 0.3s;
}

.factory-img:hover {
  transform: scale(1.02);
}

/* ===== FAQ ===== */
.faq-item {
  background: white;
  border-radius: 12px;
  overflow: hidden;
}

.faq-trigger {
  width: 100%;
  padding: 1rem 1.5rem;
  text-align: left;
  display: flex;
  align-items: center;
  justify-content: space-between;
  transition: background 0.2s;
}

.faq-trigger:hover {
  background: #f8fafc;
}

.faq-answer {
  max-height: 0;
  overflow: hidden;
  transition: max-height 0.3s ease;
}

.faq-answer--open {
  max-height: 200px;
}

.faq-answer-inner {
  padding: 0 1.5rem 1rem;
  color: #475569;
  font-size: 0.9rem;
  line-height: 1.6;
}

/* ===== Shared ===== */
.view-all-link {
  display: inline-flex;
  align-items: center;
  color: #1e40af;
  font-weight: 500;
  transition: color 0.2s;
}

.view-all-link:hover {
  color: #1e3a8a;
}

@media (max-width: 640px) {
  .hero-stats {
    gap: 1rem;
  }
  .hero-stat-divider {
    display: none;
  }
  .hero-stat-value {
    font-size: 1.25rem;
  }
}
</style>
