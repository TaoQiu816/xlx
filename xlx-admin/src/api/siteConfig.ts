import request from './request'

export const siteConfigApi = {
  list: () => request.get('/site-config'),
  update: (data: Record<string, string>) => request.put('/site-config', data),
}
