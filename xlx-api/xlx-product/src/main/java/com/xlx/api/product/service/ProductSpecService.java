package com.xlx.api.product.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.xlx.api.common.BusinessException;
import com.xlx.api.product.entity.ProductSpec;
import com.xlx.api.product.mapper.ProductSpecMapper;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 产品规格服务。
 * <p>提供规格参数的增删改查操作。</p>
 */
@Service
public class ProductSpecService {

    private final ProductSpecMapper mapper;

    public ProductSpecService(ProductSpecMapper mapper) {
        this.mapper = mapper;
    }

    /** 查询指定产品的所有规格（按排序升序） */
    public List<ProductSpec> listByProductId(Long productId) {
        return mapper.selectList(
                new LambdaQueryWrapper<ProductSpec>()
                        .eq(ProductSpec::getProductId, productId)
                        .orderByAsc(ProductSpec::getSortOrder)
        );
    }

    /** 创建规格 */
    public void create(ProductSpec spec) {
        mapper.insert(spec);
    }

    /** 更新规格 */
    public void update(Long id, ProductSpec spec) {
        ProductSpec existing = mapper.selectById(id);
        if (existing == null) throw new BusinessException("规格不存在");
        spec.setId(id);
        mapper.updateById(spec);
    }

    /** 删除单条规格 */
    public void delete(Long id) {
        mapper.deleteById(id);
    }

    /** 删除指定产品的所有规格（产品删除时级联调用） */
    public void deleteByProductId(Long productId) {
        mapper.delete(new LambdaQueryWrapper<ProductSpec>().eq(ProductSpec::getProductId, productId));
    }
}
