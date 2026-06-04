import request from './request'

export const authApi = {
  login: (data: { username: string; password: string }) =>
    request.post('/auth/login', data),
  profile: () => request.get('/auth/profile'),
}
