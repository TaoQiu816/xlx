import { useHead } from '@unhead/vue'

interface OrganizationSchema {
  '@context': string
  '@type': string
  name: string
  url: string
  logo: string
  description: string
  address?: {
    '@type': string
    addressCountry: string
    addressRegion: string
    addressLocality: string
    streetAddress: string
  }
  contactPoint?: {
    '@type': string
    telephone: string
    contactType: string
  }
}

interface ProductSchema {
  '@context': string
  '@type': string
  name: string
  description: string
  image?: string
  brand?: { '@type': string; name: string }
  manufacturer?: { '@type': string; name: string }
  offers?: {
    '@type': string
    priceCurrency: string
    price?: string
    availability: string
  }
}

interface BreadcrumbSchema {
  '@context': string
  '@type': string
  itemListElement: Array<{
    '@type': string
    position: number
    name: string
    item?: string
  }>
}

export function useOrganizationSchema(data: Partial<OrganizationSchema> = {}) {
  const schema: OrganizationSchema = {
    '@context': 'https://schema.org',
    '@type': 'Organization',
    name: '鑫连鑫丝网厂',
    url: 'https://www.xlx-siwang.com',
    logo: 'https://www.xlx-siwang.com/logo.png',
    description: '鑫连鑫丝网厂，专业生产不锈钢丝、钛丝、蒙乃尔丝、铜丝及各类丝网产品。',
    ...data,
  }

  useHead({
    script: [
      { type: 'application/ld+json', innerHTML: JSON.stringify(schema) },
    ],
  })
}

export function useProductSchema(product: {
  name: string
  description?: string
  mainImage?: string
  price?: string
  priceUnit?: string
  showPrice?: number
}) {
  const schema: ProductSchema = {
    '@context': 'https://schema.org',
    '@type': 'Product',
    name: product.name,
    description: product.description || product.name,
    image: product.mainImage,
    brand: { '@type': 'Brand', name: '鑫连鑫' },
    manufacturer: { '@type': 'Organization', name: '鑫连鑫丝网厂' },
  }

  if (product.showPrice && product.price) {
    schema.offers = {
      '@type': 'Offer',
      priceCurrency: 'CNY',
      price: product.price,
      availability: 'https://schema.org/InStock',
    }
  }

  useHead({
    script: [
      { type: 'application/ld+json', innerHTML: JSON.stringify(schema) },
    ],
  })
}

export function useBreadcrumbSchema(items: Array<{ name: string; url?: string }>) {
  const siteUrl = 'https://www.xlx-siwang.com'
  const schema: BreadcrumbSchema = {
    '@context': 'https://schema.org',
    '@type': 'BreadcrumbList',
    itemListElement: items.map((item, idx) => ({
      '@type': 'ListItem',
      position: idx + 1,
      name: item.name,
      ...(item.url ? { item: `${siteUrl}${item.url}` } : {}),
    })),
  }

  useHead({
    script: [
      { type: 'application/ld+json', innerHTML: JSON.stringify(schema) },
    ],
  })
}
