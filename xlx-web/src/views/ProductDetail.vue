<script setup lang="ts">
import { ref, onMounted, computed } from 'vue'
import { useRoute } from 'vue-router'
import { siteApi } from '../api/site'

const route = useRoute()
const product = ref<any>({})
const loading = ref(true)
const activeImage = ref('')
const showInquiry = ref(false)

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

const setActiveImage = (url: string) => { activeImage.value = url }

const submitInquiry = async () => {
  submitting.value = true
  errorMessage.value = ''
  try {
    await siteApi.submitInquiry({
      ...inquiryForm.value,
      productId: product.value.id,
      productName: product.value.name,
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
    if (images.value.length) activeImage.value = images.value[0]
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
    <div v-if="loading" class="max-w-7xl mx-auto px-4 py-20 text-center text-steel-400">加载中...</div>

    <!-- Not Found -->
    <div v-else-if="!product.id" class="max-w-7xl mx-auto px-4 py-20 text-center">
      <p class="text-steel-500 mb-4">产品不存在或已下架</p>
      <router-link to="/products" class="text-primary-600 hover:text-primary-700">返回产品中心</router-link>
    </div>

    <!-- Product Detail -->
    <div v-else>
      <!-- Breadcrumb -->
      <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-4">
        <nav class="flex items-center text-sm text-steel-400">
          <router-link to="/" class="hover:text-primary-600">首页</router-link>
          <span class="mx-2">/</span>
          <router-link to="/products" class="hover:text-primary-600">产品中心</router-link>
          <span class="mx-2">/</span>
          <span class="text-steel-700">{{ product.name }}</span>
        </nav>
      </div>

      <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 pb-16">
        <div class="grid grid-cols-1 lg:grid-cols-2 gap-12">
          <!-- Images -->
          <div>
            <div class="aspect-square bg-steel-100 rounded-lg overflow-hidden mb-4">
              <img v-if="activeImage" :src="activeImage" :alt="product.name" class="w-full h-full object-contain" />
              <div v-else class="w-full h-full flex items-center justify-center text-steel-400">暂无图片</div>
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
            <h1 class="text-2xl md:text-3xl font-bold text-steel-900 mb-4">{{ product.name }}</h1>
            <p v-if="product.nameEn" class="text-steel-500 mb-6">{{ product.nameEn }}</p>

            <div class="space-y-4 mb-8">
              <div v-if="product.categoryName" class="flex items-center text-sm">
                <span class="text-steel-500 w-20">分类：</span>
                <span class="text-steel-800">{{ product.categoryName }}</span>
              </div>
              <div v-if="product.material" class="flex items-center text-sm">
                <span class="text-steel-500 w-20">材质：</span>
                <span class="text-steel-800">{{ product.material }}</span>
              </div>
              <div v-if="product.spec" class="flex items-center text-sm">
                <span class="text-steel-500 w-20">规格：</span>
                <span class="text-steel-800">{{ product.spec }}</span>
              </div>
              <div v-if="product.showPrice && product.priceRange" class="flex items-center">
                <span class="text-steel-500 w-20 text-sm">价格：</span>
                <span class="text-xl font-bold text-primary-600">{{ product.priceRange }}</span>
              </div>
              <div v-if="product.priceNote" class="text-sm text-steel-500">{{ product.priceNote }}</div>
            </div>

            <!-- Inquiry Buttons -->
            <div class="flex flex-wrap gap-3 mb-8">
              <button @click="showInquiry = true" class="px-8 py-3 bg-primary-600 text-white font-semibold rounded hover:bg-primary-700 transition-colors">
                立即询盘
              </button>
              <a v-if="product.inquiryPhone" :href="`tel:${product.inquiryPhone}`"
                class="px-8 py-3 border border-primary-600 text-primary-600 font-semibold rounded hover:bg-primary-50 transition-colors">
                电话咨询
              </a>
            </div>

            <!-- Description -->
            <div v-if="product.description" class="border-t border-steel-100 pt-6">
              <h3 class="font-bold text-steel-800 mb-3">产品描述</h3>
              <div class="text-steel-600 text-sm leading-relaxed whitespace-pre-line">{{ product.description }}</div>
            </div>
          </div>
        </div>

        <!-- Specs Table -->
        <div v-if="product.specs && product.specs.length" class="mt-12">
          <h2 class="text-xl font-bold text-steel-900 mb-6">产品规格</h2>
          <div class="overflow-x-auto">
            <table class="w-full border border-steel-200 rounded-lg overflow-hidden">
              <thead class="bg-steel-50">
                <tr>
                  <th class="px-4 py-3 text-left text-sm font-semibold text-steel-700">规格名称</th>
                  <th class="px-4 py-3 text-left text-sm font-semibold text-steel-700">参数值</th>
                  <th v-if="product.specs.some((s: any) => s.specNameEn)" class="px-4 py-3 text-left text-sm font-semibold text-steel-700">英文名</th>
                </tr>
              </thead>
              <tbody class="divide-y divide-steel-100">
                <tr v-for="spec in product.specs" :key="spec.id" class="hover:bg-steel-50">
                  <td class="px-4 py-3 text-sm text-steel-800">{{ spec.specName }}</td>
                  <td class="px-4 py-3 text-sm text-steel-600">{{ spec.specValue }}</td>
                  <td v-if="product.specs.some((s: any) => s.specNameEn)" class="px-4 py-3 text-sm text-steel-500">{{ spec.specNameEn || '-' }}</td>
                </tr>
              </tbody>
            </table>
          </div>
        </div>

        <!-- Content -->
        <div v-if="product.content" class="mt-12">
          <h2 class="text-xl font-bold text-steel-900 mb-6">详细介绍</h2>
          <div class="prose prose-sm max-w-none text-steel-600" v-html="product.content"></div>
        </div>
      </div>

      <!-- Inquiry Modal -->
      <div v-if="showInquiry" class="fixed inset-0 z-50 flex items-center justify-center bg-black/50" @click.self="showInquiry = false">
        <div class="bg-white rounded-lg shadow-xl max-w-lg w-full mx-4 max-h-[90vh] overflow-y-auto">
          <div class="p-6">
            <div class="flex items-center justify-between mb-6">
              <h3 class="text-lg font-bold text-steel-900">产品询盘</h3>
              <button @click="showInquiry = false" class="text-steel-400 hover:text-steel-600">
                <svg class="w-6 h-6" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M6 18L18 6M6 6l12 12"/></svg>
              </button>
            </div>

            <div v-if="submitted" class="text-center py-8">
              <svg class="w-16 h-16 text-green-500 mx-auto mb-4" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M5 13l4 4L19 7"/></svg>
              <h4 class="text-lg font-bold text-steel-900 mb-2">询盘已提交</h4>
              <p class="text-steel-500">我们将在24小时内与您联系</p>
            </div>

            <form v-else @submit.prevent="submitInquiry" class="space-y-4">
              <div class="bg-steel-50 rounded p-3 text-sm text-steel-600">
                询盘产品：{{ product.name }}
              </div>
              <div class="grid grid-cols-2 gap-4">
                <div>
                  <label class="block text-sm font-medium text-steel-700 mb-1">姓名 *</label>
                  <input v-model="inquiryForm.name" required class="w-full px-3 py-2 border border-steel-200 rounded text-sm focus:outline-none focus:border-primary-500" />
                </div>
                <div>
                  <label class="block text-sm font-medium text-steel-700 mb-1">公司</label>
                  <input v-model="inquiryForm.company" class="w-full px-3 py-2 border border-steel-200 rounded text-sm focus:outline-none focus:border-primary-500" />
                </div>
              </div>
              <div class="grid grid-cols-2 gap-4">
                <div>
                  <label class="block text-sm font-medium text-steel-700 mb-1">电话</label>
                  <input v-model="inquiryForm.phone" class="w-full px-3 py-2 border border-steel-200 rounded text-sm focus:outline-none focus:border-primary-500" />
                </div>
                <div>
                  <label class="block text-sm font-medium text-steel-700 mb-1">邮箱</label>
                  <input v-model="inquiryForm.email" type="email" class="w-full px-3 py-2 border border-steel-200 rounded text-sm focus:outline-none focus:border-primary-500" />
                </div>
              </div>
              <div class="grid grid-cols-2 gap-4">
                <div>
                  <label class="block text-sm font-medium text-steel-700 mb-1">微信</label>
                  <input v-model="inquiryForm.wechat" class="w-full px-3 py-2 border border-steel-200 rounded text-sm focus:outline-none focus:border-primary-500" />
                </div>
                <div>
                  <label class="block text-sm font-medium text-steel-700 mb-1">WhatsApp</label>
                  <input v-model="inquiryForm.whatsapp" class="w-full px-3 py-2 border border-steel-200 rounded text-sm focus:outline-none focus:border-primary-500" />
                </div>
              </div>
              <div class="grid grid-cols-2 gap-4">
                <div>
                  <label class="block text-sm font-medium text-steel-700 mb-1">国家/地区</label>
                  <input v-model="inquiryForm.country" class="w-full px-3 py-2 border border-steel-200 rounded text-sm focus:outline-none focus:border-primary-500" />
                </div>
                <div>
                  <label class="block text-sm font-medium text-steel-700 mb-1">数量</label>
                  <input v-model="inquiryForm.quantity" class="w-full px-3 py-2 border border-steel-200 rounded text-sm focus:outline-none focus:border-primary-500" />
                </div>
              </div>
              <div>
                <label class="block text-sm font-medium text-steel-700 mb-1">规格要求</label>
                <input v-model="inquiryForm.specification" class="w-full px-3 py-2 border border-steel-200 rounded text-sm focus:outline-none focus:border-primary-500" />
              </div>
              <div>
                <label class="block text-sm font-medium text-steel-700 mb-1">留言</label>
                <textarea v-model="inquiryForm.message" rows="3" class="w-full px-3 py-2 border border-steel-200 rounded text-sm focus:outline-none focus:border-primary-500"></textarea>
              </div>
              <button type="submit" :disabled="submitting"
                class="w-full py-3 bg-primary-600 text-white font-semibold rounded hover:bg-primary-700 transition-colors disabled:opacity-60">
                {{ submitting ? '提交中...' : '提交询盘' }}
              </button>
            </form>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>
