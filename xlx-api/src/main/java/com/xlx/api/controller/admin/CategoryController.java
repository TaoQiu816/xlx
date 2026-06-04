package com.xlx.api.controller.admin;

import com.xlx.api.common.PageResult;
import com.xlx.api.common.Result;
import com.xlx.api.entity.ProductCategory;
import com.xlx.api.service.ProductCategoryService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/categories")
public class CategoryController {

    private final ProductCategoryService service;

    public CategoryController(ProductCategoryService service) {
        this.service = service;
    }

    @GetMapping
    public Result<List<ProductCategory>> list() {
        return Result.ok(service.listAll());
    }

    @GetMapping("/page")
    public Result<PageResult<ProductCategory>> page(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20") int size) {
        return Result.ok(service.list(page, size));
    }

    @PostMapping
    public Result<Void> create(@RequestBody ProductCategory entity) {
        service.create(entity);
        return Result.ok();
    }

    @PutMapping("/{id}")
    public Result<Void> update(@PathVariable Long id, @RequestBody ProductCategory entity) {
        service.update(id, entity);
        return Result.ok();
    }

    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return Result.ok();
    }
}
