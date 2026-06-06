import request from './request'

export const productApi = {
  list: (params: { page: number; size: number; categoryId?: number; status?: number; keyword?: string }) =>
    request.get('/products', { params }),
  detail: (id: number) => request.get(`/products/${id}`),
  create: (data: any) => request.post('/products', data),
  update: (id: number, data: any) => request.put(`/products/${id}`, data),
  delete: (id: number) => request.delete(`/products/${id}`),

  // Specs
  addSpec: (productId: number, data: any) =>
    request.post(`/products/${productId}/specs`, data),
  updateSpec: (id: number, data: any) => request.put(`/products/specs/${id}`, data),
  deleteSpec: (id: number) => request.delete(`/products/specs/${id}`),

  // Images
  listImages: (productId: number) => request.get(`/products/${productId}/images`),
  addImage: (productId: number, data: any) =>
    request.post(`/products/${productId}/images`, data),
  deleteImage: (id: number) => request.delete(`/products/images/${id}`),
}
