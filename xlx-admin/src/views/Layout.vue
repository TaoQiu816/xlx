<template>
  <el-container class="min-h-screen">
    <!-- 移动端遮罩 -->
    <div v-if="isMobile && sidebarOpen" class="sidebar-overlay" @click="sidebarOpen = false"></div>

    <el-aside :width="isMobile ? (sidebarOpen ? '220px' : '0px') : '220px'"
      class="sidebar-container" :class="{ 'sidebar-collapsed': isMobile && !sidebarOpen }">
      <!-- 品牌区 -->
      <div class="sidebar-brand">
        <div class="brand-logo">
          <img src="/logo.png" alt="鑫连鑫" class="w-full h-full object-contain" />
        </div>
        <span class="brand-text">鑫连鑫后台</span>
      </div>

      <el-menu
        :default-active="route.path"
        router
        background-color="#1e293b"
        text-color="#94a3b8"
        active-text-color="#ffffff"
        :collapse="isMobile && !sidebarOpen"
        class="sidebar-menu"
      >
        <el-menu-item index="/dashboard">
          <el-icon><Odometer /></el-icon>
          <template #title>控制台</template>
        </el-menu-item>
        <el-menu-item index="/products">
          <el-icon><Goods /></el-icon>
          <template #title>产品管理</template>
        </el-menu-item>
        <el-menu-item index="/categories">
          <el-icon><Grid /></el-icon>
          <template #title>分类管理</template>
        </el-menu-item>
        <el-menu-item index="/factory-images">
          <el-icon><Picture /></el-icon>
          <template #title>工厂展示</template>
        </el-menu-item>
        <el-menu-item index="/certificates">
          <el-icon><Document /></el-icon>
          <template #title>证书管理</template>
        </el-menu-item>
        <el-menu-item index="/inquiries">
          <el-icon><ChatLineSquare /></el-icon>
          <template #title>询盘管理</template>
        </el-menu-item>
        <el-menu-item index="/site-config">
          <el-icon><Setting /></el-icon>
          <template #title>网站配置</template>
        </el-menu-item>
        <el-menu-item index="/admin-users">
          <el-icon><User /></el-icon>
          <template #title>管理员</template>
        </el-menu-item>
      </el-menu>
    </el-aside>

    <el-container>
      <el-header class="header-bar">
        <div class="flex items-center gap-3">
          <el-button v-if="isMobile" @click="sidebarOpen = !sidebarOpen" text>
            <el-icon size="20"><Fold v-if="sidebarOpen" /><Expand v-else /></el-icon>
          </el-button>
          <span class="page-title">{{ titleMap[route.name as string] || route.name }}</span>
        </div>
        <div class="flex items-center gap-4">
          <el-dropdown trigger="click">
            <div class="user-info">
              <div class="user-avatar">
                {{ (authStore.userInfo?.nickname || authStore.userInfo?.username || 'A').charAt(0).toUpperCase() }}
              </div>
              <span class="user-name hidden sm:inline">{{ authStore.userInfo?.nickname || authStore.userInfo?.username }}</span>
              <el-icon class="hidden sm:inline"><ArrowDown /></el-icon>
            </div>
            <template #dropdown>
              <el-dropdown-menu>
                <el-dropdown-item @click="handleLogout">
                  <el-icon><SwitchButton /></el-icon>
                  退出登录
                </el-dropdown-item>
              </el-dropdown-menu>
            </template>
          </el-dropdown>
        </div>
      </el-header>
      <el-main class="main-content">
        <router-view />
      </el-main>
    </el-container>
  </el-container>
</template>

<script setup lang="ts">
import { ref, onMounted, onUnmounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { useAuthStore } from '../stores/auth'
import {
  Fold, Expand, ArrowDown, SwitchButton,
  Odometer, Goods, Grid, Picture, Document, ChatLineSquare, Setting, User
} from '@element-plus/icons-vue'

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
  AdminUsers: '管理员',
}

const handleLogout = () => {
  authStore.logout()
  router.push('/login')
}
</script>

<style scoped>
.sidebar-container {
  background: #1e293b;
  transition: width 0.3s ease;
  overflow: hidden;
}

.sidebar-collapsed {
  width: 0 !important;
}

.sidebar-overlay {
  position: fixed;
  inset: 0;
  background: rgba(0, 0, 0, 0.5);
  z-index: 99;
}

.sidebar-brand {
  display: flex;
  align-items: center;
  gap: 0.75rem;
  padding: 1.25rem 1.25rem;
  border-bottom: 1px solid rgba(255, 255, 255, 0.08);
}

.brand-logo {
  width: 32px;
  height: 32px;
  flex-shrink: 0;
  overflow: hidden;
  border-radius: 6px;
}

.brand-logo img {
  width: 100%;
  height: 100%;
  object-fit: contain;
}

.brand-text {
  color: #f1f5f9;
  font-size: 1rem;
  font-weight: 600;
  white-space: nowrap;
}

.sidebar-menu {
  border-right: none !important;
}

.sidebar-menu .el-menu-item {
  height: 48px;
  line-height: 48px;
  margin: 2px 8px;
  border-radius: 8px;
  transition: all 0.2s;
}

.sidebar-menu .el-menu-item:hover {
  background: rgba(255, 255, 255, 0.06) !important;
}

.sidebar-menu .el-menu-item.is-active {
  background: rgba(96, 165, 250, 0.15) !important;
  color: #ffffff !important;
}

.sidebar-menu .el-menu-item.is-active::before {
  content: '';
  position: absolute;
  left: 0;
  top: 50%;
  transform: translateY(-50%);
  width: 3px;
  height: 20px;
  background: #60a5fa;
  border-radius: 0 3px 3px 0;
}

.header-bar {
  background: #ffffff;
  border-bottom: 1px solid #e2e8f0;
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 0 1.5rem;
  height: 60px;
}

.page-title {
  font-size: 1.1rem;
  font-weight: 600;
  color: #1e293b;
}

.user-info {
  display: flex;
  align-items: center;
  gap: 0.5rem;
  cursor: pointer;
  padding: 0.375rem 0.75rem;
  border-radius: 8px;
  transition: background 0.2s;
}

.user-info:hover {
  background: #f1f5f9;
}

.user-avatar {
  width: 32px;
  height: 32px;
  border-radius: 50%;
  background: linear-gradient(135deg, #3b82f6, #1d4ed8);
  color: white;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 0.875rem;
  font-weight: 600;
}

.user-name {
  font-size: 0.875rem;
  color: #475569;
}

.main-content {
  background: #f1f5f9;
  padding: 1.5rem;
}
</style>
