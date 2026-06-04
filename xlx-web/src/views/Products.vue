<script setup lang="ts">
import { ref, onMounted, watch, computed } from 'vue'
import { useRoute } from 'vue-router'
import { siteApi } from '../api/site'

const route = useRoute()
const categories = ref<any[]>([])
const products = ref<any[]>([])
const total = ref(0)
const page = ref(1)
const pageSize = 12
const selectedCategory = ref<number | null>(null)
const loading = ref(false)

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
        <h1 class="text-3xl md:text-4xl font-bold mb-4">产品中心</h1>
        <p class="text-blue-200 text-lg">浏览我们的金属丝和丝网产品</p>
      </div>
    </section>

    <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-12">
      <div class="flex flex-col lg:flex-row gap-8">
        <!-- Sidebar: Categories -->
        <aside class="lg:w-64 flex-shrink-0">
          <div class="bg-white rounded-lg border border-steel-100 p-4 sticky top-20">
            <h3 class="font-bold text-steel-800 mb-4">产品分类</h3>
            <div class="space-y-1">
              <button @click="selectCategory(null)"
                class="w-full text-left px-3 py-2 rounded text-sm transition-colors"
                :class="selectedCategory === null ? 'bg-primary-50 text-primary-700 font-medium' : 'text-steel-600 hover:bg-steel-50'">
                全部产品
              </button>
              <button v-for="cat in categories" :key="cat.id" @click="selectCategory(cat.id)"
                class="w-full text-left px-3 py-2 rounded text-sm transition-colors"
                :class="selectedCategory === cat.id ? 'bg-primary-50 text-primary-700 font-medium' : 'text-steel-600 hover:bg-steel-50'">
                {{ cat.name }}
              </button>
            </div>
          </div>
        </aside>

        <!-- Product Grid -->
        <div class="flex-1">
          <div v-if="loading" class="text-center py-20 text-steel-400">加载中...</div>
          <div v-else-if="products.length === 0" class="text-center py-20 text-steel-400">暂无产品</div>
          <div v-else>
            <div class="grid grid-cols-2 md:grid-cols-3 gap-6">
              <router-link v-for="p in products" :key="p.id" :to="`/products/${p.slug}`"
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
                  <p v-if="p.showPrice && p.priceRange" class="text-sm text-primary-600 font-semibold mt-2">{{ p.priceRange }}</p>
                </div>
              </router-link>
            </div>

            <!-- Pagination -->
            <div v-if="totalPages > 1" class="flex justify-center items-center gap-2 mt-10">
              <button @click="goToPage(page - 1)" :disabled="page <= 1"
                class="px-3 py-2 rounded border border-steel-200 text-sm text-steel-600 hover:bg-steel-50 disabled:opacity-40 disabled:cursor-not-allowed">
                上一页
              </button>
              <template v-for="p in totalPages" :key="p">
                <button v-if="p <= 3 || p > totalPages - 2 || Math.abs(p - page) <= 1" @click="goToPage(p)"
                  class="w-10 h-10 rounded text-sm font-medium transition-colors"
                  :class="p === page ? 'bg-primary-600 text-white' : 'border border-steel-200 text-steel-600 hover:bg-steel-50'">
                  {{ p }}
                </button>
                <span v-else-if="p === 4 || p === totalPages - 2" class="text-steel-400">...</span>
              </template>
              <button @click="goToPage(page + 1)" :disabled="page >= totalPages"
                class="px-3 py-2 rounded border border-steel-200 text-sm text-steel-600 hover:bg-steel-50 disabled:opacity-40 disabled:cursor-not-allowed">
                下一页
              </button>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>
