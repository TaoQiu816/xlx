import request from './request'

export const certificateApi = {
  list: (params: { page: number; size: number }) =>
    request.get('/certificates', { params }),
  create: (data: any) => request.post('/certificates', data),
  update: (id: number, data: any) => request.put(`/certificates/${id}`, data),
  delete: (id: number) => request.delete(`/certificates/${id}`),
}
