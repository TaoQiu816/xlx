import { createRouter, createWebHistory } from 'vue-router'

const router = createRouter({
  history: createWebHistory(),
  routes: [
    {
      path: '/',
      name: 'Home',
      component: () => import('../views/Home.vue'),
    },
    {
      path: '/about',
      name: 'About',
      component: () => import('../views/About.vue'),
    },
    {
      path: '/products',
      name: 'Products',
      component: () => import('../views/Products.vue'),
    },
    {
      path: '/products/:slug',
      name: 'ProductDetail',
      component: () => import('../views/ProductDetail.vue'),
    },
    {
      path: '/factory',
      name: 'Factory',
      component: () => import('../views/Factory.vue'),
    },
    {
      path: '/certificates',
      name: 'Certificates',
      component: () => import('../views/Certificates.vue'),
    },
    {
      path: '/faq',
      name: 'Faq',
      component: () => import('../views/Faq.vue'),
    },
    {
      path: '/contact',
      name: 'Contact',
      component: () => import('../views/Contact.vue'),
    },
  ],
  scrollBehavior(_to, _from, savedPosition) {
    return savedPosition || { top: 0 }
  },
})

export default router
