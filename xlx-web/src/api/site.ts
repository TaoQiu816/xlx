import request from './request'

export const siteApi = {
  config: () => request.get('/config'),
  categories: () => request.get('/categories'),
  products: (params: { page?: number; size?: number; categoryId?: number; keyword?: string; sort?: string }) =>
    request.get('/products', { params }),
  featuredProducts: (limit = 8) =>
    request.get('/products/featured', { params: { limit } }),
  productDetail: (slug: string) => request.get(`/products/${slug}`),
  factoryImages: () => request.get('/factory-images'),
  certificates: () => request.get('/certificates'),
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
