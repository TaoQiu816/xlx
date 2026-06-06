package com.xlx.api.dto;

import java.util.List;
import java.util.Map;

/**
 * Dashboard 统计数据 DTO。
 * 聚合产品和询盘模块数据，供后台首页展示。
 */
public class DashboardStats {

    /** 产品总数 */
    private long productTotal;
    /** 上架产品数 */
    private long productOnline;
    /** 询盘总数 */
    private long inquiryTotal;
    /** 待处理询盘数（状态为 new） */
    private long inquiryPending;
    /** 询盘趋势（按天统计） */
    private List<TrendItem> inquiryTrend;
    /** 询盘状态分布 */
    private List<StatusItem> inquiryStatus;
    /** 最新询盘列表 */
    private List<Map<String, Object>> latestInquiries;

    public long getProductTotal() { return productTotal; }
    public void setProductTotal(long productTotal) { this.productTotal = productTotal; }
    public long getProductOnline() { return productOnline; }
    public void setProductOnline(long productOnline) { this.productOnline = productOnline; }
    public long getInquiryTotal() { return inquiryTotal; }
    public void setInquiryTotal(long inquiryTotal) { this.inquiryTotal = inquiryTotal; }
    public long getInquiryPending() { return inquiryPending; }
    public void setInquiryPending(long inquiryPending) { this.inquiryPending = inquiryPending; }
    public List<TrendItem> getInquiryTrend() { return inquiryTrend; }
    public void setInquiryTrend(List<TrendItem> inquiryTrend) { this.inquiryTrend = inquiryTrend; }
    public List<StatusItem> getInquiryStatus() { return inquiryStatus; }
    public void setInquiryStatus(List<StatusItem> inquiryStatus) { this.inquiryStatus = inquiryStatus; }
    public List<Map<String, Object>> getLatestInquiries() { return latestInquiries; }
    public void setLatestInquiries(List<Map<String, Object>> latestInquiries) { this.latestInquiries = latestInquiries; }

    /** 询盘趋势项 */
    public static class TrendItem {
        private String date;
        private int count;

        public TrendItem(String date, int count) {
            this.date = date;
            this.count = count;
        }

        public String getDate() { return date; }
        public void setDate(String date) { this.date = date; }
        public int getCount() { return count; }
        public void setCount(int count) { this.count = count; }
    }

    /** 询盘状态项 */
    public static class StatusItem {
        private String status;
        private String label;
        private int count;

        public StatusItem(String status, String label, int count) {
            this.status = status;
            this.label = label;
            this.count = count;
        }

        public String getStatus() { return status; }
        public void setStatus(String status) { this.status = status; }
        public String getLabel() { return label; }
        public void setLabel(String label) { this.label = label; }
        public int getCount() { return count; }
        public void setCount(int count) { this.count = count; }
    }
}
