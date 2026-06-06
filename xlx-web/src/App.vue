<script setup lang="ts">
import { ref, onMounted, onUnmounted } from 'vue'
import Header from './components/Header.vue'
import Footer from './components/Footer.vue'
import ToastContainer from './components/ToastContainer.vue'
import { siteApi } from './api/site'

const config = ref<Record<string, string>>({})
const showBackTop = ref(false)

const handleScroll = () => {
  showBackTop.value = window.scrollY > 300
}

const scrollToTop = () => {
  window.scrollTo({ top: 0, behavior: 'smooth' })
}

onMounted(async () => {
  window.addEventListener('scroll', handleScroll, { passive: true })
  try {
    const res: any = await siteApi.config()
    config.value = res.data || {}
  } catch {
    // config will remain empty
  }
})

onUnmounted(() => {
  window.removeEventListener('scroll', handleScroll)
})
</script>

<template>
  <div class="min-h-screen flex flex-col">
    <Header :config="config" />
    <main class="flex-1">
      <router-view v-slot="{ Component }">
        <transition name="page-fade" mode="out-in">
          <component :is="Component" :config="config" />
        </transition>
      </router-view>
    </main>
    <Footer :config="config" />

    <ToastContainer />

    <!-- 返回顶部 -->
    <transition name="fade">
      <button v-if="showBackTop" class="back-top" @click="scrollToTop" aria-label="返回顶部">
        <svg class="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
          <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M5 15l7-7 7 7"/>
        </svg>
      </button>
    </transition>
  </div>
</template>

<style>
.page-fade-enter-active,
.page-fade-leave-active {
  transition: opacity 0.2s ease;
}

.page-fade-enter-from,
.page-fade-leave-to {
  opacity: 0;
}

/* 返回顶部按钮 */
.back-top {
  position: fixed;
  bottom: 2rem;
  right: 2rem;
  width: 44px;
  height: 44px;
  border-radius: 50%;
  background: #1e40af;
  color: white;
  display: flex;
  align-items: center;
  justify-content: center;
  box-shadow: 0 4px 12px rgba(30, 64, 175, 0.3);
  transition: all 0.2s;
  z-index: 50;
}

.back-top:hover {
  background: #1e3a8a;
  transform: translateY(-2px);
  box-shadow: 0 6px 16px rgba(30, 64, 175, 0.4);
}

.fade-enter-active,
.fade-leave-active {
  transition: opacity 0.2s ease;
}

.fade-enter-from,
.fade-leave-to {
  opacity: 0;
}

@media (prefers-reduced-motion: reduce) {
  *,
  *::before,
  *::after {
    animation-duration: 0.01ms !important;
    transition-duration: 0.01ms !important;
  }
}
</style>
