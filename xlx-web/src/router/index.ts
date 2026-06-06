import { createRouter, createWebHistory } from 'vue-router'

const router = createRouter({
  history: createWebHistory(),
  routes: [
    {
      path: '/',
      name: 'Home',
      component: () => import('../views/Home.vue'),
      meta: { title: '鑫连鑫丝网厂' },
    },
    {
      path: '/about',
      name: 'About',
      component: () => import('../views/About.vue'),
      meta: { title: '关于我们' },
    },
    {
      path: '/products',
      name: 'Products',
      component: () => import('../views/Products.vue'),
      meta: { title: '产品中心' },
    },
    {
      path: '/products/:slug',
      name: 'ProductDetail',
      component: () => import('../views/ProductDetail.vue'),
      meta: { title: '产品详情' },
    },
    {
      path: '/factory',
      name: 'Factory',
      component: () => import('../views/Factory.vue'),
      meta: { title: '工厂展示' },
    },
    {
      path: '/certificates',
      name: 'Certificates',
      component: () => import('../views/Certificates.vue'),
      meta: { title: '资质证书' },
    },
    {
      path: '/faq',
      name: 'Faq',
      component: () => import('../views/Faq.vue'),
      meta: { title: '常见问题' },
    },
    {
      path: '/contact',
      name: 'Contact',
      component: () => import('../views/Contact.vue'),
      meta: { title: '联系我们' },
    },
    {
      path: '/quote-cart',
      name: 'QuoteCart',
      component: () => import('../views/QuoteCart.vue'),
      meta: { title: '询价车' },
    },
  ],
  scrollBehavior(_to, _from, savedPosition) {
    return savedPosition || { top: 0 }
  },
})

const SITE_NAME = '鑫连鑫丝网厂'

router.afterEach((to) => {
  const title = to.meta.title as string
  document.title = title ? `${title} - ${SITE_NAME}` : SITE_NAME
})

export default router
