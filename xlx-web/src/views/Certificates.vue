<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { siteApi } from '../api/site'

const certificates = ref<any[]>([])
const loading = ref(true)
const lightboxUrl = ref('')

const openLightbox = (url: string) => { lightboxUrl.value = url }
const closeLightbox = () => { lightboxUrl.value = '' }

const typeLabel = (type: string) => {
  const map: Record<string, string> = { certificate: '资质证书', report: '检测报告', quality: '质量认证' }
  return map[type] || type
}

onMounted(async () => {
  try {
    const res: any = await siteApi.certificates()
    certificates.value = res.data || []
  } catch { /* ignore */ }
  finally { loading.value = false }
})
</script>

<template>
  <div>
    <!-- Page Header -->
    <section class="bg-primary-900 text-white py-16">
      <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
        <h1 class="text-3xl md:text-4xl font-bold mb-4">证书展示</h1>
        <p class="text-blue-200 text-lg">资质齐全，品质有保障</p>
      </div>
    </section>

    <!-- Certificates Grid -->
    <section class="py-16">
      <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
        <div v-if="loading" class="text-center py-20 text-steel-400">加载中...</div>
        <div v-else-if="certificates.length === 0" class="text-center py-20 text-steel-400">暂无证书</div>
        <div v-else class="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 gap-8">
          <div v-for="cert in certificates" :key="cert.id" class="bg-white rounded-lg border border-steel-100 overflow-hidden hover:shadow-lg transition-shadow">
            <div class="aspect-[3/4] bg-steel-50 overflow-hidden cursor-pointer" @click="cert.imageUrl && openLightbox(cert.imageUrl)">
              <img v-if="cert.imageUrl" :src="cert.imageUrl" :alt="cert.name" class="w-full h-full object-contain hover:scale-105 transition-transform duration-300" loading="lazy" />
              <div v-else class="w-full h-full flex items-center justify-center text-steel-400">暂无图片</div>
            </div>
            <div class="p-4">
              <h3 class="font-semibold text-steel-800 mb-1">{{ cert.name }}</h3>
              <div class="flex items-center gap-3 text-sm text-steel-500">
                <span v-if="cert.type" class="inline-flex items-center px-2 py-0.5 bg-primary-50 text-primary-700 rounded text-xs">{{ typeLabel(cert.type) }}</span>
                <span v-if="cert.issueDate">{{ cert.issueDate }}</span>
              </div>
              <p v-if="cert.description" class="text-sm text-steel-500 mt-2">{{ cert.description }}</p>
            </div>
          </div>
        </div>
      </div>
    </section>

    <!-- Lightbox -->
    <div v-if="lightboxUrl" class="fixed inset-0 z-50 bg-black/90 flex items-center justify-center" @click.self="closeLightbox">
      <button @click="closeLightbox" class="absolute top-4 right-4 text-white/80 hover:text-white">
        <svg class="w-8 h-8" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M6 18L18 6M6 6l12 12"/></svg>
      </button>
      <img :src="lightboxUrl" class="max-w-[90vw] max-h-[90vh] object-contain" />
    </div>
  </div>
</template>
