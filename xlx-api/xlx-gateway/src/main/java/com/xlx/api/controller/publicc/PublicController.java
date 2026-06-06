package com.xlx.api.controller.publicc;

import com.xlx.api.common.BusinessException;
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
import com.xlx.api.inquiry.entity.InquiryItem;
import com.xlx.api.inquiry.service.InquiryService;
import com.xlx.api.product.entity.Product;
import com.xlx.api.product.entity.ProductCategory;
import com.xlx.api.product.service.ProductCategoryService;
import com.xlx.api.product.service.ProductService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.ArrayList;
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
     */
    @GetMapping("/products")
    public Result<PageResult<Product>> products(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String sort) {
        return Result.ok(productService.listPublic(page, size, categoryId, keyword, sort));
    }

    /**
     * 获取推荐产品列表
     */
    @GetMapping("/products/featured")
    public Result<List<Product>> featuredProducts(
            @RequestParam(defaultValue = "8") int limit) {
        return Result.ok(productService.listFeatured(limit));
    }

    /**
     * 获取产品详情（含规格和图片）
     */
    @GetMapping("/products/{slug}")
    public Result<Map<String, Object>> productDetail(@PathVariable String slug) {
        Product product = productService.getBySlug(slug);
        Map<String, Object> detail = productService.getDetailById(product.getId());
        detail.put("relatedProducts", productService.listRelated(
                product.getCategoryId(), product.getId(), 4));
        return Result.ok(detail);
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
     * 提交询盘（支持单产品和多产品）
     * <p>如果 items 非空，创建 inquiry 主记录 + inquiry_item 明细。
     * 如果 items 为空且 productId 非空，走原有单品询盘逻辑。</p>
     */
    @PostMapping("/inquiries")
    public Result<Map<String, Object>> submitInquiry(@Valid @RequestBody InquirySubmitRequest request) {
        Inquiry inquiry = new Inquiry();
        inquiry.setName(request.name);
        inquiry.setCompany(request.company);
        inquiry.setPhone(request.phone);
        inquiry.setEmail(request.email);
        inquiry.setWechat(request.wechat);
        inquiry.setWhatsapp(request.whatsapp);
        inquiry.setCountry(request.country);
        inquiry.setMessage(request.message);
        inquiry.setFileUrl(request.fileUrl);

        if (request.items != null && !request.items.isEmpty()) {
            // 多产品询盘
            if (request.items.size() > 20) {
                throw new BusinessException("产品数量不能超过 20 个");
            }
            List<InquiryItem> items = new ArrayList<>();
            for (CartItemDTO cartItem : request.items) {
                InquiryItem item = new InquiryItem();
                item.setProductId(cartItem.productId);
                item.setProductNameCn(cartItem.productNameCn);
                item.setProductNameEn(cartItem.productNameEn != null ? cartItem.productNameEn : "");
                item.setQuantity(cartItem.quantity != null ? cartItem.quantity : "");
                item.setSpecification(cartItem.specification != null ? cartItem.specification : "");
                items.add(item);
            }
            Inquiry saved = inquiryService.createWithItems(inquiry, items);
            return Result.ok(Map.of("id", saved.getId()));
        } else {
            // 单品询盘（兼容原逻辑）
            inquiry.setProductId(request.productId);
            inquiry.setProductName(request.productName);
            inquiry.setQuantity(request.quantity);
            inquiry.setSpecification(request.specification);
            inquiryService.create(inquiry);
            return Result.ok(Map.of("id", inquiry.getId()));
        }
    }

    /** 询盘提交请求体 */
    public static class InquirySubmitRequest {
        @NotBlank(message = "客户姓名不能为空")
        public String name;
        public String company;
        public String phone;
        @Email(message = "邮箱格式不正确")
        public String email;
        public String wechat;
        public String whatsapp;
        public String country;
        public String message;
        public String fileUrl;
        // 单品询盘字段（兼容）
        public Long productId;
        public String productName;
        public String quantity;
        public String specification;
        // 多产品询盘
        @Size(max = 20, message = "产品数量不能超过 20 个")
        public List<CartItemDTO> items;
    }

    /** 询价车产品项 */
    public static class CartItemDTO {
        public Long productId;
        @NotBlank(message = "产品名称不能为空")
        @Size(max = 200, message = "产品名称不能超过 200 个字符")
        public String productNameCn;
        @Size(max = 200, message = "英文产品名称不能超过 200 个字符")
        public String productNameEn;
        @Size(max = 100, message = "数量不能超过 100 个字符")
        public String quantity;
        @Size(max = 500, message = "规格说明不能超过 500 个字符")
        public String specification;
    }

    /**
     * 上传文件（用于询盘附件等）
     */
    @PostMapping("/upload")
    public Result<FileUpload> upload(@RequestParam("file") MultipartFile file) {
        return Result.ok(fileUploadService.upload(file, "inquiry", null));
    }
}
