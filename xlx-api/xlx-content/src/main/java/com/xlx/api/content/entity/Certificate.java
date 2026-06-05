package com.xlx.api.content.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 证书实体。
 * <p>对应数据库表 certificate，存储企业资质证书和检测报告信息。</p>
 */
@TableName("certificate")
public class Certificate {

    @TableId(type = IdType.AUTO)
    private Long id;
    /** 证书标题（中文） */
    private String titleCn;
    /** 证书标题（英文） */
    private String titleEn;
    /** 证书图片 URL */
    private String imageUrl;
    /** 证书描述（中文） */
    private String descriptionCn;
    /** 证书描述（英文） */
    private String descriptionEn;
    /** 证书类型：certificate-资质证书，report-检测报告，quality-质量认证 */
    private String certificateType;
    /** 签发日期 */
    private LocalDate issueDate;
    /** 排序权重（升序） */
    private Integer sortOrder;
    /** 状态：1-显示，0-隐藏 */
    private Integer status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getTitleCn() { return titleCn; }
    public void setTitleCn(String titleCn) { this.titleCn = titleCn; }
    public String getTitleEn() { return titleEn; }
    public void setTitleEn(String titleEn) { this.titleEn = titleEn; }
    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
    public String getDescriptionCn() { return descriptionCn; }
    public void setDescriptionCn(String descriptionCn) { this.descriptionCn = descriptionCn; }
    public String getDescriptionEn() { return descriptionEn; }
    public void setDescriptionEn(String descriptionEn) { this.descriptionEn = descriptionEn; }
    public String getCertificateType() { return certificateType; }
    public void setCertificateType(String certificateType) { this.certificateType = certificateType; }
    public LocalDate getIssueDate() { return issueDate; }
    public void setIssueDate(LocalDate issueDate) { this.issueDate = issueDate; }
    public Integer getSortOrder() { return sortOrder; }
    public void setSortOrder(Integer sortOrder) { this.sortOrder = sortOrder; }
    public Integer getStatus() { return status; }
    public void setStatus(Integer status) { this.status = status; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}
