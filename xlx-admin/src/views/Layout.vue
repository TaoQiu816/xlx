<template>
  <el-container class="min-h-screen">
    <el-aside :width="isMobile ? (sidebarOpen ? '220px' : '0px') : '220px'"
      class="bg-gray-800 transition-all duration-300 overflow-hidden">
      <div class="text-white text-center py-4 font-bold text-lg border-b border-gray-700">
        鑫连鑫管理后台
      </div>
      <el-menu
        :default-active="route.path"
        router
        background-color="#1f2937"
        text-color="#d1d5db"
        active-text-color="#60a5fa"
      >
        <el-menu-item index="/dashboard">
          <span>控制台</span>
        </el-menu-item>
        <el-menu-item index="/products">
          <span>产品管理</span>
        </el-menu-item>
        <el-menu-item index="/categories">
          <span>分类管理</span>
        </el-menu-item>
        <el-menu-item index="/factory-images">
          <span>工厂展示</span>
        </el-menu-item>
        <el-menu-item index="/certificates">
          <span>证书管理</span>
        </el-menu-item>
        <el-menu-item index="/inquiries">
          <span>询盘管理</span>
        </el-menu-item>
        <el-menu-item index="/site-config">
          <span>网站配置</span>
        </el-menu-item>
      </el-menu>
    </el-aside>
    <el-container>
      <el-header class="bg-white border-b flex items-center justify-between px-4 md:px-6">
        <div class="flex items-center gap-3">
          <el-button v-if="isMobile" @click="sidebarOpen = !sidebarOpen" text>
            <el-icon size="20"><Fold v-if="sidebarOpen" /><Expand v-else /></el-icon>
          </el-button>
          <span class="text-lg font-medium">{{ titleMap[route.name as string] || route.name }}</span>
        </div>
        <div class="flex items-center gap-4">
          <span class="text-sm text-gray-500 hidden sm:inline">{{ authStore.userInfo?.nickname || authStore.userInfo?.username }}</span>
          <el-button type="danger" size="small" @click="handleLogout">退出</el-button>
        </div>
      </el-header>
      <el-main class="bg-gray-50 p-4 md:p-6">
        <router-view />
      </el-main>
    </el-container>
  </el-container>
</template>

<script setup lang="ts">
import { ref, onMounted, onUnmounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { useAuthStore } from '../stores/auth'
import { Fold, Expand } from '@element-plus/icons-vue'

const route = useRoute()
const router = useRouter()
const authStore = useAuthStore()

const isMobile = ref(window.innerWidth < 768)
const sidebarOpen = ref(!isMobile.value)

const checkMobile = () => {
  isMobile.value = window.innerWidth < 768
  if (!isMobile.value) sidebarOpen.value = true
}

onMounted(() => {
  authStore.fetchProfile()
  window.addEventListener('resize', checkMobile)
})

onUnmounted(() => {
  window.removeEventListener('resize', checkMobile)
})

const titleMap: Record<string, string> = {
  Dashboard: '控制台',
  Products: '产品管理',
  ProductCreate: '新增产品',
  ProductEdit: '编辑产品',
  Categories: '分类管理',
  FactoryImages: '工厂展示',
  Certificates: '证书管理',
  Inquiries: '询盘管理',
  InquiryDetail: '询盘详情',
  SiteConfig: '网站配置',
}

const handleLogout = () => {
  authStore.logout()
  router.push('/login')
}
</script>
