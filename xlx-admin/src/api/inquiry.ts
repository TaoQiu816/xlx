import axios from 'axios'
import request from './request'

export interface InquiryListParams {
  page: number
  size: number
  status?: string
  keyword?: string
  startDate?: string
  endDate?: string
}

export const inquiryApi = {
  list: (params: InquiryListParams) =>
    request.get('/inquiries', { params }),
  detail: (id: number) => request.get(`/inquiries/${id}`),
  updateStatus: (id: number, status: string) =>
    request.put(`/inquiries/${id}/status`, { status }),
  updateRemark: (id: number, remark: string) =>
    request.put(`/inquiries/${id}/remark`, { remark }),
  delete: (id: number) => request.delete(`/inquiries/${id}`),
  export: (params: { status?: string; keyword?: string; startDate?: string; endDate?: string }) => {
    const token = localStorage.getItem('admin_token')
    return axios.get('/api/admin/inquiries/export', {
      params,
      responseType: 'blob',
      headers: token ? { Authorization: `Bearer ${token}` } : {},
    })
  },
}
