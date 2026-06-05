package com.xlx.api.product.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.xlx.api.common.BusinessException;
import com.xlx.api.product.entity.ProductImage;
import com.xlx.api.product.mapper.ProductImageMapper;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 产品图片服务。
 * <p>提供产品附加图片的增删查操作。</p>
 */
@Service
public class ProductImageService {

    private final ProductImageMapper mapper;

    public ProductImageService(ProductImageMapper mapper) {
        this.mapper = mapper;
    }

    /** 查询指定产品的所有图片（按排序升序） */
    public List<ProductImage> listByProductId(Long productId) {
        return mapper.selectList(
                new LambdaQueryWrapper<ProductImage>()
                        .eq(ProductImage::getProductId, productId)
                        .orderByAsc(ProductImage::getSortOrder)
        );
    }

    /** 创建图片记录 */
    public void create(ProductImage image) {
        mapper.insert(image);
    }

    /** 删除图片记录 */
    public void delete(Long id) {
        ProductImage existing = mapper.selectById(id);
        if (existing == null) throw new BusinessException("图片不存在");
        mapper.deleteById(id);
    }
}
