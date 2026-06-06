<script setup lang="ts">
import { useI18n } from 'vue-i18n'
import { useSEO } from '../composables/useSEO'

const { t } = useI18n()

useSEO({
  title: t('about.title'),
  description: t('about.subtitle'),
})
</script>

<template>
  <div>
    <!-- Page Header -->
    <section class="bg-primary-900 text-white py-16">
      <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
        <h1 class="text-3xl md:text-4xl font-bold mb-4">{{ t('about.title') }}</h1>
        <p class="text-blue-200 text-lg">{{ t('about.subtitle') }}</p>
      </div>
    </section>

    <!-- Company Intro -->
    <section class="py-16 bg-white">
      <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
        <div class="grid grid-cols-1 lg:grid-cols-2 gap-12 items-center">
          <div>
            <h2 class="text-2xl font-bold text-steel-900 mb-6">{{ t('about.title') }}</h2>
            <div class="space-y-4 text-steel-600 leading-relaxed">
              <p>
                鑫连鑫丝网厂位于中国丝网之都——江苏省南通市，是一家专业生产金属丝和丝网产品的企业。公司拥有多年的生产经验，技术力量雄厚，检测设备齐全。
              </p>
              <p>
                我们主要产品涵盖金属丝类（镀锌铁丝、不锈钢丝、退火丝、PVC涂塑丝等）和丝网类（电焊网片、荷兰网、护栏网、窗纱、钢板网、钢格板等），广泛应用于建筑、农业、工业、交通等领域。
              </p>
              <p>
                公司始终坚持"质量第一、客户至上"的经营理念，产品远销东南亚、中东、非洲、欧洲等多个国家和地区，赢得了国内外客户的信赖与好评。
              </p>
            </div>
          </div>
          <div class="aspect-[4/3] bg-steel-100 rounded-lg overflow-hidden">
            <img :src="'/uploads/about/company.jpg'" alt="公司照片" class="w-full h-full object-cover" loading="lazy" onerror="this.style.display='none'" />
          </div>
        </div>
      </div>
    </section>

    <!-- Key Numbers -->
    <section class="py-16 bg-steel-50">
      <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
        <div class="grid grid-cols-2 md:grid-cols-4 gap-8 text-center">
          <div>
            <div class="text-4xl font-bold text-primary-600 mb-2">10+</div>
            <div class="text-steel-600">{{ t('about.yearsExp') }}</div>
          </div>
          <div>
            <div class="text-4xl font-bold text-primary-600 mb-2">50+</div>
            <div class="text-steel-600">{{ t('about.productTypes') }}</div>
          </div>
          <div>
            <div class="text-4xl font-bold text-primary-600 mb-2">30+</div>
            <div class="text-steel-600">{{ t('about.exportCountries') }}</div>
          </div>
          <div>
            <div class="text-4xl font-bold text-primary-600 mb-2">1000+</div>
            <div class="text-steel-600">{{ t('about.servedClients') }}</div>
          </div>
        </div>
      </div>
    </section>

    <!-- Our Advantages -->
    <section class="py-16 bg-white">
      <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
        <h2 class="text-2xl font-bold text-steel-900 mb-12 text-center">{{ t('about.ourAdvantages') }}</h2>
        <div class="grid grid-cols-1 md:grid-cols-3 gap-8">
          <div class="p-6 border border-steel-100 rounded-lg">
            <div class="w-12 h-12 bg-primary-100 rounded-lg flex items-center justify-center mb-4">
              <svg class="w-6 h-6 text-primary-600" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M19.428 15.428a2 2 0 00-1.022-.547l-2.387-.477a6 6 0 00-3.86.517l-.318.158a6 6 0 01-3.86.517L6.05 15.21a2 2 0 00-1.806.547M8 4h8l-1 1v5.172a2 2 0 00.586 1.414l5 5c1.26 1.26.367 3.414-1.415 3.414H4.828c-1.782 0-2.674-2.154-1.414-3.414l5-5A2 2 0 009 10.172V5L8 4z"/></svg>
            </div>
            <h3 class="font-bold text-steel-800 mb-3">{{ t('about.sourceFactory') }}</h3>
            <p class="text-steel-600 text-sm leading-relaxed">{{ t('about.sourceFactoryDesc') }}</p>
          </div>
          <div class="p-6 border border-steel-100 rounded-lg">
            <div class="w-12 h-12 bg-primary-100 rounded-lg flex items-center justify-center mb-4">
              <svg class="w-6 h-6 text-primary-600" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 12l2 2 4-4m5.618-4.016A11.955 11.955 0 0112 2.944a11.955 11.955 0 01-8.618 3.04A12.02 12.02 0 003 9c0 5.591 3.824 10.29 9 11.622 5.176-1.332 9-6.03 9-11.622 0-1.042-.133-2.052-.382-3.016z"/></svg>
            </div>
            <h3 class="font-bold text-steel-800 mb-3">{{ t('about.qualityControl') }}</h3>
            <p class="text-steel-600 text-sm leading-relaxed">{{ t('about.qualityControlDesc') }}</p>
          </div>
          <div class="p-6 border border-steel-100 rounded-lg">
            <div class="w-12 h-12 bg-primary-100 rounded-lg flex items-center justify-center mb-4">
              <svg class="w-6 h-6 text-primary-600" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M3.055 11H5a2 2 0 012 2v1a2 2 0 002 2 2 2 0 012 2v2.945M8 3.935V5.5A2.5 2.5 0 0010.5 8h.5a2 2 0 012 2 2 2 0 104 0 2 2 0 012-2h1.064M15 20.488V18a2 2 0 012-2h3.064M21 12a9 9 0 11-18 0 9 9 0 0118 0z"/></svg>
            </div>
            <h3 class="font-bold text-steel-800 mb-3">{{ t('about.exportService') }}</h3>
            <p class="text-steel-600 text-sm leading-relaxed">{{ t('about.exportServiceDesc') }}</p>
          </div>
        </div>
      </div>
    </section>

    <!-- Contact CTA -->
    <section class="py-12 bg-primary-800 text-white text-center">
      <div class="max-w-4xl mx-auto px-4 sm:px-6 lg:px-8">
        <h2 class="text-2xl font-bold mb-4">{{ t('about.ctaTitle') }}</h2>
        <p class="text-blue-200 mb-6">{{ t('about.ctaSubtitle') }}</p>
        <router-link to="/contact" class="inline-flex items-center px-8 py-3 bg-white text-primary-700 font-semibold rounded hover:bg-blue-50 transition-colors">
          {{ t('nav.contact') }}
        </router-link>
      </div>
    </section>
  </div>
</template>
