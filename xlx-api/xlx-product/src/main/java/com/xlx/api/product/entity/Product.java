package com.xlx.api.product.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 产品实体。
 * <p>对应数据库表 product，存储产品的完整信息，包含中英文双语字段。</p>
 */
@TableName("product")
public class Product {

    @TableId(type = IdType.AUTO)
    private Long id;
    /** 所属分类 ID */
    private Long categoryId;
    private String nameCn;
    private String nameEn;
    /** URL 别名，用于前端详情页路由 */
    private String slug;
    private String summaryCn;
    private String summaryEn;
    private String descriptionCn;
    private String descriptionEn;
    /** 主图 URL */
    private String mainImage;
    /** 单价 */
    private BigDecimal price;
    /** 价格单位（如：元/公斤、USD/TON） */
    private String priceUnit;
    /** 是否显示价格：1-显示，0-隐藏 */
    private Integer showPrice;
    /** 是否支持定制：1-是，0-否 */
    private Integer supportCustom;
    /** 是否支持询盘：1-是，0-否 */
    private Integer supportInquiry;
    /** 是否支持下单：1-是，0-否 */
    private Integer supportOrder;
    /** 最小起订量 */
    private String moq;
    /** 库存状态 */
    private String stockStatus;
    private String packagingCn;
    private String packagingEn;
    private String applicationsCn;
    private String applicationsEn;
    private String certificateNoteCn;
    private String certificateNoteEn;
    /** 排序权重（升序） */
    private Integer sortOrder;
    /** 是否推荐：1-是，0-否 */
    private Integer isFeatured;
    /** 状态：1-上架，0-下架 */
    private Integer status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getCategoryId() { return categoryId; }
    public void setCategoryId(Long categoryId) { this.categoryId = categoryId; }
    public String getNameCn() { return nameCn; }
    public void setNameCn(String nameCn) { this.nameCn = nameCn; }
    public String getNameEn() { return nameEn; }
    public void setNameEn(String nameEn) { this.nameEn = nameEn; }
    public String getSlug() { return slug; }
    public void setSlug(String slug) { this.slug = slug; }
    public String getSummaryCn() { return summaryCn; }
    public void setSummaryCn(String summaryCn) { this.summaryCn = summaryCn; }
    public String getSummaryEn() { return summaryEn; }
    public void setSummaryEn(String summaryEn) { this.summaryEn = summaryEn; }
    public String getDescriptionCn() { return descriptionCn; }
    public void setDescriptionCn(String descriptionCn) { this.descriptionCn = descriptionCn; }
    public String getDescriptionEn() { return descriptionEn; }
    public void setDescriptionEn(String descriptionEn) { this.descriptionEn = descriptionEn; }
    public String getMainImage() { return mainImage; }
    public void setMainImage(String mainImage) { this.mainImage = mainImage; }
    public BigDecimal getPrice() { return price; }
    public void setPrice(BigDecimal price) { this.price = price; }
    public String getPriceUnit() { return priceUnit; }
    public void setPriceUnit(String priceUnit) { this.priceUnit = priceUnit; }
    public Integer getShowPrice() { return showPrice; }
    public void setShowPrice(Integer showPrice) { this.showPrice = showPrice; }
    public Integer getSupportCustom() { return supportCustom; }
    public void setSupportCustom(Integer supportCustom) { this.supportCustom = supportCustom; }
    public Integer getSupportInquiry() { return supportInquiry; }
    public void setSupportInquiry(Integer supportInquiry) { this.supportInquiry = supportInquiry; }
    public Integer getSupportOrder() { return supportOrder; }
    public void setSupportOrder(Integer supportOrder) { this.supportOrder = supportOrder; }
    public String getMoq() { return moq; }
    public void setMoq(String moq) { this.moq = moq; }
    public String getStockStatus() { return stockStatus; }
    public void setStockStatus(String stockStatus) { this.stockStatus = stockStatus; }
    public String getPackagingCn() { return packagingCn; }
    public void setPackagingCn(String packagingCn) { this.packagingCn = packagingCn; }
    public String getPackagingEn() { return packagingEn; }
    public void setPackagingEn(String packagingEn) { this.packagingEn = packagingEn; }
    public String getApplicationsCn() { return applicationsCn; }
    public void setApplicationsCn(String applicationsCn) { this.applicationsCn = applicationsCn; }
    public String getApplicationsEn() { return applicationsEn; }
    public void setApplicationsEn(String applicationsEn) { this.applicationsEn = applicationsEn; }
    public String getCertificateNoteCn() { return certificateNoteCn; }
    public void setCertificateNoteCn(String certificateNoteCn) { this.certificateNoteCn = certificateNoteCn; }
    public String getCertificateNoteEn() { return certificateNoteEn; }
    public void setCertificateNoteEn(String certificateNoteEn) { this.certificateNoteEn = certificateNoteEn; }
    public Integer getSortOrder() { return sortOrder; }
    public void setSortOrder(Integer sortOrder) { this.sortOrder = sortOrder; }
    public Integer getIsFeatured() { return isFeatured; }
    public void setIsFeatured(Integer isFeatured) { this.isFeatured = isFeatured; }
    public Integer getStatus() { return status; }
    public void setStatus(Integer status) { this.status = status; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}
