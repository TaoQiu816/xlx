import request from './request'

export interface TrendItem {
  date: string
  count: number
}

export interface StatusItem {
  status: string
  label: string
  count: number
}

export interface LatestInquiry {
  id: number
  name: string
  company: string
  productName: string
  status: string
  createdAt: string
}

export interface DashboardStats {
  productTotal: number
  productOnline: number
  inquiryTotal: number
  inquiryPending: number
  inquiryTrend: TrendItem[]
  inquiryStatus: StatusItem[]
  latestInquiries: LatestInquiry[]
}

export const dashboardApi = {
  stats: (trendDays = 7) =>
    request.get('/admin/dashboard/stats', { params: { trendDays } }),
}
