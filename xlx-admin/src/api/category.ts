import request from './request'

export const categoryApi = {
  list: () => request.get('/categories'),
  page: (params: { page: number; size: number }) =>
    request.get('/categories/page', { params }),
  create: (data: any) => request.post('/categories', data),
  update: (id: number, data: any) => request.put(`/categories/${id}`, data),
  delete: (id: number) => request.delete(`/categories/${id}`),
}
