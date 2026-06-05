package com.xlx.api.product.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xlx.api.common.BusinessException;
import com.xlx.api.common.PageResult;
import com.xlx.api.product.entity.ProductCategory;
import com.xlx.api.product.mapper.ProductCategoryMapper;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 产品分类服务。
 * <p>提供分类的增删改查操作。</p>
 */
@Service
public class ProductCategoryService {

    private final ProductCategoryMapper mapper;

    public ProductCategoryService(ProductCategoryMapper mapper) {
        this.mapper = mapper;
    }

    /** 分页查询分类列表 */
    public PageResult<ProductCategory> list(int page, int size) {
        Page<ProductCategory> p = mapper.selectPage(
                new Page<>(page, size),
                new LambdaQueryWrapper<ProductCategory>().orderByAsc(ProductCategory::getSortOrder)
        );
        return new PageResult<>(p.getRecords(), p.getTotal(), page, size);
    }

    /** 查询所有启用的分类（用于下拉选择） */
    public List<ProductCategory> listAll() {
        return mapper.selectList(
                new LambdaQueryWrapper<ProductCategory>()
                        .eq(ProductCategory::getStatus, 1)
                        .orderByAsc(ProductCategory::getSortOrder)
        );
    }

    /** 根据 ID 获取分类 */
    public ProductCategory getById(Long id) {
        ProductCategory entity = mapper.selectById(id);
        if (entity == null) throw new BusinessException("分类不存在");
        return entity;
    }

    /** 创建分类 */
    public void create(ProductCategory entity) {
        mapper.insert(entity);
    }

    /** 更新分类 */
    public void update(Long id, ProductCategory entity) {
        getById(id);
        entity.setId(id);
        mapper.updateById(entity);
    }

    /** 删除分类 */
    public void delete(Long id) {
        getById(id);
        mapper.deleteById(id);
    }
}
