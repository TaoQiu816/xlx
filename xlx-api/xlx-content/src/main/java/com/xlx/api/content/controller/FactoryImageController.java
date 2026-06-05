package com.xlx.api.content.controller;

import com.xlx.api.common.PageResult;
import com.xlx.api.common.Result;
import com.xlx.api.content.entity.FactoryImage;
import com.xlx.api.content.service.FactoryImageService;
import org.springframework.web.bind.annotation.*;

/**
 * 工厂图片管理控制器（后台）。
 * <p>提供工厂展示图片的增删改查接口，需要 JWT 鉴权。</p>
 */
@RestController
@RequestMapping("/admin/factory-images")
public class FactoryImageController {

    private final FactoryImageService service;

    public FactoryImageController(FactoryImageService service) {
        this.service = service;
    }

    @GetMapping
    public Result<PageResult<FactoryImage>> list(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20") int size) {
        return Result.ok(service.list(page, size));
    }

    @PostMapping
    public Result<Void> create(@RequestBody FactoryImage entity) {
        service.create(entity);
        return Result.ok();
    }

    @PutMapping("/{id}")
    public Result<Void> update(@PathVariable Long id, @RequestBody FactoryImage entity) {
        service.update(id, entity);
        return Result.ok();
    }

    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return Result.ok();
    }
}
