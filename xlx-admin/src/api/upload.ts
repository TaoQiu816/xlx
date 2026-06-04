import request from './request'

export const uploadApi = {
  upload: (file: File, bizType = 'general', bizId?: number) => {
    const formData = new FormData()
    formData.append('file', file)
    formData.append('bizType', bizType)
    if (bizId) formData.append('bizId', String(bizId))
    return request.post('/upload', formData, {
      headers: { 'Content-Type': 'multipart/form-data' },
    })
  },
}
