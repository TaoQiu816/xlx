import { createRouter, createWebHistory } from 'vue-router'

const router = createRouter({
  history: createWebHistory('/admin/'),
  routes: [
    {
      path: '/login',
      name: 'Login',
      component: () => import('../views/Login.vue'),
    },
    {
      path: '/',
      component: () => import('../views/Layout.vue'),
      redirect: '/dashboard',
      children: [
        {
          path: 'dashboard',
          name: 'Dashboard',
          component: () => import('../views/Dashboard.vue'),
        },
        {
          path: 'categories',
          name: 'Categories',
          component: () => import('../views/Categories.vue'),
        },
        {
          path: 'products',
          name: 'Products',
          component: () => import('../views/Products.vue'),
        },
        {
          path: 'products/create',
          name: 'ProductCreate',
          component: () => import('../views/ProductEdit.vue'),
        },
        {
          path: 'products/:id/edit',
          name: 'ProductEdit',
          component: () => import('../views/ProductEdit.vue'),
        },
        {
          path: 'factory-images',
          name: 'FactoryImages',
          component: () => import('../views/FactoryImages.vue'),
        },
        {
          path: 'certificates',
          name: 'Certificates',
          component: () => import('../views/Certificates.vue'),
        },
        {
          path: 'inquiries',
          name: 'Inquiries',
          component: () => import('../views/Inquiries.vue'),
        },
        {
          path: 'inquiries/:id',
          name: 'InquiryDetail',
          component: () => import('../views/InquiryDetail.vue'),
        },
        {
          path: 'site-config',
          name: 'SiteConfig',
          component: () => import('../views/SiteConfig.vue'),
        },
        {
          path: 'admin-users',
          name: 'AdminUsers',
          component: () => import('../views/AdminUsers.vue'),
        },
      ],
    },
  ],
})

// Navigation guard: redirect to login if no token or token is invalid
router.beforeEach(async (to, _from, next) => {
  const token = localStorage.getItem('admin_token')
  if (to.name === 'Login') {
    // 已登录时访问登录页，跳转到 dashboard
    if (token) {
      try {
        const res = await fetch('/api/admin/auth/profile', {
          headers: { Authorization: `Bearer ${token}` }
        })
        if (res.ok) {
          next({ name: 'Dashboard' })
          return
        }
      } catch {}
      // token 无效，清除后允许访问登录页
      localStorage.removeItem('admin_token')
    }
    next()
    return
  }

  if (!token) {
    next({ name: 'Login' })
    return
  }

  // 验证 token 是否有效
  try {
    const res = await fetch('/api/admin/auth/profile', {
      headers: { Authorization: `Bearer ${token}` }
    })
    if (res.ok) {
      next()
    } else {
      localStorage.removeItem('admin_token')
      next({ name: 'Login' })
    }
  } catch {
    // 网络错误时放行（可能是离线开发）
    next()
  }
})

export default router
