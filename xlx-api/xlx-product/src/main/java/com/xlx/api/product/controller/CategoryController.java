package com.xlx.api.product.controller;

import com.xlx.api.common.PageResult;
import com.xlx.api.common.Result;
import com.xlx.api.product.entity.ProductCategory;
import com.xlx.api.product.service.ProductCategoryService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 产品分类管理控制器（后台）。
 * <p>提供分类的增删改查接口，需要 JWT 鉴权。</p>
 */
@RestController
@RequestMapping("/admin/categories")
public class CategoryController {

    private final ProductCategoryService service;

    public CategoryController(ProductCategoryService service) {
        this.service = service;
    }

    /** 查询所有启用的分类 */
    @GetMapping
    public Result<List<ProductCategory>> list() {
        return Result.ok(service.listAll());
    }

    /** 分页查询分类 */
    @GetMapping("/page")
    public Result<PageResult<ProductCategory>> page(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20") int size) {
        return Result.ok(service.list(page, size));
    }

    /** 创建分类 */
    @PostMapping
    public Result<Void> create(@RequestBody ProductCategory entity) {
        service.create(entity);
        return Result.ok();
    }

    /** 更新分类 */
    @PutMapping("/{id}")
    public Result<Void> update(@PathVariable Long id, @RequestBody ProductCategory entity) {
        service.update(id, entity);
        return Result.ok();
    }

    /** 删除分类 */
    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return Result.ok();
    }
}
