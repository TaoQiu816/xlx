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
      ],
    },
  ],
})

// Navigation guard: redirect to login if no token
router.beforeEach((to, _from, next) => {
  const token = localStorage.getItem('admin_token')
  if (to.name !== 'Login' && !token) {
    next({ name: 'Login' })
  } else {
    next()
  }
})

export default router
