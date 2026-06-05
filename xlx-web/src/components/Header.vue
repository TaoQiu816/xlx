<template>
  <header class="bg-white shadow-sm sticky top-0 z-50">
    <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
      <div class="flex items-center justify-between h-16">
        <!-- Logo -->
        <router-link to="/" class="flex items-center gap-3">
          <img :src="'/logo.png'" alt="鑫连鑫" class="h-10 w-auto" />
          <div class="hidden sm:block">
            <div class="text-lg font-bold text-primary-700">鑫连鑫金属丝网</div>
          </div>
        </router-link>

        <!-- Desktop Nav -->
        <nav class="hidden md:flex items-center gap-8">
          <router-link v-for="item in navItems" :key="item.path" :to="item.path"
            class="text-steel-700 hover:text-primary-600 font-medium text-sm transition-colors"
            active-class="text-primary-600">
            {{ item.label }}
          </router-link>
        </nav>

        <!-- Contact CTA -->
        <router-link to="/contact"
          class="hidden md:inline-flex items-center px-4 py-2 bg-primary-600 text-white text-sm font-medium rounded hover:bg-primary-700 transition-colors">
          立即咨询
        </router-link>

        <!-- Mobile menu button -->
        <button class="md:hidden p-2 text-steel-600" @click="mobileOpen = !mobileOpen">
          <svg class="w-6 h-6" fill="none" stroke="currentColor" viewBox="0 0 24 24">
            <path v-if="!mobileOpen" stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
              d="M4 6h16M4 12h16M4 18h16" />
            <path v-else stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
              d="M6 18L18 6M6 6l12 12" />
          </svg>
        </button>
      </div>

      <!-- Mobile Nav -->
      <div v-if="mobileOpen" class="md:hidden pb-4 border-t border-gray-100">
        <router-link v-for="item in navItems" :key="item.path" :to="item.path"
          class="block py-3 text-steel-700 hover:text-primary-600 font-medium text-sm"
          @click="mobileOpen = false">
          {{ item.label }}
        </router-link>
        <router-link to="/contact"
          class="block mt-2 text-center py-2 bg-primary-600 text-white text-sm font-medium rounded"
          @click="mobileOpen = false">
          立即咨询
        </router-link>
      </div>
    </div>
  </header>
</template>

<script setup lang="ts">
import { ref } from 'vue'

const mobileOpen = ref(false)

const navItems = [
  { label: '首页', path: '/' },
  { label: '产品中心', path: '/products' },
  { label: '工厂展示', path: '/factory' },
  { label: '证书展示', path: '/certificates' },
  { label: '关于我们', path: '/about' },
  { label: '常见问题', path: '/faq' },
  { label: '联系我们', path: '/contact' },
]
</script>
