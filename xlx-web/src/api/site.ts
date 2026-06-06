import request from './request'
import { cachedRequest } from '../utils/apiCache'

const TTL = {
  CONFIG: 10 * 60 * 1000,
  CATEGORIES: 10 * 60 * 1000,
  PRODUCTS: 3 * 60 * 1000,
  PRODUCT_DETAIL: 5 * 60 * 1000,
  FACTORY: 10 * 60 * 1000,
  CERTIFICATES: 10 * 60 * 1000,
}

export const siteApi = {
  config: () =>
    cachedRequest(() => request.get('/config'), '/config', undefined, TTL.CONFIG),

  categories: () =>
    cachedRequest(() => request.get('/categories'), '/categories', undefined, TTL.CATEGORIES),

  products: (params: { page?: number; size?: number; categoryId?: number; keyword?: string; sort?: string }) =>
    cachedRequest(() => request.get('/products', { params }), '/products', params, TTL.PRODUCTS),

  featuredProducts: (limit = 8) =>
    cachedRequest(
      () => request.get('/products/featured', { params: { limit } }),
      '/products/featured',
      { limit },
      TTL.PRODUCTS,
    ),

  productDetail: (slug: string) =>
    cachedRequest(() => request.get(`/products/${slug}`), `/products/${slug}`, undefined, TTL.PRODUCT_DETAIL),

  factoryImages: () =>
    cachedRequest(() => request.get('/factory-images'), '/factory-images', undefined, TTL.FACTORY),

  certificates: () =>
    cachedRequest(() => request.get('/certificates'), '/certificates', undefined, TTL.CERTIFICATES),

  submitInquiry: (data: any) => request.post('/inquiries', data),

  uploadFile: (file: File, onProgress?: (percent: number) => void) => {
    const formData = new FormData()
    formData.append('file', file)
    return request.post('/upload', formData, {
      headers: { 'Content-Type': 'multipart/form-data' },
      onUploadProgress: (e: any) => {
        if (e.total && onProgress) {
          onProgress(Math.round((e.loaded * 100) / e.total))
        }
      },
    })
  },
}
