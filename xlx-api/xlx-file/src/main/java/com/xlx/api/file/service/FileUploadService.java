package com.xlx.api.file.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xlx.api.common.BusinessException;
import com.xlx.api.common.PageResult;
import com.xlx.api.file.entity.FileUpload;
import com.xlx.api.file.mapper.FileUploadMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Set;
import java.util.UUID;

/**
 * 文件上传服务。
 * <p>处理文件上传，包含类型校验、大小校验、危险文件拦截。
 * 文件按 业务类型/年月 目录结构存储，文件名使用 UUID 避免冲突。</p>
 */
@Service
public class FileUploadService {

    @Value("${upload.path:./uploads}")
    private String uploadPath;

    @Value("${upload.allowed-types:jpg,jpeg,png,gif,webp,pdf,doc,docx,xls,xlsx,txt}")
    private String allowedTypes;

    @Value("${upload.max-size:10485760}")
    private long maxSize;

    private final FileUploadMapper mapper;

    /** 禁止上传的文件扩展名（安全防护） */
    private static final Set<String> BLOCKED_EXTENSIONS = Set.of(
            "exe", "sh", "bat", "cmd", "js", "php", "jar", "py", "msi", "com", "vbs",
            "html", "htm", "svg", "jsp", "asp", "aspx"
    );

    public FileUploadService(FileUploadMapper mapper) {
        this.mapper = mapper;
    }

    /**
     * 上传文件
     * @param file 上传的文件
     * @param bizType 业务类型（如 product、inquiry、general）
     * @param bizId 关联业务 ID（可选）
     * @return 文件上传记录
     */
    public FileUpload upload(MultipartFile file, String bizType, Long bizId) {
        if (file.isEmpty()) {
            throw new BusinessException("文件不能为空");
        }
        if (file.getSize() > maxSize) {
            throw new BusinessException("文件大小超过限制（最大 " + (maxSize / 1024 / 1024) + "MB）");
        }

        String originalName = file.getOriginalFilename();
        String ext = getExtension(originalName).toLowerCase();

        // 安全检查：拦截危险文件类型
        if (BLOCKED_EXTENSIONS.contains(ext)) {
            throw new BusinessException("不允许上传此类型文件");
        }

        // 校验文件扩展名是否在白名单中
        Set<String> allowed = Set.of(allowedTypes.split(","));
        if (!allowed.contains(ext)) {
            throw new BusinessException("不支持的文件类型: " + ext);
        }

        // 生成唯一文件名：UUID + 按年月分目录
        String datePath = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy/MM"));
        String newFileName = UUID.randomUUID().toString().replace("-", "") + "." + ext;
        String relativePath = bizType + "/" + datePath;
        String fullPath = uploadPath + "/" + relativePath;

        // 创建目录
        File dir = new File(fullPath);
        if (!dir.exists()) dir.mkdirs();

        // 保存文件到磁盘
        try {
            file.transferTo(new File(dir, newFileName));
        } catch (IOException e) {
            throw new BusinessException("文件上传失败");
        }

        // 保存上传记录到数据库
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

    /** 提取文件扩展名 */
    private String getExtension(String filename) {
        if (filename == null || !filename.contains(".")) return "";
        return filename.substring(filename.lastIndexOf(".") + 1);
    }

    /** 分页查询上传记录（可按业务类型筛选） */
    public PageResult<FileUpload> list(int page, int size, String bizType) {
        LambdaQueryWrapper<FileUpload> wrapper = new LambdaQueryWrapper<>();
        if (bizType != null && !bizType.isEmpty()) {
            wrapper.eq(FileUpload::getBizType, bizType);
        }
        wrapper.orderByDesc(FileUpload::getCreatedAt);
        Page<FileUpload> p = mapper.selectPage(new Page<>(page, size), wrapper);
        return new PageResult<>(p.getRecords(), p.getTotal(), page, size);
    }

    /** 删除上传记录及物理文件 */
    public void delete(Long id) {
        FileUpload record = mapper.selectById(id);
        if (record == null) {
            throw new BusinessException("文件记录不存在");
        }
        // 删除物理文件
        String fullPath = uploadPath + record.getFileUrl().replaceFirst("^/uploads", "");
        File file = new File(fullPath);
        if (file.exists()) {
            file.delete();
        }
        mapper.deleteById(id);
    }
}
