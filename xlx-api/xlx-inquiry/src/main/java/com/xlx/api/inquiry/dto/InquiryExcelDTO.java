package com.xlx.api.inquiry.dto;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class InquiryExcelDTO {

    private static final DateTimeFormatter DT_FMT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @ExcelProperty("编号")
    @ColumnWidth(8)
    private Long id;

    @ExcelProperty("提交时间")
    @ColumnWidth(20)
    private String createdAt;

    @ExcelProperty("客户姓名")
    @ColumnWidth(15)
    private String name;

    @ExcelProperty("公司名称")
    @ColumnWidth(20)
    private String company;

    @ExcelProperty("邮箱")
    @ColumnWidth(25)
    private String email;

    @ExcelProperty("联系电话")
    @ColumnWidth(15)
    private String phone;

    @ExcelProperty("微信")
    @ColumnWidth(15)
    private String wechat;

    @ExcelProperty("WhatsApp")
    @ColumnWidth(15)
    private String whatsapp;

    @ExcelProperty("国家/地区")
    @ColumnWidth(15)
    private String country;

    @ExcelProperty("产品名称")
    @ColumnWidth(25)
    private String productName;

    @ExcelProperty("数量")
    @ColumnWidth(15)
    private String quantity;

    @ExcelProperty("规格要求")
    @ColumnWidth(25)
    private String specification;

    @ExcelProperty("留言内容")
    @ColumnWidth(40)
    private String message;

    @ExcelProperty("状态")
    @ColumnWidth(12)
    private String statusLabel;

    @ExcelProperty("后台备注")
    @ColumnWidth(30)
    private String remark;

    public static InquiryExcelDTO from(com.xlx.api.inquiry.entity.Inquiry inquiry) {
        InquiryExcelDTO dto = new InquiryExcelDTO();
        dto.id = inquiry.getId();
        dto.createdAt = inquiry.getCreatedAt() != null ? inquiry.getCreatedAt().format(DT_FMT) : "";
        dto.name = defaultStr(inquiry.getName());
        dto.company = defaultStr(inquiry.getCompany());
        dto.email = defaultStr(inquiry.getEmail());
        dto.phone = defaultStr(inquiry.getPhone());
        dto.wechat = defaultStr(inquiry.getWechat());
        dto.whatsapp = defaultStr(inquiry.getWhatsapp());
        dto.country = defaultStr(inquiry.getCountry());
        dto.productName = defaultStr(inquiry.getProductName());
        dto.quantity = defaultStr(inquiry.getQuantity());
        dto.specification = defaultStr(inquiry.getSpecification());
        dto.message = defaultStr(inquiry.getMessage());
        dto.statusLabel = statusLabel(inquiry.getStatus());
        dto.remark = defaultStr(inquiry.getRemark());
        return dto;
    }

    private static String statusLabel(String status) {
        if (status == null) return "-";
        return switch (status) {
            case "new" -> "新询盘";
            case "contacted" -> "已联系";
            case "quoted" -> "已报价";
            case "sample_confirmed" -> "样品沟通中";
            case "order_confirmed" -> "订单确认";
            case "closed" -> "已关闭";
            case "invalid" -> "无效询盘";
            default -> status;
        };
    }

    private static String defaultStr(String s) {
        return (s == null) ? "" : s;
    }
}
