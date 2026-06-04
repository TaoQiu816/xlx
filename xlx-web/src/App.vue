<script setup lang="ts">
import { ref, onMounted } from 'vue'
import Header from './components/Header.vue'
import Footer from './components/Footer.vue'
import { siteApi } from './api/site'

const config = ref<Record<string, string>>({})

onMounted(async () => {
  try {
    const res: any = await siteApi.config()
    config.value = res.data || {}
  } catch {
    // config will remain empty, footer handles missing values gracefully
  }
})
</script>

<template>
  <div class="min-h-screen flex flex-col">
    <Header />
    <main class="flex-1">
      <router-view :config="config" />
    </main>
    <Footer :config="config" />
  </div>
</template>
