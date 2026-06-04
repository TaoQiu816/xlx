package com.xlx.api.controller.admin;

import com.xlx.api.common.PageResult;
import com.xlx.api.common.Result;
import com.xlx.api.entity.Product;
import com.xlx.api.entity.ProductSpec;
import com.xlx.api.entity.ProductImage;
import com.xlx.api.service.ProductService;
import com.xlx.api.service.ProductSpecService;
import com.xlx.api.service.ProductImageService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin/products")
public class ProductController {

    private final ProductService productService;
    private final ProductSpecService specService;
    private final ProductImageService imageService;

    public ProductController(ProductService productService, ProductSpecService specService, ProductImageService imageService) {
        this.productService = productService;
        this.specService = specService;
        this.imageService = imageService;
    }

    @GetMapping
    public Result<PageResult<Product>> list(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) Integer status) {
        return Result.ok(productService.list(page, size, categoryId, status));
    }

    @GetMapping("/{id}")
    public Result<Map<String, Object>> detail(@PathVariable Long id) {
        return Result.ok(productService.getDetailById(id));
    }

    @PostMapping
    public Result<Void> create(@RequestBody Product product) {
        productService.create(product);
        return Result.ok();
    }

    @PutMapping("/{id}")
    public Result<Void> update(@PathVariable Long id, @RequestBody Product product) {
        productService.update(id, product);
        return Result.ok();
    }

    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        productService.delete(id);
        return Result.ok();
    }

    // Specs
    @PostMapping("/{productId}/specs")
    public Result<Void> addSpec(@PathVariable Long productId, @RequestBody ProductSpec spec) {
        spec.setProductId(productId);
        specService.create(spec);
        return Result.ok();
    }

    @PutMapping("/specs/{id}")
    public Result<Void> updateSpec(@PathVariable Long id, @RequestBody ProductSpec spec) {
        specService.update(id, spec);
        return Result.ok();
    }

    @DeleteMapping("/specs/{id}")
    public Result<Void> deleteSpec(@PathVariable Long id) {
        specService.delete(id);
        return Result.ok();
    }

    // Images
    @GetMapping("/{productId}/images")
    public Result<List<ProductImage>> listImages(@PathVariable Long productId) {
        return Result.ok(imageService.listByProductId(productId));
    }

    @PostMapping("/{productId}/images")
    public Result<Void> addImage(@PathVariable Long productId, @RequestBody ProductImage image) {
        image.setProductId(productId);
        imageService.create(image);
        return Result.ok();
    }

    @DeleteMapping("/images/{id}")
    public Result<Void> deleteImage(@PathVariable Long id) {
        imageService.delete(id);
        return Result.ok();
    }
}
