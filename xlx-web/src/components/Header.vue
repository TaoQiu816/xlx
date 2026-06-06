<template>
  <header class="site-header" :class="{ 'header-scrolled': isScrolled }">
    <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
      <div class="flex items-center justify-between h-16">
        <!-- Logo -->
        <router-link to="/" class="flex items-center gap-3">
          <img src="/logo.png" alt="鑫连鑫丝网厂" class="h-8 w-auto" />
          <div class="hidden sm:block">
            <div class="text-lg font-bold text-primary-700">鑫连鑫丝网厂</div>
          </div>
        </router-link>

        <!-- Desktop Nav -->
        <nav class="hidden md:flex items-center gap-3">
          <router-link v-for="item in navItems" :key="item.path" :to="item.path"
            class="nav-link" :class="{ 'nav-link--active': route.path === item.path }">
            {{ item.label }}
          </router-link>
        </nav>

        <!-- Desktop Contact -->
        <div class="hidden md:flex items-center gap-4">
          <a v-if="config?.phone" :href="`tel:${config.phone}`" class="header-phone">
            <svg class="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M3 5a2 2 0 012-2h3.28a1 1 0 01.948.684l1.498 4.493a1 1 0 01-.502 1.21l-2.257 1.13a11.042 11.042 0 005.516 5.516l1.13-2.257a1 1 0 011.21-.502l4.493 1.498a1 1 0 01.684.949V19a2 2 0 01-2 2h-1C9.716 21 3 14.284 3 6V5z"/>
            </svg>
            {{ config.phone }}
          </a>
          <router-link to="/quote-cart" class="cart-link" :title="t('quoteCart.title')">
            <svg class="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M20 7l-8-4-8 4m16 0l-8 4m8-4v10l-8 4m0-10L4 7m8 4v10M4 7v10l8 4"/>
            </svg>
            <span v-if="cartCount > 0" class="cart-badge">{{ cartCount }}</span>
          </router-link>
          <button class="lang-switch" @click="toggleLang" :title="currentLang === 'zh' ? 'Switch to English' : '切换到中文'">
            {{ currentLang === 'zh' ? 'EN' : '中' }}
          </button>
          <router-link to="/contact" class="cta-button">
            {{ $t('nav.contactUs') }}
          </router-link>
        </div>

        <!-- Mobile menu button -->
        <button class="mobile-menu-btn" @click="mobileOpen = !mobileOpen">
          <svg class="w-6 h-6" fill="none" stroke="currentColor" viewBox="0 0 24 24">
            <path v-if="!mobileOpen" stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
              d="M4 6h16M4 12h16M4 18h16" />
            <path v-else stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
              d="M6 18L18 6M6 6l12 12" />
          </svg>
        </button>
      </div>
    </div>

    <!-- Mobile Nav -->
    <transition name="slide-down">
      <div v-if="mobileOpen" class="mobile-nav">
        <router-link v-for="item in navItems" :key="item.path" :to="item.path"
          class="mobile-nav-link" :class="{ 'mobile-nav-link--active': route.path === item.path }"
          @click="mobileOpen = false">
          {{ item.label }}
        </router-link>
        <div class="flex items-center justify-center mt-2 mb-1">
          <button class="lang-switch" @click="toggleLang" :title="currentLang === 'zh' ? 'Switch to English' : '切换到中文'">
            {{ currentLang === 'zh' ? 'EN' : '中' }}
          </button>
        </div>
        <router-link to="/contact" class="mobile-cta" @click="mobileOpen = false">
          {{ $t('nav.contactUs') }}
        </router-link>
      </div>
    </transition>
  </header>
</template>

<script setup lang="ts">
import { ref, computed, onMounted, onUnmounted } from 'vue'
import { useRoute } from 'vue-router'
import { useI18n } from 'vue-i18n'
import { setLocale, getLocale } from '../i18n'
import { useQuoteCart } from '../composables/useQuoteCart'

defineProps<{
  config?: Record<string, string>
}>()

const route = useRoute()
const { t } = useI18n()
const mobileOpen = ref(false)
const isScrolled = ref(false)
const currentLang = ref(getLocale())
const { count: cartCount } = useQuoteCart()

const navItems = computed(() => [
  { label: t('nav.home'), path: '/' },
  { label: t('nav.products'), path: '/products' },
  { label: t('nav.factory'), path: '/factory' },
  { label: t('nav.certificates'), path: '/certificates' },
  { label: t('nav.about'), path: '/about' },
  { label: t('nav.faq'), path: '/faq' },
  { label: t('nav.contact'), path: '/contact' },
])

function toggleLang() {
  const next = currentLang.value === 'zh' ? 'en' : 'zh'
  setLocale(next)
  currentLang.value = next
}

const handleScroll = () => {
  isScrolled.value = window.scrollY > 10
}

onMounted(() => window.addEventListener('scroll', handleScroll, { passive: true }))
onUnmounted(() => window.removeEventListener('scroll', handleScroll))
</script>

<style scoped>
.site-header {
  background: white;
  position: sticky;
  top: 0;
  z-index: 50;
  transition: box-shadow 0.2s ease;
}

.header-scrolled {
  box-shadow: 0 1px 8px rgba(0, 0, 0, 0.08);
}

/* Desktop nav link */
.nav-link {
  position: relative;
  padding: 0.5rem 0.75rem;
  color: #475569;
  font-size: 0.9rem;
  font-weight: 500;
  transition: color 0.2s;
}

.nav-link:hover {
  color: #1e40af;
}

.nav-link--active {
  color: #1e40af;
}

.nav-link--active::after {
  content: '';
  position: absolute;
  bottom: -2px;
  left: 0;
  right: 0;
  height: 2px;
  background: #1e40af;
  border-radius: 1px;
}

/* Phone */
.header-phone {
  display: flex;
  align-items: center;
  gap: 0.375rem;
  color: #475569;
  font-size: 0.875rem;
  font-weight: 500;
  transition: color 0.2s;
}

.header-phone:hover {
  color: #1e40af;
}

/* CTA button */
.cta-button {
  display: inline-flex;
  align-items: center;
  padding: 0.5rem 1.25rem;
  background: #1e40af;
  color: white;
  font-size: 0.875rem;
  font-weight: 500;
  border-radius: 6px;
  transition: background 0.2s, transform 0.15s;
}

.cta-button:hover {
  background: #1e3a8a;
  transform: translateY(-1px);
}

/* Language switch */
.lang-switch {
  padding: 0.375rem 0.75rem;
  font-size: 0.8rem;
  font-weight: 600;
  color: #475569;
  border: 1px solid #e2e8f0;
  border-radius: 4px;
  background: white;
  cursor: pointer;
  transition: all 0.2s;
  min-width: 44px;
  min-height: 32px;
  display: inline-flex;
  align-items: center;
  justify-content: center;
}

.lang-switch:hover {
  border-color: #1e40af;
  color: #1e40af;
}

/* Cart link */
.cart-link {
  position: relative;
  display: flex;
  align-items: center;
  justify-content: center;
  width: 40px;
  height: 40px;
  color: #475569;
  border-radius: 6px;
  transition: color 0.2s, background 0.2s;
}

.cart-link:hover {
  color: #1e40af;
  background: #f1f5f9;
}

.cart-badge {
  position: absolute;
  top: 2px;
  right: 2px;
  min-width: 18px;
  height: 18px;
  padding: 0 4px;
  font-size: 11px;
  font-weight: 600;
  line-height: 18px;
  text-align: center;
  color: white;
  background: #ef4444;
  border-radius: 9px;
}

/* Mobile */
.mobile-menu-btn {
  padding: 0.625rem;
  min-width: 44px;
  min-height: 44px;
  display: flex;
  align-items: center;
  justify-content: center;
  color: #475569;
  border-radius: 8px;
  transition: background 0.2s;
}

@media (max-width: 768px) {
  .mobile-nav-link {
    padding: 0.875rem 0;
    min-height: 44px;
    display: flex;
    align-items: center;
  }

  .mobile-cta {
    padding: 0.875rem;
    min-height: 44px;
    display: flex;
    align-items: center;
    justify-content: center;
  }
}

.mobile-menu-btn:hover {
  background: #f1f5f9;
}

.mobile-nav {
  background: white;
  border-top: 1px solid #f1f5f9;
  padding: 0.5rem 1rem 1rem;
}

.mobile-nav-link {
  display: block;
  padding: 0.75rem 0;
  color: #475569;
  font-weight: 500;
  font-size: 0.95rem;
  border-bottom: 1px solid #f8fafc;
  transition: color 0.2s;
}

.mobile-nav-link:hover,
.mobile-nav-link--active {
  color: #1e40af;
}

.mobile-cta {
  display: block;
  margin-top: 0.75rem;
  text-align: center;
  padding: 0.75rem;
  background: #1e40af;
  color: white;
  font-weight: 500;
  border-radius: 8px;
}

/* Slide-down transition */
.slide-down-enter-active,
.slide-down-leave-active {
  transition: all 0.25s ease;
}

.slide-down-enter-from,
.slide-down-leave-to {
  opacity: 0;
  transform: translateY(-8px);
}
</style>
