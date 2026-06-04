package com.xlx.api.controller.admin;

import com.xlx.api.common.Result;
import com.xlx.api.entity.FileUpload;
import com.xlx.api.service.FileUploadService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/admin/upload")
public class UploadController {

    private final FileUploadService service;

    public UploadController(FileUploadService service) {
        this.service = service;
    }

    @PostMapping
    public Result<FileUpload> upload(
            @RequestParam("file") MultipartFile file,
            @RequestParam(value = "bizType", defaultValue = "general") String bizType,
            @RequestParam(value = "bizId", required = false) Long bizId) {
        return Result.ok(service.upload(file, bizType, bizId));
    }
}
