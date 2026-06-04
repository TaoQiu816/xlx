package com.xlx.api.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.xlx.api.common.BusinessException;
import com.xlx.api.entity.ProductSpec;
import com.xlx.api.mapper.ProductSpecMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductSpecService {

    private final ProductSpecMapper mapper;

    public ProductSpecService(ProductSpecMapper mapper) {
        this.mapper = mapper;
    }

    public List<ProductSpec> listByProductId(Long productId) {
        return mapper.selectList(
                new LambdaQueryWrapper<ProductSpec>()
                        .eq(ProductSpec::getProductId, productId)
                        .orderByAsc(ProductSpec::getSortOrder)
        );
    }

    public void create(ProductSpec spec) {
        mapper.insert(spec);
    }

    public void update(Long id, ProductSpec spec) {
        ProductSpec existing = mapper.selectById(id);
        if (existing == null) throw new BusinessException("规格不存在");
        spec.setId(id);
        mapper.updateById(spec);
    }

    public void delete(Long id) {
        mapper.deleteById(id);
    }

    public void deleteByProductId(Long productId) {
        mapper.delete(new LambdaQueryWrapper<ProductSpec>().eq(ProductSpec::getProductId, productId));
    }
}
