<template>
  <div class="login-container">
    <!-- 左侧品牌展示区 -->
    <div class="login-brand">
      <div class="brand-content">
        <div class="brand-icon">
          <img src="/logo.png" alt="鑫连鑫" class="w-full h-full object-contain" />
        </div>
        <h1 class="brand-title">鑫连鑫丝网厂</h1>
        <p class="brand-subtitle">后台管理系统</p>
        <div class="brand-features">
          <div class="feature-item">
            <span class="feature-dot"></span>
            <span>产品管理与展示</span>
          </div>
          <div class="feature-item">
            <span class="feature-dot"></span>
            <span>询盘跟踪与处理</span>
          </div>
          <div class="feature-item">
            <span class="feature-dot"></span>
            <span>网站内容配置</span>
          </div>
        </div>
      </div>
    </div>

    <!-- 右侧登录表单 -->
    <div class="login-form-wrapper">
      <div class="login-form-card">
        <h2 class="form-title">欢迎回来</h2>
        <p class="form-desc">请登录管理员账号</p>
        <el-form ref="formRef" :model="form" :rules="rules" @keyup.enter="handleLogin" class="login-form">
          <el-form-item prop="username">
            <el-input v-model="form.username" placeholder="用户名" size="large" prefix-icon="User" />
          </el-form-item>
          <el-form-item prop="password">
            <el-input v-model="form.password" type="password" placeholder="密码" size="large" prefix-icon="Lock" show-password />
          </el-form-item>
          <div class="form-options">
            <el-checkbox v-model="rememberMe">记住我</el-checkbox>
          </div>
          <el-button type="primary" size="large" class="login-btn" :loading="loading" @click="handleLogin">
            登 录
          </el-button>
        </el-form>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage, type FormInstance } from 'element-plus'
import { useAuthStore } from '../stores/auth'

const router = useRouter()
const authStore = useAuthStore()
const formRef = ref<FormInstance>()
const loading = ref(false)
const rememberMe = ref(false)

const form = reactive({
  username: '',
  password: '',
})

const rules = {
  username: [{ required: true, message: '请输入用户名', trigger: 'blur' }],
  password: [{ required: true, message: '请输入密码', trigger: 'blur' }],
}

const handleLogin = async () => {
  const valid = await formRef.value?.validate().catch(() => false)
  if (!valid) return

  loading.value = true
  try {
    await authStore.login(form.username, form.password)
    ElMessage.success('登录成功')
    router.push('/dashboard')
  } catch (e: any) {
    ElMessage.error(e.message || '登录失败')
  } finally {
    loading.value = false
  }
}
</script>

<style scoped>
.login-container {
  min-height: 100vh;
  display: flex;
}

.login-brand {
  flex: 1;
  background: linear-gradient(135deg, #1e3a5f 0%, #0f172a 100%);
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 3rem;
  position: relative;
  overflow: hidden;
}

.login-brand::before {
  content: '';
  position: absolute;
  inset: 0;
  background-image:
    repeating-linear-gradient(0deg, transparent, transparent 40px, rgba(255,255,255,0.03) 40px, rgba(255,255,255,0.03) 41px),
    repeating-linear-gradient(90deg, transparent, transparent 40px, rgba(255,255,255,0.03) 40px, rgba(255,255,255,0.03) 41px);
}

.brand-content {
  position: relative;
  z-index: 1;
  text-align: center;
  color: white;
}

.brand-icon {
  width: 100px;
  height: 100px;
  margin: 0 auto 2rem;
  overflow: hidden;
  border-radius: 12px;
}

.brand-icon img {
  width: 100%;
  height: 100%;
  object-fit: contain;
}

.brand-title {
  font-size: 2rem;
  font-weight: 700;
  margin-bottom: 0.5rem;
  letter-spacing: 0.05em;
}

.brand-subtitle {
  font-size: 1.1rem;
  color: rgba(255, 255, 255, 0.7);
  margin-bottom: 3rem;
}

.brand-features {
  text-align: left;
  display: inline-block;
}

.feature-item {
  display: flex;
  align-items: center;
  gap: 0.75rem;
  padding: 0.5rem 0;
  color: rgba(255, 255, 255, 0.8);
  font-size: 0.95rem;
}

.feature-dot {
  width: 6px;
  height: 6px;
  border-radius: 50%;
  background: #60a5fa;
  flex-shrink: 0;
}

.login-form-wrapper {
  width: 480px;
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 3rem;
  background: #f8fafc;
}

.login-form-card {
  width: 100%;
  max-width: 360px;
}

.form-title {
  font-size: 1.5rem;
  font-weight: 700;
  color: #1e293b;
  margin-bottom: 0.5rem;
}

.form-desc {
  color: #64748b;
  margin-bottom: 2rem;
}

.login-form {
  width: 100%;
}

.form-options {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 1.5rem;
}

.login-btn {
  width: 100%;
  height: 44px;
  font-size: 1rem;
  letter-spacing: 0.1em;
}

@media (max-width: 768px) {
  .login-container {
    flex-direction: column;
  }

  .login-brand {
    padding: 2rem;
    min-height: auto;
  }

  .brand-icon {
    width: 60px;
    height: 60px;
    margin-bottom: 1rem;
  }

  .brand-title {
    font-size: 1.5rem;
  }

  .brand-features {
    display: none;
  }

  .login-form-wrapper {
    width: 100%;
    padding: 2rem;
  }
}
</style>
