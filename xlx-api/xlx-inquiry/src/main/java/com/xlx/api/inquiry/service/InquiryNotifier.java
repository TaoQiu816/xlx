package com.xlx.api.inquiry.service;

import com.xlx.api.inquiry.entity.Inquiry;

/**
 * 询盘通知接口。
 * <p>用于询盘创建后的异步通知（邮件等），便于测试时替换为无操作实现。</p>
 */
public interface InquiryNotifier {
    void sendInquiryNotification(Inquiry inquiry);
}
