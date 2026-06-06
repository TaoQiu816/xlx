export interface StatusOption {
  label: string
  type: string
}

export const inquiryStatusMap: Record<string, StatusOption> = {
  new: { label: '新询盘', type: 'danger' },
  contacted: { label: '已联系', type: 'warning' },
  quoted: { label: '已报价', type: 'success' },
  sample_confirmed: { label: '样品沟通中', type: '' },
  order_confirmed: { label: '订单确认', type: 'success' },
  closed: { label: '已关闭', type: 'info' },
  invalid: { label: '无效询盘', type: 'info' },
}

export const inquiryStatusLabel = (status: string): string =>
  inquiryStatusMap[status]?.label || status

export const inquiryStatusType = (status: string): string =>
  inquiryStatusMap[status]?.type || ''
