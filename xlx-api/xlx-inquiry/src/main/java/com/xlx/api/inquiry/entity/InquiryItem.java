package com.xlx.api.inquiry.entity;

import com.baomidou.mybatisplus.annotation.*;
import java.time.LocalDateTime;

/**
 * 询盘产品明细实体。
 * <p>对应数据库表 inquiry_item，存储多产品询盘的各产品明细。</p>
 */
@TableName("inquiry_item")
public class InquiryItem {

    @TableId(type = IdType.AUTO)
    private Long id;
    /** 关联 inquiry.id */
    private Long inquiryId;
    /** 关联 product.id，可为空（产品删除后保留历史） */
    private Long productId;
    /** 产品名称快照（中文） */
    private String productNameCn;
    /** 产品名称快照（英文） */
    private String productNameEn;
    /** 需求数量 */
    private String quantity;
    /** 规格要求 */
    private String specification;
    /** 排序 */
    private Integer sortOrder;
    /** 逻辑删除：0-正常，1-已删除 */
    @TableLogic
    private Integer deleted;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getInquiryId() { return inquiryId; }
    public void setInquiryId(Long inquiryId) { this.inquiryId = inquiryId; }
    public Long getProductId() { return productId; }
    public void setProductId(Long productId) { this.productId = productId; }
    public String getProductNameCn() { return productNameCn; }
    public void setProductNameCn(String productNameCn) { this.productNameCn = productNameCn; }
    public String getProductNameEn() { return productNameEn; }
    public void setProductNameEn(String productNameEn) { this.productNameEn = productNameEn; }
    public String getQuantity() { return quantity; }
    public void setQuantity(String quantity) { this.quantity = quantity; }
    public String getSpecification() { return specification; }
    public void setSpecification(String specification) { this.specification = specification; }
    public Integer getSortOrder() { return sortOrder; }
    public void setSortOrder(Integer sortOrder) { this.sortOrder = sortOrder; }
    public Integer getDeleted() { return deleted; }
    public void setDeleted(Integer deleted) { this.deleted = deleted; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}
