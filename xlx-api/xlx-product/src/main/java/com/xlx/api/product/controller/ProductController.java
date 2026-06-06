package com.xlx.api.product.controller;

import com.xlx.api.common.PageResult;
import com.xlx.api.common.Result;
import com.xlx.api.product.entity.Product;
import com.xlx.api.product.entity.ProductSpec;
import com.xlx.api.product.entity.ProductImage;
import com.xlx.api.product.service.ProductService;
import com.xlx.api.product.service.ProductSpecService;
import com.xlx.api.product.service.ProductImageService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 产品管理控制器（后台）。
 * <p>提供产品 CRUD、规格管理、图片管理接口，需要 JWT 鉴权。</p>
 */
@RestController
@RequestMapping("/admin/products")
public class ProductController {

    private final ProductService productService;
    private final ProductSpecService specService;
    private final ProductImageService imageService;

    public ProductController(ProductService productService, ProductSpecService specService, ProductImageService imageService) {
        this.productService = productService;
        this.specService = specService;
        this.imageService = imageService;
    }

    /** 分页查询产品列表 */
    @GetMapping
    public Result<PageResult<Product>> list(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) Integer status,
            @RequestParam(required = false) String keyword) {
        return Result.ok(productService.list(page, size, categoryId, status, keyword));
    }

    /** 获取产品详情（含规格和图片） */
    @GetMapping("/{id}")
    public Result<Map<String, Object>> detail(@PathVariable Long id) {
        return Result.ok(productService.getDetailById(id));
    }

    /** 创建产品 */
    @PostMapping
    public Result<Void> create(@RequestBody Product product) {
        productService.create(product);
        return Result.ok();
    }

    /** 更新产品 */
    @PutMapping("/{id}")
    public Result<Void> update(@PathVariable Long id, @RequestBody Product product) {
        productService.update(id, product);
        return Result.ok();
    }

    /** 删除产品（级联删除规格和图片） */
    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        productService.delete(id);
        return Result.ok();
    }

    // ===== 规格管理 =====

    /** 为产品添加规格 */
    @PostMapping("/{productId}/specs")
    public Result<Void> addSpec(@PathVariable Long productId, @RequestBody ProductSpec spec) {
        spec.setProductId(productId);
        specService.create(spec);
        return Result.ok();
    }

    /** 更新规格 */
    @PutMapping("/specs/{id}")
    public Result<Void> updateSpec(@PathVariable Long id, @RequestBody ProductSpec spec) {
        specService.update(id, spec);
        return Result.ok();
    }

    /** 删除规格 */
    @DeleteMapping("/specs/{id}")
    public Result<Void> deleteSpec(@PathVariable Long id) {
        specService.delete(id);
        return Result.ok();
    }

    // ===== 图片管理 =====

    /** 查询产品的图片列表 */
    @GetMapping("/{productId}/images")
    public Result<List<ProductImage>> listImages(@PathVariable Long productId) {
        return Result.ok(imageService.listByProductId(productId));
    }

    /** 为产品添加图片 */
    @PostMapping("/{productId}/images")
    public Result<Void> addImage(@PathVariable Long productId, @RequestBody ProductImage image) {
        image.setProductId(productId);
        imageService.create(image);
        return Result.ok();
    }

    /** 删除图片 */
    @DeleteMapping("/images/{id}")
    public Result<Void> deleteImage(@PathVariable Long id) {
        imageService.delete(id);
        return Result.ok();
    }
}
