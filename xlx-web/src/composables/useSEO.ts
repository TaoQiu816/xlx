import { useHead, useSeoMeta } from '@unhead/vue'
import { computed } from 'vue'
import { useRoute } from 'vue-router'
import { getLocale } from '../i18n'

const SITE_URL = 'https://www.xlx-siwang.com'
const SITE_NAME_ZH = '鑫连鑫丝网厂'
const SITE_NAME_EN = 'Xinlianxin Wire Mesh'
const DEFAULT_DESC_ZH = '鑫连鑫丝网厂，专业生产不锈钢丝、钛丝、蒙乃尔丝、铜丝及各类丝网产品，支持标准规格与定制加工。'
const DEFAULT_DESC_EN = 'Xinlianxin Wire Mesh — professional manufacturer of stainless steel wire, titanium wire, Monel wire, copper wire and mesh products.'

export interface PageSEO {
  title: string
  description: string
  image?: string
  type?: string
}

export function useSEO(seo?: Partial<PageSEO>) {
  const route = useRoute()

  const siteName = computed(() => getLocale() === 'en' ? SITE_NAME_EN : SITE_NAME_ZH)
  const defaultDesc = computed(() => getLocale() === 'en' ? DEFAULT_DESC_EN : DEFAULT_DESC_ZH)
  const ogLocale = computed(() => getLocale() === 'en' ? 'en' : 'zh_CN')

  const merged = computed(() => ({
    title: seo?.title || siteName.value,
    description: seo?.description || defaultDesc.value,
    image: seo?.image || '/logo.png',
    type: seo?.type || 'website',
  }))

  const pageTitle = computed(() => {
    return merged.value.title === siteName.value
      ? siteName.value
      : `${merged.value.title} - ${siteName.value}`
  })

  const pageUrl = computed(() => `${SITE_URL}${route.path}`)

  useHead({
    title: pageTitle,
    htmlAttrs: { lang: () => getLocale() === 'en' ? 'en' : 'zh-CN' },
    link: [
      { rel: 'canonical', href: pageUrl },
    ],
  })

  useSeoMeta({
    title: pageTitle,
    description: () => merged.value.description,
    ogTitle: pageTitle,
    ogDescription: () => merged.value.description,
    ogImage: () => merged.value.image,
    ogUrl: pageUrl,
    ogSiteName: siteName,
    ogLocale,
    twitterCard: 'summary',
    twitterTitle: pageTitle,
    twitterDescription: () => merged.value.description,
    twitterImage: () => merged.value.image,
    robots: 'index, follow',
  })

  useHead({
    meta: [
      { property: 'og:type', content: () => merged.value.type || 'website' },
    ],
  })
}
