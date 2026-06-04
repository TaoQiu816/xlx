package com.xlx.api.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.time.LocalDateTime;

@TableName("product_spec")
public class ProductSpec {

    @TableId(type = IdType.AUTO)
    private Long id;
    private Long productId;
    private String specNameCn;
    private String specNameEn;
    private String specValueCn;
    private String specValueEn;
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
