package com.xlx.api.inquiry.service;

import com.xlx.api.config.service.SiteConfigService;
import com.xlx.api.inquiry.entity.Inquiry;
import com.xlx.api.inquiry.entity.InquiryItem;
import jakarta.mail.internet.MimeMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

@Service
public class InquiryNotificationService implements InquiryNotifier {

    private static final Logger log = LoggerFactory.getLogger(InquiryNotificationService.class);

    private final SiteConfigService configService;
    private final InquiryItemService inquiryItemService;

    public InquiryNotificationService(SiteConfigService configService, InquiryItemService inquiryItemService) {
        this.configService = configService;
        this.inquiryItemService = inquiryItemService;
    }

    @Async("emailExecutor")
    public void sendInquiryNotification(Inquiry inquiry) {
        try {
            Map<String, String> config = configService.getConfigMap();

            String enabled = config.getOrDefault("mail_enabled", "false");
            if (!"true".equalsIgnoreCase(enabled)) {
                return;
            }

            String host = config.getOrDefault("mail_host", "");
            String port = config.getOrDefault("mail_port", "587");
            String username = config.getOrDefault("mail_username", "");
            String password = config.getOrDefault("mail_password", "");
            String fromName = config.getOrDefault("mail_from_name", "鑫连鑫丝网");
            String to = config.getOrDefault("mail_to", "");
            String tls = config.getOrDefault("mail_tls", "true");

            if (host.isBlank() || username.isBlank() || to.isBlank()) {
                log.warn("邮件配置不完整，跳过发送通知");
                return;
            }

            JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
            mailSender.setHost(host);
            mailSender.setPort(Integer.parseInt(port));
            mailSender.setUsername(username);
            mailSender.setPassword(password);
            mailSender.setDefaultEncoding("UTF-8");

            var props = mailSender.getJavaMailProperties();
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.starttls.enable", tls);
            props.put("mail.smtp.timeout", "5000");
            props.put("mail.smtp.connectiontimeout", "5000");

            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setFrom(username, fromName);
            helper.setTo(to);
            helper.setSubject("新询盘通知 - " + inquiry.getName() + (inquiry.getCompany() != null ? "(" + inquiry.getCompany() + ")" : ""));
            helper.setText(buildHtmlBody(inquiry), true);

            mailSender.send(message);
            log.info("询盘通知邮件发送成功，询盘ID: {}", inquiry.getId());
        } catch (Exception e) {
            log.warn("询盘通知邮件发送失败，询盘ID: {}，原因: {}", inquiry.getId(), e.getMessage());
        }
    }

    private String buildHtmlBody(Inquiry inquiry) {
        StringBuilder sb = new StringBuilder();
        sb.append("<html><body style='font-family:Arial,sans-serif;color:#333;'>");
        sb.append("<h2 style='color:#1e40af;'>新询盘通知</h2>");
        sb.append("<table style='border-collapse:collapse;width:100%;max-width:600px;'>");

        appendRow(sb, "客户姓名", inquiry.getName());
        appendRow(sb, "公司名称", inquiry.getCompany());
        appendRow(sb, "联系电话", inquiry.getPhone());
        appendRow(sb, "邮箱", inquiry.getEmail());
        appendRow(sb, "微信", inquiry.getWechat());
        appendRow(sb, "WhatsApp", inquiry.getWhatsapp());
        appendRow(sb, "国家/地区", inquiry.getCountry());

        // 产品信息（单产品）
        if (inquiry.getProductName() != null && !inquiry.getProductName().isBlank()) {
            appendRow(sb, "产品名称", inquiry.getProductName());
            appendRow(sb, "需求数量", inquiry.getQuantity());
            appendRow(sb, "规格要求", inquiry.getSpecification());
        }

        appendRow(sb, "留言内容", inquiry.getMessage());
        appendRow(sb, "提交时间", inquiry.getCreatedAt() != null
                ? inquiry.getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) : "-");

        sb.append("</table>");

        // 多产品明细
        List<InquiryItem> items = inquiryItemService.listByInquiryId(inquiry.getId());
        if (!items.isEmpty()) {
            sb.append("<h3 style='color:#1e40af;margin-top:20px;'>询价产品明细</h3>");
            sb.append("<table style='border-collapse:collapse;width:100%;max-width:600px;'>");
            sb.append("<tr style='background:#f1f5f9;'>");
            sb.append("<th style='border:1px solid #e2e8f0;padding:8px;text-align:left;'>序号</th>");
            sb.append("<th style='border:1px solid #e2e8f0;padding:8px;text-align:left;'>产品名称</th>");
            sb.append("<th style='border:1px solid #e2e8f0;padding:8px;text-align:left;'>数量</th>");
            sb.append("<th style='border:1px solid #e2e8f0;padding:8px;text-align:left;'>规格</th>");
            sb.append("</tr>");
            for (int i = 0; i < items.size(); i++) {
                InquiryItem item = items.get(i);
                sb.append("<tr>");
                sb.append("<td style='border:1px solid #e2e8f0;padding:8px;'>").append(i + 1).append("</td>");
                sb.append("<td style='border:1px solid #e2e8f0;padding:8px;'>").append(item.getProductNameCn()).append("</td>");
                sb.append("<td style='border:1px solid #e2e8f0;padding:8px;'>").append(defaultStr(item.getQuantity())).append("</td>");
                sb.append("<td style='border:1px solid #e2e8f0;padding:8px;'>").append(defaultStr(item.getSpecification())).append("</td>");
                sb.append("</tr>");
            }
            sb.append("</table>");
        }

        sb.append("</body></html>");
        return sb.toString();
    }

    private void appendRow(StringBuilder sb, String label, String value) {
        sb.append("<tr>");
        sb.append("<td style='border:1px solid #e2e8f0;padding:8px;font-weight:bold;width:120px;background:#f8fafc;'>").append(label).append("</td>");
        sb.append("<td style='border:1px solid #e2e8f0;padding:8px;'>").append(defaultStr(value)).append("</td>");
        sb.append("</tr>");
    }

    private String defaultStr(String s) {
        return (s == null || s.isBlank()) ? "-" : s;
    }
}
