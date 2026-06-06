<script setup lang="ts">
import { ref, onMounted, watch, computed } from 'vue'
import { useRoute } from 'vue-router'
import { useI18n } from 'vue-i18n'
import { siteApi } from '../api/site'
import SkeletonCard from '../components/SkeletonCard.vue'
import { useSEO } from '../composables/useSEO'
import { useBreadcrumbSchema } from '../composables/useJsonLd'
import { langField } from '../composables/useLangField'

const { t } = useI18n()

useSEO({
  title: t('products.title'),
  description: t('products.subtitle'),
})

useBreadcrumbSchema([
  { name: '首页', url: '/' },
  { name: '产品中心', url: '/products' },
])

const route = useRoute()
const categories = ref<any[]>([])
const products = ref<any[]>([])
const total = ref(0)
const page = ref(1)
const pageSize = 12
const selectedCategory = ref<number | null>(null)
const loading = ref(true)
const keyword = ref('')
const sortBy = ref('')

const totalPages = computed(() => Math.ceil(total.value / pageSize))

const loadCategories = async () => {
  try {
    const res: any = await siteApi.categories()
    categories.value = res.data || []
  } catch { /* ignore */ }
}

const loadProducts = async () => {
  loading.value = true
  try {
    const params: any = { page: page.value, size: pageSize }
    if (selectedCategory.value) params.categoryId = selectedCategory.value
    if (keyword.value) params.keyword = keyword.value
    if (sortBy.value) params.sort = sortBy.value
    const res: any = await siteApi.products(params)
    products.value = res.data?.records || res.data || []
    total.value = res.data?.total || 0
  } catch { /* ignore */ }
  finally { loading.value = false }
}

const selectCategory = (id: number | null) => {
  selectedCategory.value = id
  page.value = 1
  loadProducts()
}

const handleSearch = () => {
  page.value = 1
  loadProducts()
}

const goToPage = (p: number) => {
  if (p < 1 || p > totalPages.value) return
  page.value = p
  loadProducts()
}

watch(() => route.query.category, (val) => {
  if (val) selectCategory(Number(val))
})

onMounted(() => {
  loadCategories()
  if (route.query.category) selectedCategory.value = Number(route.query.category)
  loadProducts()
})
</script>

<template>
  <div>
    <!-- Page Header -->
    <section class="bg-primary-900 text-white py-16">
      <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
        <h1 class="text-3xl md:text-4xl font-bold mb-4">{{ t('products.title') }}</h1>
        <p class="text-blue-200 text-lg">{{ t('products.subtitle') }}</p>
      </div>
    </section>

    <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-12">
      <div class="flex flex-col lg:flex-row gap-8">
        <!-- Sidebar: Categories -->
        <aside class="lg:w-64 flex-shrink-0">
          <div class="bg-white rounded-lg border border-steel-100 p-4 sticky top-20">
            <h3 class="font-bold text-steel-800 mb-4">{{ t('products.title') }}</h3>
            <div class="space-y-1">
              <button @click="selectCategory(null)"
                class="w-full text-left px-3 py-2 rounded text-sm transition-colors"
                :class="selectedCategory === null ? 'bg-primary-50 text-primary-700 font-medium' : 'text-steel-600 hover:bg-steel-50'">
                {{ t('products.allProducts') }}
              </button>
              <button v-for="cat in categories" :key="cat.id" @click="selectCategory(cat.id)"
                class="w-full text-left px-3 py-2 rounded text-sm transition-colors"
                :class="selectedCategory === cat.id ? 'bg-primary-50 text-primary-700 font-medium' : 'text-steel-600 hover:bg-steel-50'">
                {{ langField(cat, 'name') }}
              </button>
            </div>
          </div>
        </aside>

        <!-- Product Grid -->
        <div class="flex-1">
          <!-- Search & Sort -->
          <div class="flex flex-col sm:flex-row gap-3 mb-6">
            <div class="relative flex-1">
              <input v-model="keyword" @keyup.enter="handleSearch"
                :placeholder="t('products.search')"
                class="w-full pl-10 pr-4 py-2.5 border border-steel-200 rounded-lg text-sm focus:outline-none focus:border-primary-500 focus:ring-1 focus:ring-primary-500" />
              <svg class="absolute left-3 top-1/2 -translate-y-1/2 w-4 h-4 text-steel-400" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M21 21l-6-6m2-5a7 7 0 11-14 0 7 7 0 0114 0z"/></svg>
            </div>
            <select v-model="sortBy" @change="handleSearch"
              class="px-4 py-2.5 border border-steel-200 rounded-lg text-sm text-steel-600 focus:outline-none focus:border-primary-500 bg-white">
              <option value="">{{ t('products.sortByDefault') }}</option>
              <option value="latest">{{ t('products.sortByLatest') }}</option>
              <option value="name_asc">{{ t('products.sortByName') }}</option>
            </select>
          </div>

          <!-- Loading Skeleton -->
          <div v-if="loading" class="grid grid-cols-2 md:grid-cols-3 gap-6">
            <SkeletonCard v-for="n in 6" :key="'sk-'+n" :image="true" :lines="2" />
          </div>

          <!-- Empty -->
          <div v-else-if="products.length === 0" class="text-center py-20">
            <svg class="w-16 h-16 text-steel-300 mx-auto mb-4" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="1.5" d="M20 7l-8-4-8 4m16 0l-8 4m8-4v10l-8 4m0-10L4 7m8 4v10M4 7v10l8 4"/></svg>
            <p class="text-steel-500 mb-2">{{ t('products.noResults') }}</p>
            <p class="text-steel-400 text-sm">{{ t('products.noResultsHint') }}</p>
          </div>

          <!-- Products -->
          <div v-else class="grid grid-cols-2 md:grid-cols-3 gap-6">
            <router-link v-for="p in products" :key="p.id" :to="`/products/${p.slug}`"
              class="bg-white rounded-lg border border-steel-100 overflow-hidden hover:shadow-lg transition-shadow group">
              <div class="aspect-square bg-steel-100 overflow-hidden">
                <img v-if="p.mainImage" :src="p.mainImage" :alt="p.nameCn" class="w-full h-full object-cover group-hover:scale-105 transition-transform duration-300" loading="lazy" />
                <div v-else class="w-full h-full flex items-center justify-center text-steel-400">
                  <svg class="w-12 h-12" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="1.5" d="M4 16l4.586-4.586a2 2 0 012.828 0L16 16m-2-2l1.586-1.586a2 2 0 012.828 0L20 14m-6-6h.01M6 20h12a2 2 0 002-2V6a2 2 0 00-2-2H6a2 2 0 00-2 2v12a2 2 0 002 2z"/></svg>
                </div>
              </div>
              <div class="p-4">
                <h3 class="font-semibold text-steel-800 text-sm mb-1 line-clamp-2 group-hover:text-primary-600 transition-colors">{{ langField(p, 'name') || p.name }}</h3>
                <p v-if="p.categoryName" class="text-xs text-steel-400">{{ p.categoryName }}</p>
                <p v-if="p.showPrice && p.price" class="text-sm text-primary-600 font-semibold mt-2">{{ p.price }} {{ p.priceUnit }}</p>
                <p v-else class="text-xs text-steel-400 mt-2">{{ t('products.contactForPrice') }}</p>
              </div>
            </router-link>
          </div>

          <!-- Pagination -->
          <div v-if="totalPages > 1" class="flex justify-center items-center gap-2 mt-10">
            <button @click="goToPage(page - 1)" :disabled="page <= 1"
              class="px-4 py-2.5 min-h-[44px] rounded border border-steel-200 text-sm text-steel-600 hover:bg-steel-50 disabled:opacity-40 disabled:cursor-not-allowed">
              {{ t('products.prev') }}
            </button>
            <template v-for="p in totalPages" :key="p">
              <button v-if="p <= 3 || p > totalPages - 2 || Math.abs(p - page) <= 1" @click="goToPage(p)"
                class="w-11 h-11 rounded text-sm font-medium transition-colors"
                :class="p === page ? 'bg-primary-600 text-white' : 'border border-steel-200 text-steel-600 hover:bg-steel-50'">
                {{ p }}
              </button>
              <span v-else-if="p === 4 || p === totalPages - 2" class="text-steel-400">...</span>
            </template>
            <button @click="goToPage(page + 1)" :disabled="page >= totalPages"
              class="px-4 py-2.5 min-h-[44px] rounded border border-steel-200 text-sm text-steel-600 hover:bg-steel-50 disabled:opacity-40 disabled:cursor-not-allowed">
              {{ t('products.next') }}
            </button>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>
