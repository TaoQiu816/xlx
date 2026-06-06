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
}
