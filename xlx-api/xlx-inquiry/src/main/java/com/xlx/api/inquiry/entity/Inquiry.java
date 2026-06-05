package com.xlx.api.inquiry.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import java.time.LocalDateTime;

/**
 * 客户询盘实体。
 * <p>对应数据库表 inquiry，存储客户提交的询盘信息。
 * 状态流转：new → contacted → quoted → sample_confirmed → order_confirmed → closed/invalid</p>
 */
@TableName("inquiry")
public class Inquiry {

    @TableId(type = IdType.AUTO)
    private Long id;
    /** 客户姓名 */
    @NotBlank(message = "客户姓名不能为空")
    private String name;
    /** 公司名称 */
    private String company;
    private String phone;
    @Email(message = "邮箱格式不正确")
    private String email;
    private String wechat;
    private String whatsapp;
    /** 国家/地区 */
    private String country;
    /** 关联产品 ID（可选） */
    private Long productId;
    /** 关联产品名称（冗余存储，避免 JOIN） */
    private String productName;
    /** 需求数量 */
    private String quantity;
    /** 规格要求 */
    private String specification;
    /** 留言内容 */
    private String message;
    /** 附件 URL */
    private String fileUrl;
    /** 处理状态：new/contacted/quoted/sample_confirmed/order_confirmed/closed/invalid */
    private String status;
    /** 后台备注 */
    private String remark;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getCompany() { return company; }
    public void setCompany(String company) { this.company = company; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getWechat() { return wechat; }
    public void setWechat(String wechat) { this.wechat = wechat; }
    public String getWhatsapp() { return whatsapp; }
    public void setWhatsapp(String whatsapp) { this.whatsapp = whatsapp; }
    public String getCountry() { return country; }
    public void setCountry(String country) { this.country = country; }
    public Long getProductId() { return productId; }
    public void setProductId(Long productId) { this.productId = productId; }
    public String getProductName() { return productName; }
    public void setProductName(String productName) { this.productName = productName; }
    public String getQuantity() { return quantity; }
    public void setQuantity(String quantity) { this.quantity = quantity; }
    public String getSpecification() { return specification; }
    public void setSpecification(String specification) { this.specification = specification; }
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
    public String getFileUrl() { return fileUrl; }
    public void setFileUrl(String fileUrl) { this.fileUrl = fileUrl; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getRemark() { return remark; }
    public void setRemark(String remark) { this.remark = remark; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}
