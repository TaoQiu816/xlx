<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { siteApi } from '../api/site'

const images = ref<any[]>([])
const loading = ref(true)
const lightboxIndex = ref(-1)

const openLightbox = (idx: number) => { lightboxIndex.value = idx }
const closeLightbox = () => { lightboxIndex.value = -1 }
const prevImage = () => { if (lightboxIndex.value > 0) lightboxIndex.value-- }
const nextImage = () => { if (lightboxIndex.value < images.value.length - 1) lightboxIndex.value++ }

onMounted(async () => {
  try {
    const res: any = await siteApi.factoryImages()
    images.value = res.data || []
  } catch { /* ignore */ }
  finally { loading.value = false }
})
</script>

<template>
  <div>
    <!-- Page Header -->
    <section class="bg-primary-900 text-white py-16">
      <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
        <h1 class="text-3xl md:text-4xl font-bold mb-4">工厂展示</h1>
        <p class="text-blue-200 text-lg">走进鑫连鑫，了解我们的生产实力</p>
      </div>
    </section>

    <!-- Images Gallery -->
    <section class="py-16">
      <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
        <div v-if="loading" class="text-center py-20 text-steel-400">加载中...</div>
        <div v-else-if="images.length === 0" class="text-center py-20 text-steel-400">暂无工厂图片</div>
        <div v-else class="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 gap-6">
          <div v-for="(img, idx) in images" :key="img.id"
            class="group cursor-pointer rounded-lg overflow-hidden bg-steel-100"
            @click="openLightbox(idx)">
            <div class="aspect-[4/3] overflow-hidden">
              <img :src="img.imageUrl" :alt="img.title || '工厂图片'" class="w-full h-full object-cover group-hover:scale-105 transition-transform duration-300" loading="lazy" />
            </div>
            <div v-if="img.title || img.description" class="p-4 bg-white">
              <h3 v-if="img.title" class="font-semibold text-steel-800 text-sm">{{ img.title }}</h3>
              <p v-if="img.description" class="text-steel-500 text-xs mt-1">{{ img.description }}</p>
            </div>
          </div>
        </div>
      </div>
    </section>

    <!-- Lightbox -->
    <div v-if="lightboxIndex >= 0" class="fixed inset-0 z-50 bg-black/90 flex items-center justify-center" @click.self="closeLightbox">
      <button @click="closeLightbox" class="absolute top-4 right-4 text-white/80 hover:text-white z-10">
        <svg class="w-8 h-8" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M6 18L18 6M6 6l12 12"/></svg>
      </button>
      <button v-if="lightboxIndex > 0" @click="prevImage" class="absolute left-4 top-1/2 -translate-y-1/2 text-white/80 hover:text-white">
        <svg class="w-10 h-10" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M15 19l-7-7 7-7"/></svg>
      </button>
      <button v-if="lightboxIndex < images.length - 1" @click="nextImage" class="absolute right-4 top-1/2 -translate-y-1/2 text-white/80 hover:text-white">
        <svg class="w-10 h-10" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 5l7 7-7 7"/></svg>
      </button>
      <img :src="images[lightboxIndex]?.imageUrl" :alt="images[lightboxIndex]?.title" class="max-w-[90vw] max-h-[85vh] object-contain" />
      <div v-if="images[lightboxIndex]?.title" class="absolute bottom-8 left-1/2 -translate-x-1/2 text-white text-center">
        <p class="text-lg font-medium">{{ images[lightboxIndex].title }}</p>
      </div>
    </div>
  </div>
</template>
