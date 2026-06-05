package com.xlx.api.file.controller;

import com.xlx.api.common.PageResult;
import com.xlx.api.common.Result;
import com.xlx.api.file.entity.FileUpload;
import com.xlx.api.file.service.FileUploadService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * 文件上传控制器（后台）。
 * <p>提供文件上传、列表查询和删除接口，需要 JWT 鉴权。</p>
 */
@RestController
@RequestMapping("/admin/upload")
public class UploadController {

    private final FileUploadService service;

    public UploadController(FileUploadService service) {
        this.service = service;
    }

    /**
     * 上传文件
     * @param file 文件
     * @param bizType 业务类型（默认 general）
     * @param bizId 关联业务 ID（可选）
     * @return 文件上传记录
     */
    @PostMapping
    public Result<FileUpload> upload(
            @RequestParam("file") MultipartFile file,
            @RequestParam(value = "bizType", defaultValue = "general") String bizType,
            @RequestParam(value = "bizId", required = false) Long bizId) {
        return Result.ok(service.upload(file, bizType, bizId));
    }

    /**
     * 分页查询上传记录
     * @param page 页码
     * @param size 每页数量
     * @param bizType 业务类型筛选（可选）
     * @return 分页上传记录列表
     */
    @GetMapping
    public Result<PageResult<FileUpload>> list(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(required = false) String bizType) {
        return Result.ok(service.list(page, size, bizType));
    }

    /**
     * 删除上传记录及物理文件
     * @param id 记录 ID
     * @return 操作结果
     */
    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return Result.ok();
    }
}
