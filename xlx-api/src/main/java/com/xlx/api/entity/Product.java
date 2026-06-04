package com.xlx.api.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@TableName("product")
public class Product {

    @TableId(type = IdType.AUTO)
    private Long id;
    private Long categoryId;
    private String nameCn;
    private String nameEn;
    private String slug;
    private String summaryCn;
    private String summaryEn;
    private String descriptionCn;
    private String descriptionEn;
    private String mainImage;
    private BigDecimal price;
    private String priceUnit;
    private Integer showPrice;
    private Integer supportCustom;
    private Integer supportInquiry;
    private Integer supportOrder;
    private String moq;
    private String stockStatus;
    private String packagingCn;
    private String packagingEn;
    private String applicationsCn;
    private String applicationsEn;
    private String certificateNoteCn;
    private String certificateNoteEn;
    private Integer sortOrder;
    private Integer isFeatured;
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
