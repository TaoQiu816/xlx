<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { useI18n } from 'vue-i18n'
import { siteApi } from '../api/site'
import { useSEO } from '../composables/useSEO'
import { langField } from '../composables/useLangField'

const { t } = useI18n()

useSEO({
  title: t('factory.title'),
  description: t('factory.subtitle'),
})

const mediaList = ref<any[]>([])
const loading = ref(true)
const lightboxIndex = ref(-1)

const openLightbox = (idx: number) => { lightboxIndex.value = idx }
const closeLightbox = () => { lightboxIndex.value = -1 }
const prevImage = () => { if (lightboxIndex.value > 0) lightboxIndex.value-- }
const nextImage = () => { if (lightboxIndex.value < mediaList.value.length - 1) lightboxIndex.value++ }

onMounted(async () => {
  try {
    const res: any = await siteApi.factoryImages()
    mediaList.value = res.data || []
  } catch { /* ignore */ }
  finally { loading.value = false }
})
</script>

<template>
  <div>
    <!-- Page Header -->
    <section class="bg-primary-900 text-white py-16">
      <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
        <h1 class="text-3xl md:text-4xl font-bold mb-4">{{ t('factory.title') }}</h1>
        <p class="text-blue-200 text-lg">{{ t('factory.subtitle') }}</p>
      </div>
    </section>

    <!-- Media Gallery -->
    <section class="py-16">
      <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
        <div v-if="loading" class="text-center py-20 text-steel-400">{{ t('factory.loading') }}</div>
        <div v-else-if="mediaList.length === 0" class="text-center py-20 text-steel-400">{{ t('factory.noContent') }}</div>
        <div v-else class="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 gap-6">
          <div v-for="(item, idx) in mediaList" :key="item.id"
            class="group cursor-pointer rounded-lg overflow-hidden bg-steel-100"
            @click="openLightbox(idx)">
            <div class="aspect-[4/3] overflow-hidden">
              <video v-if="item.mediaType === 'video'" :src="item.imageUrl"
                class="w-full h-full object-cover" muted preload="metadata" />
              <img v-else :src="item.imageUrl" :alt="langField(item, 'title') || t('factory.title')"
                class="w-full h-full object-cover group-hover:scale-105 transition-transform duration-300" loading="lazy" />
            </div>
            <div class="p-4 bg-white">
              <div class="flex items-center gap-2">
                <span v-if="item.mediaType === 'video'" class="inline-flex items-center gap-1 text-xs text-orange-600 bg-orange-50 px-2 py-0.5 rounded">
                  <svg class="w-3 h-3" fill="currentColor" viewBox="0 0 24 24"><path d="M8 5v14l11-7z"/></svg>
                  {{ t('factory.video') }}
                </span>
                <h3 v-if="langField(item, 'title')" class="font-semibold text-steel-800 text-sm">{{ langField(item, 'title') }}</h3>
              </div>
              <p v-if="langField(item, 'description')" class="text-steel-500 text-xs mt-1">{{ langField(item, 'description') }}</p>
            </div>
          </div>
        </div>
      </div>
    </section>

    <!-- Lightbox -->
    <div v-if="lightboxIndex >= 0" class="fixed inset-0 z-50 bg-black/90 flex items-center justify-center" @click.self="closeLightbox">
      <button @click="closeLightbox" class="absolute top-4 right-4 w-11 h-11 flex items-center justify-center text-white/80 hover:text-white z-10 rounded-full bg-black/30">
        <svg class="w-7 h-7" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M6 18L18 6M6 6l12 12"/></svg>
      </button>
      <button v-if="lightboxIndex > 0" @click="prevImage" class="absolute left-4 top-1/2 -translate-y-1/2 w-11 h-11 flex items-center justify-center text-white/80 hover:text-white rounded-full bg-black/30">
        <svg class="w-7 h-7" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M15 19l-7-7 7-7"/></svg>
      </button>
      <button v-if="lightboxIndex < mediaList.length - 1" @click="nextImage" class="absolute right-4 top-1/2 -translate-y-1/2 w-11 h-11 flex items-center justify-center text-white/80 hover:text-white rounded-full bg-black/30">
        <svg class="w-7 h-7" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 5l7 7-7 7"/></svg>
      </button>
      <video v-if="mediaList[lightboxIndex]?.mediaType === 'video'"
        :src="mediaList[lightboxIndex]?.imageUrl" controls
        class="max-w-[90vw] max-h-[85vh]" />
      <img v-else :src="mediaList[lightboxIndex]?.imageUrl" :alt="langField(mediaList[lightboxIndex] || {}, 'title')"
        class="max-w-[90vw] max-h-[85vh] object-contain" />
      <div v-if="langField(mediaList[lightboxIndex] || {}, 'title')" class="absolute bottom-8 left-1/2 -translate-x-1/2 text-white text-center">
        <p class="text-lg font-medium">{{ langField(mediaList[lightboxIndex], 'title') }}</p>
      </div>
    </div>
  </div>
</template>
