package com.xlx.api.controller.publicc;

import com.xlx.api.common.PageResult;
import com.xlx.api.common.Result;
import com.xlx.api.entity.*;
import com.xlx.api.service.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/public")
public class PublicController {

    private final SiteConfigService configService;
    private final ProductCategoryService categoryService;
    private final ProductService productService;
    private final FactoryImageService factoryImageService;
    private final CertificateService certificateService;
    private final InquiryService inquiryService;
    private final FileUploadService fileUploadService;

    public PublicController(SiteConfigService configService,
                            ProductCategoryService categoryService,
                            ProductService productService,
                            FactoryImageService factoryImageService,
                            CertificateService certificateService,
                            InquiryService inquiryService,
                            FileUploadService fileUploadService) {
        this.configService = configService;
        this.categoryService = categoryService;
        this.productService = productService;
        this.factoryImageService = factoryImageService;
        this.certificateService = certificateService;
        this.inquiryService = inquiryService;
        this.fileUploadService = fileUploadService;
    }

    @GetMapping("/config")
    public Result<Map<String, String>> config() {
        return Result.ok(configService.getConfigMap());
    }

    @GetMapping("/categories")
    public Result<List<ProductCategory>> categories() {
        return Result.ok(categoryService.listAll());
    }

    @GetMapping("/products")
    public Result<PageResult<Product>> products(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(required = false) Long categoryId) {
        return Result.ok(productService.list(page, size, categoryId, 1));
    }

    @GetMapping("/products/featured")
    public Result<List<Product>> featuredProducts(
            @RequestParam(defaultValue = "8") int limit) {
        return Result.ok(productService.listFeatured(limit));
    }

    @GetMapping("/products/{slug}")
    public Result<Map<String, Object>> productDetail(@PathVariable String slug) {
        Product product = productService.getBySlug(slug);
        return Result.ok(productService.getDetailById(product.getId()));
    }

    @GetMapping("/factory-images")
    public Result<List<FactoryImage>> factoryImages() {
        return Result.ok(factoryImageService.listAll());
    }

    @GetMapping("/certificates")
    public Result<List<Certificate>> certificates() {
        return Result.ok(certificateService.listAll());
    }

    @PostMapping("/inquiries")
    public Result<Void> submitInquiry(@RequestBody Inquiry inquiry) {
        inquiryService.create(inquiry);
        return Result.ok();
    }

    @PostMapping("/upload")
    public Result<FileUpload> upload(@RequestParam("file") MultipartFile file) {
        return Result.ok(fileUploadService.upload(file, "inquiry", null));
    }
}
