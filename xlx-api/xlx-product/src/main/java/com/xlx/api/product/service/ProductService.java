package com.xlx.api.product.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xlx.api.common.BusinessException;
import com.xlx.api.common.PageResult;
import com.xlx.api.product.entity.Product;
import com.xlx.api.product.entity.ProductSpec;
import com.xlx.api.product.entity.ProductImage;
import com.xlx.api.product.mapper.ProductMapper;
import com.xlx.api.product.mapper.ProductSpecMapper;
import com.xlx.api.product.mapper.ProductImageMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 产品服务。
 * <p>提供产品的增删改查、详情查询（含规格和图片）、推荐产品等功能。</p>
 */
@Service
public class ProductService {

    private final ProductMapper productMapper;
    private final ProductSpecMapper specMapper;
    private final ProductImageMapper imageMapper;

    public ProductService(ProductMapper productMapper, ProductSpecMapper specMapper, ProductImageMapper imageMapper) {
        this.productMapper = productMapper;
        this.specMapper = specMapper;
        this.imageMapper = imageMapper;
    }

    /** 分页查询产品列表（可按分类和状态筛选） */
    public PageResult<Product> list(int page, int size, Long categoryId, Integer status) {
        LambdaQueryWrapper<Product> wrapper = new LambdaQueryWrapper<>();
        if (categoryId != null) wrapper.eq(Product::getCategoryId, categoryId);
        if (status != null) wrapper.eq(Product::getStatus, status);
        wrapper.orderByAsc(Product::getSortOrder);
        Page<Product> p = productMapper.selectPage(new Page<>(page, size), wrapper);
        return new PageResult<>(p.getRecords(), p.getTotal(), page, size);
    }

    /** 查询推荐产品（用于首页展示） */
    public List<Product> listFeatured(int limit) {
        return productMapper.selectList(
                new LambdaQueryWrapper<Product>()
                        .eq(Product::getIsFeatured, 1)
                        .eq(Product::getStatus, 1)
                        .orderByAsc(Product::getSortOrder)
                        .last("LIMIT " + limit)
        );
    }

    /** 根据 ID 获取产品 */
    public Product getById(Long id) {
        Product product = productMapper.selectById(id);
        if (product == null) throw new BusinessException("产品不存在");
        return product;
    }

    /** 获取产品详情（含规格和图片） */
    public Map<String, Object> getDetailById(Long id) {
        Product product = getById(id);
        List<ProductSpec> specs = specMapper.selectList(
                new LambdaQueryWrapper<ProductSpec>()
                        .eq(ProductSpec::getProductId, id)
                        .orderByAsc(ProductSpec::getSortOrder)
        );
        List<ProductImage> images = imageMapper.selectList(
                new LambdaQueryWrapper<ProductImage>()
                        .eq(ProductImage::getProductId, id)
                        .orderByAsc(ProductImage::getSortOrder)
        );
        Map<String, Object> result = new HashMap<>();
        result.put("product", product);
        result.put("specs", specs);
        result.put("images", images);
        return result;
    }

    /** 根据 slug 获取产品（用于前端详情页） */
    public Product getBySlug(String slug) {
        Product product = productMapper.selectOne(
                new LambdaQueryWrapper<Product>().eq(Product::getSlug, slug)
        );
        if (product == null) throw new BusinessException("产品不存在");
        return product;
    }

    /** 创建产品 */
    @Transactional
    public void create(Product product) {
        productMapper.insert(product);
    }

    /** 更新产品 */
    @Transactional
    public void update(Long id, Product product) {
        getById(id);
        product.setId(id);
        productMapper.updateById(product);
    }

    /** 删除产品（级联删除关联的规格和图片） */
    @Transactional
    public void delete(Long id) {
        getById(id);
        productMapper.deleteById(id);
        specMapper.delete(new LambdaQueryWrapper<ProductSpec>().eq(ProductSpec::getProductId, id));
        imageMapper.delete(new LambdaQueryWrapper<ProductImage>().eq(ProductImage::getProductId, id));
    }
}
