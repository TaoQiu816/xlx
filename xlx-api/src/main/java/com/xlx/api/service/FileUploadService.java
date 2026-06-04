package com.xlx.api.service;

import com.xlx.api.common.BusinessException;
import com.xlx.api.entity.FileUpload;
import com.xlx.api.mapper.FileUploadMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Set;
import java.util.UUID;

@Service
public class FileUploadService {

    @Value("${upload.path:./uploads}")
    private String uploadPath;

    @Value("${upload.allowed-types:jpg,jpeg,png,gif,webp,pdf,doc,docx,xls,xlsx,txt}")
    private String allowedTypes;

    @Value("${upload.max-size:10485760}")
    private long maxSize;

    private final FileUploadMapper mapper;

    private static final Set<String> BLOCKED_EXTENSIONS = Set.of(
            "exe", "sh", "bat", "cmd", "js", "php", "jar", "py", "msi", "com", "vbs"
    );

    public FileUploadService(FileUploadMapper mapper) {
        this.mapper = mapper;
    }

    public FileUpload upload(MultipartFile file, String bizType, Long bizId) {
        if (file.isEmpty()) {
            throw new BusinessException("文件不能为空");
        }
        if (file.getSize() > maxSize) {
            throw new BusinessException("文件大小超过限制（最大 10MB）");
        }

        String originalName = file.getOriginalFilename();
        String ext = getExtension(originalName).toLowerCase();

        if (BLOCKED_EXTENSIONS.contains(ext)) {
            throw new BusinessException("不允许上传此类型文件");
        }

        Set<String> allowed = Set.of(allowedTypes.split(","));
        if (!allowed.contains(ext)) {
            throw new BusinessException("不支持的文件类型: " + ext);
        }

        // Generate unique file name
        String datePath = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy/MM"));
        String newFileName = UUID.randomUUID().toString().replace("-", "") + "." + ext;
        String relativePath = bizType + "/" + datePath;
        String fullPath = uploadPath + "/" + relativePath;

        // Create directory
        File dir = new File(fullPath);
        if (!dir.exists()) dir.mkdirs();

        // Save file
        try {
            file.transferTo(new File(dir, newFileName));
        } catch (IOException e) {
            throw new BusinessException("文件上传失败");
        }

        // Save record
        FileUpload record = new FileUpload();
        record.setOriginalName(originalName);
        record.setFileName(newFileName);
        record.setFileUrl("/uploads/" + relativePath + "/" + newFileName);
        record.setFileType(file.getContentType());
        record.setFileSize(file.getSize());
        record.setBizType(bizType);
        record.setBizId(bizId);
        mapper.insert(record);

        return record;
    }

    private String getExtension(String filename) {
        if (filename == null || !filename.contains(".")) return "";
        return filename.substring(filename.lastIndexOf(".") + 1);
    }
}
