package com.xlx.api.product.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.time.LocalDateTime;

/**
 * 产品规格实体。
 * <p>对应数据库表 product_spec，存储产品的技术规格参数（如丝径、网孔等）。</p>
 */
@TableName("product_spec")
public class ProductSpec {

    @TableId(type = IdType.AUTO)
    private Long id;
    /** 所属产品 ID */
    private Long productId;
    /** 规格名称（中文），如：材质、丝径、网孔 */
    private String specNameCn;
    /** 规格名称（英文） */
    private String specNameEn;
    /** 规格值（中文），如：304不锈钢、0.5mm */
    private String specValueCn;
    /** 规格值（英文） */
    private String specValueEn;
    /** 排序权重（升序） */
    private Integer sortOrder;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getProductId() { return productId; }
    public void setProductId(Long productId) { this.productId = productId; }
    public String getSpecNameCn() { return specNameCn; }
    public void setSpecNameCn(String specNameCn) { this.specNameCn = specNameCn; }
    public String getSpecNameEn() { return specNameEn; }
    public void setSpecNameEn(String specNameEn) { this.specNameEn = specNameEn; }
    public String getSpecValueCn() { return specValueCn; }
    public void setSpecValueCn(String specValueCn) { this.specValueCn = specValueCn; }
    public String getSpecValueEn() { return specValueEn; }
    public void setSpecValueEn(String specValueEn) { this.specValueEn = specValueEn; }
    public Integer getSortOrder() { return sortOrder; }
    public void setSortOrder(Integer sortOrder) { this.sortOrder = sortOrder; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}
