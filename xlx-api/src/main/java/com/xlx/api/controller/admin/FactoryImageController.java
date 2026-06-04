package com.xlx.api.controller.admin;

import com.xlx.api.common.PageResult;
import com.xlx.api.common.Result;
import com.xlx.api.entity.FactoryImage;
import com.xlx.api.service.FactoryImageService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/factory-images")
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
