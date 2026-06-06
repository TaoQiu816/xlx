import request from './request'

export const adminUserApi = {
  list: (params: { page: number; size: number }) =>
    request.get('/auth/admins', { params }),
  create: (data: { username: string; password: string; nickname?: string; role?: string }) =>
    request.post('/auth/admins', data),
  update: (id: number, data: { nickname?: string; role?: string; status?: number }) =>
    request.put(`/auth/admins/${id}`, data),
  changePassword: (id: number, password: string) =>
    request.put(`/auth/admins/${id}/password`, { password }),
  delete: (id: number) =>
    request.delete(`/auth/admins/${id}`),
}
