import request from './request'

export const factoryApi = {
  list: (params: { page: number; size: number }) =>
    request.get('/factory-images', { params }),
  create: (data: any) => request.post('/factory-images', data),
  update: (id: number, data: any) => request.put(`/factory-images/${id}`, data),
  delete: (id: number) => request.delete(`/factory-images/${id}`),
}
