<template>
  <el-container class="min-h-screen">
    <el-aside width="220px" class="bg-gray-800">
      <div class="text-white text-center py-4 font-bold text-lg border-b border-gray-700">
        鑫联信管理后台
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
      <el-header class="bg-white border-b flex items-center justify-between px-6">
        <span class="text-lg font-medium">{{ titleMap[route.name as string] || route.name }}</span>
        <div class="flex items-center gap-4">
          <span class="text-sm text-gray-500">{{ authStore.userInfo?.nickname || authStore.userInfo?.username }}</span>
          <el-button type="danger" size="small" @click="handleLogout">退出登录</el-button>
        </div>
      </el-header>
      <el-main class="bg-gray-50">
        <router-view />
      </el-main>
    </el-container>
  </el-container>
</template>

<script setup lang="ts">
import { onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { useAuthStore } from '../stores/auth'

const route = useRoute()
const router = useRouter()
const authStore = useAuthStore()

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

onMounted(() => {
  authStore.fetchProfile()
})

const handleLogout = () => {
  authStore.logout()
  router.push('/login')
}
</script>
