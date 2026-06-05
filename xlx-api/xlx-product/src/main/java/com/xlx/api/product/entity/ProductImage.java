package com.xlx.api.product.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.time.LocalDateTime;

/**
 * 产品图片实体。
 * <p>对应数据库表 product_image，存储产品的附加图片（主图在 Product.mainImage 中）。</p>
 */
@TableName("product_image")
public class ProductImage {

    @TableId(type = IdType.AUTO)
    private Long id;
    /** 所属产品 ID */
    private Long productId;
    /** 图片 URL */
    private String imageUrl;
    /** 图片替代文本（中文），用于 SEO 和无障碍 */
    private String altCn;
    /** 图片替代文本（英文） */
    private String altEn;
    /** 排序权重（升序） */
    private Integer sortOrder;
    private LocalDateTime createdAt;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getProductId() { return productId; }
    public void setProductId(Long productId) { this.productId = productId; }
    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
    public String getAltCn() { return altCn; }
    public void setAltCn(String altCn) { this.altCn = altCn; }
    public String getAltEn() { return altEn; }
    public void setAltEn(String altEn) { this.altEn = altEn; }
    public Integer getSortOrder() { return sortOrder; }
    public void setSortOrder(Integer sortOrder) { this.sortOrder = sortOrder; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
