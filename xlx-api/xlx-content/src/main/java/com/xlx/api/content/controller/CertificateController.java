package com.xlx.api.content.controller;

import com.xlx.api.common.PageResult;
import com.xlx.api.common.Result;
import com.xlx.api.content.entity.Certificate;
import com.xlx.api.content.service.CertificateService;
import org.springframework.web.bind.annotation.*;

/**
 * 证书管理控制器（后台）。
 * <p>提供资质证书的增删改查接口，需要 JWT 鉴权。</p>
 */
@RestController
@RequestMapping("/admin/certificates")
public class CertificateController {

    private final CertificateService service;

    public CertificateController(CertificateService service) {
        this.service = service;
    }

    @GetMapping
    public Result<PageResult<Certificate>> list(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20") int size) {
        return Result.ok(service.list(page, size));
    }

    @PostMapping
    public Result<Void> create(@RequestBody Certificate entity) {
        service.create(entity);
        return Result.ok();
    }

    @PutMapping("/{id}")
    public Result<Void> update(@PathVariable Long id, @RequestBody Certificate entity) {
        service.update(id, entity);
        return Result.ok();
    }

    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return Result.ok();
    }
}
