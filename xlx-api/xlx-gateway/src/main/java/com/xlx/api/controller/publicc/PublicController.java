package com.xlx.api.controller.publicc;

import com.xlx.api.common.PageResult;
import com.xlx.api.common.Result;
import com.xlx.api.config.entity.SiteConfig;
import com.xlx.api.config.service.SiteConfigService;
import com.xlx.api.content.entity.Certificate;
import com.xlx.api.content.entity.FactoryImage;
import com.xlx.api.content.service.CertificateService;
import com.xlx.api.content.service.FactoryImageService;
import com.xlx.api.file.entity.FileUpload;
import com.xlx.api.file.service.FileUploadService;
import com.xlx.api.inquiry.entity.Inquiry;
import com.xlx.api.inquiry.service.InquiryService;
import com.xlx.api.product.entity.Product;
import com.xlx.api.product.entity.ProductCategory;
import com.xlx.api.product.service.ProductCategoryService;
import com.xlx.api.product.service.ProductService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.Valid;

import java.util.List;
import java.util.Map;

/**
 * 公开 API 聚合控制器（BFF 层）
 * 聚合各业务模块数据，为前台提供统一的公开接口
 * 所有接口无需鉴权，供前端展示网站使用
 */
@RestController
@RequestMapping("/public")
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

    /**
     * 获取网站配置（键值对映射）
     */
    @GetMapping("/config")
    public Result<Map<String, String>> config() {
        return Result.ok(configService.getConfigMap());
    }

    /**
     * 获取所有产品分类（树形结构由前端组装）
     */
    @GetMapping("/categories")
    public Result<List<ProductCategory>> categories() {
        return Result.ok(categoryService.listAll());
    }

    /**
     * 分页查询产品列表
     *
     * @param page       页码
     * @param size       每页数量
     * @param categoryId 分类 ID（可选）
     * @return 分页产品列表
     */
    @GetMapping("/products")
    public Result<PageResult<Product>> products(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(required = false) Long categoryId) {
        return Result.ok(productService.list(page, size, categoryId, 1));
    }

    /**
     * 获取推荐产品列表
     *
     * @param limit 数量限制，默认 8
     * @return 推荐产品列表
     */
    @GetMapping("/products/featured")
    public Result<List<Product>> featuredProducts(
            @RequestParam(defaultValue = "8") int limit) {
        return Result.ok(productService.listFeatured(limit));
    }

    /**
     * 获取产品详情（含规格和图片）
     *
     * @param slug 产品 URL 标识
     * @return 产品详情
     */
    @GetMapping("/products/{slug}")
    public Result<Map<String, Object>> productDetail(@PathVariable String slug) {
        Product product = productService.getBySlug(slug);
        return Result.ok(productService.getDetailById(product.getId()));
    }

    /**
     * 获取工厂图片列表
     */
    @GetMapping("/factory-images")
    public Result<List<FactoryImage>> factoryImages() {
        return Result.ok(factoryImageService.listAll());
    }

    /**
     * 获取证书列表
     */
    @GetMapping("/certificates")
    public Result<List<Certificate>> certificates() {
        return Result.ok(certificateService.listAll());
    }

    /**
     * 提交询盘
     *
     * @param inquiry 询盘信息
     * @return 操作结果
     */
    @PostMapping("/inquiries")
    public Result<Void> submitInquiry(@Valid @RequestBody Inquiry inquiry) {
        inquiryService.create(inquiry);
        return Result.ok();
    }

    /**
     * 上传文件（用于询盘附件等）
     *
     * @param file 上传的文件
     * @return 文件上传记录
     */
    @PostMapping("/upload")
    public Result<FileUpload> upload(@RequestParam("file") MultipartFile file) {
        return Result.ok(fileUploadService.upload(file, "inquiry", null));
    }
}
