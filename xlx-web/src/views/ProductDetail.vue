<script setup lang="ts">
import { ref, onMounted, computed } from 'vue'
import { useRoute } from 'vue-router'
import { useI18n } from 'vue-i18n'
import { siteApi } from '../api/site'
import { useSEO } from '../composables/useSEO'
import { useHead } from '@unhead/vue'
import { useProductSchema, useBreadcrumbSchema } from '../composables/useJsonLd'
import { langField } from '../composables/useLangField'
import { useQuoteCart } from '../composables/useQuoteCart'
import { useToast } from '../composables/useToast'

const { t } = useI18n()
const { addItem, hasItem } = useQuoteCart()
const toast = useToast()

useSEO()

useBreadcrumbSchema([
  { name: '首页', url: '/' },
  { name: '产品中心', url: '/products' },
  { name: '产品详情' },
])

const route = useRoute()
const product = ref<any>({})
const loading = ref(true)
const activeImage = ref('')
const showInquiry = ref(false)
const relatedProducts = ref<any[]>([])

const isInQuoteCart = computed(() => product.value.id ? hasItem(product.value.id) : false)

function handleAddToQuoteCart() {
  if (!product.value.id) return
  if (isInQuoteCart.value) {
    toast.show(t('quoteCart.addItemExists'), 'info')
    return
  }
  addItem({
    productId: product.value.id,
    slug: product.value.slug || '',
    nameCn: product.value.nameCn || product.value.name || '',
    nameEn: product.value.nameEn || '',
    image: product.value.mainImage || '',
    quantity: '',
    specification: '',
  })
  toast.show(t('quoteCart.addItemSuccess'), 'success')
}

// Lightbox
const showLightbox = ref(false)
const lightboxIndex = ref(0)

const inquiryForm = ref({
  name: '', company: '', phone: '', email: '',
  wechat: '', whatsapp: '', country: '',
  quantity: '', specification: '', message: '',
})
const submitting = ref(false)
const submitted = ref(false)
const errorMessage = ref('')

const images = computed(() => {
  const list: string[] = []
  if (product.value.mainImage) list.push(product.value.mainImage)
  if (product.value.images) {
    for (const img of product.value.images) {
      if (img.imageUrl && !list.includes(img.imageUrl)) list.push(img.imageUrl)
    }
  }
  return list
})

const specs = computed(() => {
  return product.value.specs || []
})

const setActiveImage = (url: string) => { activeImage.value = url }

const openLightbox = (url: string) => {
  lightboxIndex.value = images.value.indexOf(url)
  showLightbox.value = true
}

const closeLightbox = () => { showLightbox.value = false }

const lightboxPrev = () => {
  lightboxIndex.value = (lightboxIndex.value - 1 + images.value.length) % images.value.length
}

const lightboxNext = () => {
  lightboxIndex.value = (lightboxIndex.value + 1) % images.value.length
}

const lightboxImage = computed(() => images.value[lightboxIndex.value] || '')

const submitInquiry = async () => {
  submitting.value = true
  errorMessage.value = ''
  try {
    await siteApi.submitInquiry({
      ...inquiryForm.value,
      productId: product.value.id,
      productName: product.value.nameCn || product.value.name,
    })
    submitted.value = true
  } catch (err: any) {
    errorMessage.value = err?.message || '提交失败，请稍后重试'
  } finally {
    submitting.value = false
  }
}

onMounted(async () => {
  try {
    const res: any = await siteApi.productDetail(route.params.slug as string)
    product.value = res.data || {}
    relatedProducts.value = res.data?.relatedProducts || []
    if (images.value.length) activeImage.value = images.value[0]

    // 动态 SEO
    const name = product.value.nameCn || product.value.name || ''
    const summary = product.value.summaryCn || product.value.summary || ''
    useHead({
      title: name ? `${name} - 鑫连鑫丝网厂` : undefined,
      meta: [
        { name: 'description', content: summary || `${name} - 鑫连鑫丝网厂专业生产` },
        { property: 'og:title', content: name ? `${name} - 鑫连鑫丝网厂` : undefined },
        { property: 'og:description', content: summary || `${name} - 鑫连鑫丝网厂专业生产` },
        { property: 'og:image', content: product.value.mainImage || undefined },
      ],
    })

    // Product JSON-LD
    useProductSchema({
      name,
      description: product.value.descriptionCn || product.value.descriptionCn || summary,
      mainImage: product.value.mainImage,
      price: product.value.price,
      priceUnit: product.value.priceUnit,
      showPrice: product.value.showPrice,
    })
  } catch (err: any) {
    errorMessage.value = err?.message || '加载产品详情失败'
  } finally {
    loading.value = false
  }
})
</script>

<template>
  <div>
    <!-- Loading -->
    <div v-if="loading" class="max-w-7xl mx-auto px-4 py-20 text-center text-steel-400">{{ t('factory.loading') }}</div>

    <!-- Not Found -->
    <div v-else-if="!product.id" class="max-w-7xl mx-auto px-4 py-20 text-center">
      <p class="text-steel-500 mb-4">{{ t('products.noResults') }}</p>
      <router-link to="/products" class="text-primary-600 hover:text-primary-700">{{ t('products.allProducts') }}</router-link>
    </div>

    <!-- Product Detail -->
    <div v-else>
      <!-- Breadcrumb -->
      <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-4">
        <nav class="flex items-center text-sm text-steel-400">
          <router-link to="/" class="hover:text-primary-600">{{ t('nav.home') }}</router-link>
          <span class="mx-2">/</span>
          <router-link to="/products" class="hover:text-primary-600">{{ t('products.title') }}</router-link>
          <span class="mx-2">/</span>
          <span class="text-steel-700">{{ langField(product, 'name') || product.name }}</span>
        </nav>
      </div>

      <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 pb-16">
        <div class="grid grid-cols-1 lg:grid-cols-2 gap-12">
          <!-- Images -->
          <div>
            <div class="aspect-square bg-steel-100 rounded-lg overflow-hidden mb-4 cursor-pointer" @click="openLightbox(activeImage)">
              <img v-if="activeImage" :src="activeImage" :alt="product.nameCn" class="w-full h-full object-contain hover:opacity-95 transition-opacity" />
              <div v-else class="w-full h-full flex items-center justify-center text-steel-400">{{ t('factory.noContent') }}</div>
            </div>
            <div v-if="images.length > 1" class="flex gap-3 overflow-x-auto pb-2">
              <button v-for="(img, idx) in images" :key="idx" @click="setActiveImage(img)"
                class="w-20 h-20 flex-shrink-0 rounded border-2 overflow-hidden transition-colors"
                :class="activeImage === img ? 'border-primary-600' : 'border-steel-200 hover:border-steel-300'">
                <img :src="img" class="w-full h-full object-cover" />
              </button>
            </div>
          </div>

          <!-- Info -->
          <div>
            <h1 class="text-2xl md:text-3xl font-bold text-steel-900 mb-4">{{ langField(product, 'name') || product.name }}</h1>
            <p v-if="product.nameEn && product.nameCn" class="text-steel-500 mb-6">{{ product.nameEn }}</p>

            <div class="space-y-4 mb-8">
              <div v-if="product.categoryName" class="flex items-center text-sm">
                <span class="text-steel-500 w-20">{{ t('productDetail.category') }}</span>
                <span class="text-steel-800">{{ product.categoryName }}</span>
              </div>
              <div v-if="langField(product, 'summary')" class="flex items-start text-sm">
                <span class="text-steel-500 w-20 flex-shrink-0">{{ t('productDetail.summary') }}</span>
                <span class="text-steel-600">{{ langField(product, 'summary') }}</span>
              </div>
              <div v-if="product.showPrice && product.price" class="flex items-center">
                <span class="text-steel-500 w-20 text-sm">{{ t('productDetail.price') }}</span>
                <span class="text-xl font-bold text-primary-600">{{ product.price }} {{ product.priceUnit }}</span>
              </div>
              <div v-else class="flex items-center text-sm">
                <span class="text-steel-500 w-20">{{ t('productDetail.price') }}</span>
                <span class="text-steel-400">{{ t('productDetail.contactPrice') }}</span>
              </div>
            </div>

            <!-- Specs Table (inline) -->
            <div v-if="specs.length" class="mb-8">
              <h3 class="font-bold text-steel-800 mb-3">{{ t('productDetail.specs') }}</h3>
              <table class="w-full border border-steel-200 rounded-lg overflow-hidden text-sm">
                <tbody class="divide-y divide-steel-100">
                  <tr v-for="spec in specs" :key="spec.id" class="hover:bg-steel-50">
                    <td class="px-4 py-2.5 text-steel-600 bg-steel-50 w-1/3">{{ langField(spec, 'specName') || spec.specName }}</td>
                    <td class="px-4 py-2.5 text-steel-800">{{ langField(spec, 'specValue') || spec.specValue }}</td>
                  </tr>
                </tbody>
              </table>
            </div>

            <!-- Inquiry Buttons -->
            <div class="flex flex-wrap gap-3 mb-8">
              <button @click="showInquiry = true" class="px-8 py-3 bg-primary-600 text-white font-semibold rounded hover:bg-primary-700 transition-colors">
                {{ t('productDetail.inquireNow') }}
              </button>
              <button @click="handleAddToQuoteCart" class="px-8 py-3 border border-primary-600 text-primary-600 font-semibold rounded hover:bg-primary-50 transition-colors">
                {{ isInQuoteCart ? t('quoteCart.added') : t('quoteCart.addItem') }}
              </button>
              <a v-if="product.phone" :href="`tel:${product.phone}`"
                class="px-8 py-3 border border-primary-600 text-primary-600 font-semibold rounded hover:bg-primary-50 transition-colors">
                {{ t('productDetail.callConsult') }}
              </a>
            </div>

            <!-- Description -->
            <div v-if="langField(product, 'description')" class="border-t border-steel-100 pt-6">
              <h3 class="font-bold text-steel-800 mb-3">{{ t('productDetail.description') }}</h3>
              <div class="text-steel-600 text-sm leading-relaxed whitespace-pre-line">{{ langField(product, 'description') }}</div>
            </div>
          </div>
        </div>

        <!-- Full Specs Table -->
        <div v-if="specs.length > 4" class="mt-12">
          <h2 class="text-xl font-bold text-steel-900 mb-6">{{ t('productDetail.fullSpecs') }}</h2>
          <div class="overflow-x-auto">
            <table class="w-full border border-steel-200 rounded-lg overflow-hidden">
              <thead class="bg-steel-50">
                <tr>
                  <th class="px-4 py-3 text-left text-sm font-semibold text-steel-700">{{ t('productDetail.param') }}</th>
                  <th class="px-4 py-3 text-left text-sm font-semibold text-steel-700">{{ t('productDetail.value') }}</th>
                  <th v-if="specs.some((s: any) => s.specNameEn)" class="px-4 py-3 text-left text-sm font-semibold text-steel-700">{{ t('productDetail.english') }}</th>
                </tr>
              </thead>
              <tbody class="divide-y divide-steel-100">
                <tr v-for="spec in specs" :key="spec.id" class="hover:bg-steel-50">
                  <td class="px-4 py-3 text-sm text-steel-800">{{ langField(spec, 'specName') || spec.specName }}</td>
                  <td class="px-4 py-3 text-sm text-steel-600">{{ langField(spec, 'specValue') || spec.specValue }}</td>
                  <td v-if="specs.some((s: any) => s.specNameEn)" class="px-4 py-3 text-sm text-steel-500">{{ spec.specNameEn || '-' }}</td>
                </tr>
              </tbody>
            </table>
          </div>
        </div>

        <!-- Content -->
        <div v-if="langField(product, 'description')" class="mt-12">
          <h2 class="text-xl font-bold text-steel-900 mb-6">{{ t('productDetail.detailIntro') }}</h2>
          <div class="text-steel-600 text-sm leading-relaxed whitespace-pre-line">{{ langField(product, 'description') }}</div>
        </div>

        <!-- Related Products -->
        <div v-if="relatedProducts.length" class="mt-12">
          <h2 class="text-xl font-bold text-steel-900 mb-6">{{ t('productDetail.relatedProducts') }}</h2>
          <div class="grid grid-cols-2 md:grid-cols-4 gap-6">
            <router-link v-for="rp in relatedProducts" :key="rp.id" :to="`/products/${rp.slug}`"
              class="bg-white rounded-lg border border-steel-100 overflow-hidden hover:shadow-lg transition-shadow group">
              <div class="aspect-square bg-steel-100 overflow-hidden">
                <img v-if="rp.mainImage" :src="rp.mainImage" :alt="rp.nameCn" class="w-full h-full object-cover group-hover:scale-105 transition-transform duration-300" loading="lazy" />
                <div v-else class="w-full h-full flex items-center justify-center text-steel-400">
                  <svg class="w-8 h-8" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="1.5" d="M4 16l4.586-4.586a2 2 0 012.828 0L16 16m-2-2l1.586-1.586a2 2 0 012.828 0L20 14m-6-6h.01M6 20h12a2 2 0 002-2V6a2 2 0 00-2-2H6a2 2 0 00-2 2v12a2 2 0 002 2z"/></svg>
                </div>
              </div>
              <div class="p-3">
                <h3 class="font-medium text-steel-800 text-sm line-clamp-2 group-hover:text-primary-600 transition-colors">{{ langField(rp, 'name') || rp.name }}</h3>
              </div>
            </router-link>
          </div>
        </div>
      </div>

      <!-- Lightbox -->
      <div v-if="showLightbox" class="fixed inset-0 z-50 flex items-center justify-center bg-black/90" @click.self="closeLightbox">
        <button @click="closeLightbox" class="absolute top-4 right-4 w-11 h-11 flex items-center justify-center text-white/70 hover:text-white z-10 rounded-full bg-black/30">
          <svg class="w-7 h-7" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M6 18L18 6M6 6l12 12"/></svg>
        </button>
        <button @click="lightboxPrev" class="absolute left-4 top-1/2 -translate-y-1/2 w-11 h-11 flex items-center justify-center text-white/70 hover:text-white rounded-full bg-black/30">
          <svg class="w-7 h-7" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M15 19l-7-7 7-7"/></svg>
        </button>
        <img :src="lightboxImage" class="max-w-[90vw] max-h-[85vh] object-contain" />
        <button @click="lightboxNext" class="absolute right-4 top-1/2 -translate-y-1/2 w-11 h-11 flex items-center justify-center text-white/70 hover:text-white rounded-full bg-black/30">
          <svg class="w-7 h-7" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 5l7 7-7 7"/></svg>
        </button>
        <div class="absolute bottom-4 text-white/60 text-sm">{{ lightboxIndex + 1 }} / {{ images.length }}</div>
      </div>

      <!-- Inquiry Modal -->
      <div v-if="showInquiry" class="fixed inset-0 z-50 flex items-center justify-center bg-black/50" @click.self="showInquiry = false">
        <div class="bg-white rounded-lg shadow-xl max-w-lg w-full mx-4 max-h-[90vh] overflow-y-auto">
          <div class="p-6">
            <div class="flex items-center justify-between mb-6">
              <h3 class="text-lg font-bold text-steel-900">{{ t('productDetail.productInquiry') }}</h3>
              <button @click="showInquiry = false" class="w-11 h-11 flex items-center justify-center text-steel-400 hover:text-steel-600 -mr-2">
                <svg class="w-6 h-6" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M6 18L18 6M6 6l12 12"/></svg>
              </button>
            </div>

            <div v-if="submitted" class="text-center py-8">
              <svg class="w-16 h-16 text-green-500 mx-auto mb-4" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M5 13l4 4L19 7"/></svg>
              <h4 class="text-lg font-bold text-steel-900 mb-2">{{ t('inquiry.submitted') }}</h4>
              <p class="text-steel-500">{{ t('inquiry.submittedHint') }}</p>
            </div>

            <form v-else @submit.prevent="submitInquiry" class="space-y-4">
              <div class="bg-steel-50 rounded p-3 text-sm text-steel-600">
                {{ t('productDetail.inquiryProduct') }}{{ langField(product, 'name') || product.name }}
              </div>
              <div class="grid grid-cols-1 sm:grid-cols-2 gap-4">
                <div>
                  <label class="block text-sm font-medium text-steel-700 mb-1">{{ t('contact.name') }} *</label>
                  <input v-model="inquiryForm.name" required class="w-full px-3 py-2 border border-steel-200 rounded text-sm focus:outline-none focus:border-primary-500" />
                </div>
                <div>
                  <label class="block text-sm font-medium text-steel-700 mb-1">{{ t('contact.company') }}</label>
                  <input v-model="inquiryForm.company" class="w-full px-3 py-2 border border-steel-200 rounded text-sm focus:outline-none focus:border-primary-500" />
                </div>
              </div>
              <div class="grid grid-cols-1 sm:grid-cols-2 gap-4">
                <div>
                  <label class="block text-sm font-medium text-steel-700 mb-1">{{ t('contact.phoneLabel') }}</label>
                  <input v-model="inquiryForm.phone" class="w-full px-3 py-2 border border-steel-200 rounded text-sm focus:outline-none focus:border-primary-500" />
                </div>
                <div>
                  <label class="block text-sm font-medium text-steel-700 mb-1">{{ t('contact.emailLabel') }}</label>
                  <input v-model="inquiryForm.email" type="email" class="w-full px-3 py-2 border border-steel-200 rounded text-sm focus:outline-none focus:border-primary-500" />
                </div>
              </div>
              <div class="grid grid-cols-1 sm:grid-cols-2 gap-4">
                <div>
                  <label class="block text-sm font-medium text-steel-700 mb-1">{{ t('contact.wechatLabel') }}</label>
                  <input v-model="inquiryForm.wechat" class="w-full px-3 py-2 border border-steel-200 rounded text-sm focus:outline-none focus:border-primary-500" />
                </div>
                <div>
                  <label class="block text-sm font-medium text-steel-700 mb-1">{{ t('contact.whatsappLabel') }}</label>
                  <input v-model="inquiryForm.whatsapp" class="w-full px-3 py-2 border border-steel-200 rounded text-sm focus:outline-none focus:border-primary-500" />
                </div>
              </div>
              <div class="grid grid-cols-1 sm:grid-cols-2 gap-4">
                <div>
                  <label class="block text-sm font-medium text-steel-700 mb-1">{{ t('contact.country') }}</label>
                  <input v-model="inquiryForm.country" class="w-full px-3 py-2 border border-steel-200 rounded text-sm focus:outline-none focus:border-primary-500" />
                </div>
                <div>
                  <label class="block text-sm font-medium text-steel-700 mb-1">{{ t('contact.quantity') }}</label>
                  <input v-model="inquiryForm.quantity" class="w-full px-3 py-2 border border-steel-200 rounded text-sm focus:outline-none focus:border-primary-500" />
                </div>
              </div>
              <div>
                <label class="block text-sm font-medium text-steel-700 mb-1">{{ t('contact.spec') }}</label>
                <input v-model="inquiryForm.specification" class="w-full px-3 py-2 border border-steel-200 rounded text-sm focus:outline-none focus:border-primary-500" />
              </div>
              <div>
                <label class="block text-sm font-medium text-steel-700 mb-1">{{ t('contact.message') }}</label>
                <textarea v-model="inquiryForm.message" rows="3" class="w-full px-3 py-2 border border-steel-200 rounded text-sm focus:outline-none focus:border-primary-500"></textarea>
              </div>
              <button type="submit" :disabled="submitting"
                class="w-full py-3 bg-primary-600 text-white font-semibold rounded hover:bg-primary-700 transition-colors disabled:opacity-60">
                {{ submitting ? t('inquiry.submitting') : t('inquiry.submit') }}
              </button>
            </form>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>
