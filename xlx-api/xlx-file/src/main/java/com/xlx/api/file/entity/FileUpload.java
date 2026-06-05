package com.xlx.api.file.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.time.LocalDateTime;

/**
 * 文件上传记录实体。
 * <p>对应数据库表 file_upload，记录所有上传文件的元数据。
 * 通过 bizType + bizId 实现多态关联（可绑定到产品、询盘等不同业务实体）。</p>
 */
@TableName("file_upload")
public class FileUpload {

    @TableId(type = IdType.AUTO)
    private Long id;
    /** 原始文件名 */
    private String originalName;
    /** 存储文件名（UUID 生成） */
    private String fileName;
    /** 文件访问 URL */
    private String fileUrl;
    /** 文件 MIME 类型 */
    private String fileType;
    /** 文件大小（字节） */
    private Long fileSize;
    /** 业务类型（如：product、inquiry、general） */
    private String bizType;
    /** 关联业务 ID */
    private Long bizId;
    private LocalDateTime createdAt;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getOriginalName() { return originalName; }
    public void setOriginalName(String originalName) { this.originalName = originalName; }
    public String getFileName() { return fileName; }
    public void setFileName(String fileName) { this.fileName = fileName; }
    public String getFileUrl() { return fileUrl; }
    public void setFileUrl(String fileUrl) { this.fileUrl = fileUrl; }
    public String getFileType() { return fileType; }
    public void setFileType(String fileType) { this.fileType = fileType; }
    public Long getFileSize() { return fileSize; }
    public void setFileSize(Long fileSize) { this.fileSize = fileSize; }
    public String getBizType() { return bizType; }
    public void setBizType(String bizType) { this.bizType = bizType; }
    public Long getBizId() { return bizId; }
    public void setBizId(Long bizId) { this.bizId = bizId; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
