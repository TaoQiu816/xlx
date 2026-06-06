<template>
  <footer class="bg-primary-900 text-gray-300">
    <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-12">
      <div class="grid grid-cols-1 md:grid-cols-3 gap-8">
        <!-- Company info -->
        <div>
          <div class="flex items-center gap-3 mb-4">
            <div class="footer-logo">
              <img src="/logo.png" alt="Xinlianxin" class="w-full h-full object-contain" />
            </div>
            <h3 class="text-white font-bold text-lg">{{ $t('home.heroTitle') }}</h3>
          </div>
          <p class="text-sm leading-relaxed text-gray-400">
            {{ $t('home.heroSubtitle') }}
          </p>
        </div>

        <!-- Quick links -->
        <div>
          <h3 class="text-white font-bold text-lg mb-4">{{ $t('nav.products') }}</h3>
          <div class="space-y-2">
            <router-link v-for="link in quickLinks" :key="link.path" :to="link.path"
              class="block text-sm text-gray-400 hover:text-white transition-colors">
              {{ link.label }}
            </router-link>
          </div>
        </div>

        <!-- Contact -->
        <div>
          <h3 class="text-white font-bold text-lg mb-4">{{ $t('contact.contactInfo') }}</h3>
          <div class="space-y-3 text-sm">
            <a v-if="config.phone" :href="`tel:${config.phone}`" class="footer-contact-item">
              <svg class="w-4 h-4 flex-shrink-0" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M3 5a2 2 0 012-2h3.28a1 1 0 01.948.684l1.498 4.493a1 1 0 01-.502 1.21l-2.257 1.13a11.042 11.042 0 005.516 5.516l1.13-2.257a1 1 0 011.21-.502l4.493 1.498a1 1 0 01.684.949V19a2 2 0 01-2 2h-1C9.716 21 3 14.284 3 6V5z"/>
              </svg>
              <span>{{ config.phone }}</span>
            </a>
            <a v-if="config.email" :href="`mailto:${config.email}`" class="footer-contact-item">
              <svg class="w-4 h-4 flex-shrink-0" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M3 8l7.89 5.26a2 2 0 002.22 0L21 8M5 19h14a2 2 0 002-2V7a2 2 0 00-2-2H5a2 2 0 00-2 2v10a2 2 0 002 2z"/>
              </svg>
              <span>{{ config.email }}</span>
            </a>
            <p v-if="config.wechat" class="footer-contact-item">
              <svg class="w-4 h-4 flex-shrink-0" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M8 12h.01M12 12h.01M16 12h.01M21 12c0 4.418-4.03 8-9 8a9.863 9.863 0 01-4.255-.949L3 20l1.395-3.72C3.512 15.042 3 13.574 3 12c0-4.418 4.03-8 9-8s9 3.582 9 8z"/>
              </svg>
              <span>{{ $t('contact.wechat') }}：{{ config.wechat }}</span>
            </p>
            <p v-if="config.whatsapp" class="footer-contact-item">
              <svg class="w-4 h-4 flex-shrink-0" fill="currentColor" viewBox="0 0 24 24">
                <path d="M17.472 14.382c-.297-.149-1.758-.867-2.03-.967-.273-.099-.471-.148-.67.15-.197.297-.767.966-.94 1.164-.173.199-.347.223-.644.075-.297-.15-1.255-.463-2.39-1.475-.883-.788-1.48-1.761-1.653-2.059-.173-.297-.018-.458.13-.606.134-.133.298-.347.446-.52.149-.174.198-.298.298-.497.099-.198.05-.371-.025-.52-.075-.149-.669-1.612-.916-2.207-.242-.579-.487-.5-.669-.51-.173-.008-.371-.01-.57-.01-.198 0-.52.074-.792.372-.272.297-1.04 1.016-1.04 2.479 0 1.462 1.065 2.875 1.213 3.074.149.198 2.096 3.2 5.077 4.487.709.306 1.262.489 1.694.625.712.227 1.36.195 1.871.118.571-.085 1.758-.719 2.006-1.413.248-.694.248-1.289.173-1.413-.074-.124-.272-.198-.57-.347m-5.421 7.403h-.004a9.87 9.87 0 01-5.031-1.378l-.361-.214-3.741.982.998-3.648-.235-.374a9.86 9.86 0 01-1.51-5.26c.001-5.45 4.436-9.884 9.888-9.884 2.64 0 5.122 1.03 6.988 2.898a9.825 9.825 0 012.893 6.994c-.003 5.45-4.437 9.884-9.885 9.884m8.413-18.297A11.815 11.815 0 0012.05 0C5.495 0 .16 5.335.157 11.892c0 2.096.547 4.142 1.588 5.945L.057 24l6.305-1.654a11.882 11.882 0 005.683 1.448h.005c6.554 0 11.89-5.335 11.893-11.893a11.821 11.821 0 00-3.48-8.413z"/>
              </svg>
              <span>{{ $t('contact.whatsapp') }}：{{ config.whatsapp }}</span>
            </p>
            <p v-if="config.address" class="footer-contact-item">
              <svg class="w-4 h-4 flex-shrink-0" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M17.657 16.657L13.414 20.9a1.998 1.998 0 01-2.827 0l-4.244-4.243a8 8 0 1111.314 0z"/>
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M15 11a3 3 0 11-6 0 3 3 0 016 0z"/>
              </svg>
              <span>{{ config.address }}</span>
            </p>
          </div>
        </div>
      </div>

      <div class="border-t border-gray-700 mt-8 pt-8 text-center text-sm text-gray-500">
        <p>&copy; {{ new Date().getFullYear() }} {{ $t('home.heroTitle') }} {{ $t('footer.rights') }}.</p>
      </div>
    </div>
  </footer>
</template>

<script setup lang="ts">
import { computed } from 'vue'
import { useI18n } from 'vue-i18n'

defineProps<{
  config: Record<string, string>
}>()

const { t } = useI18n()

const quickLinks = computed(() => [
  { label: t('nav.products'), path: '/products' },
  { label: t('nav.factory'), path: '/factory' },
  { label: t('nav.certificates'), path: '/certificates' },
  { label: t('nav.faq'), path: '/faq' },
  { label: t('nav.contact'), path: '/contact' },
])
</script>

<style scoped>
.footer-logo {
  width: 32px;
  height: 32px;
  flex-shrink: 0;
  overflow: hidden;
  border-radius: 6px;
}

.footer-logo img {
  width: 100%;
  height: 100%;
  object-fit: contain;
}

.footer-contact-item {
  display: flex;
  align-items: center;
  gap: 0.625rem;
  color: #9ca3af;
  transition: color 0.2s;
}

.footer-contact-item:hover {
  color: white;
}
</style>
