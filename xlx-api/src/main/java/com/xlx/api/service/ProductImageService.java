package com.xlx.api.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.xlx.api.common.BusinessException;
import com.xlx.api.entity.ProductImage;
import com.xlx.api.mapper.ProductImageMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductImageService {

    private final ProductImageMapper mapper;

    public ProductImageService(ProductImageMapper mapper) {
        this.mapper = mapper;
    }

    public List<ProductImage> listByProductId(Long productId) {
        return mapper.selectList(
                new LambdaQueryWrapper<ProductImage>()
                        .eq(ProductImage::getProductId, productId)
                        .orderByAsc(ProductImage::getSortOrder)
        );
    }

    public void create(ProductImage image) {
        mapper.insert(image);
    }

    public void delete(Long id) {
        ProductImage existing = mapper.selectById(id);
        if (existing == null) throw new BusinessException("图片不存在");
        mapper.deleteById(id);
    }
}
