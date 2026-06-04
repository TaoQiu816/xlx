package com.xlx.api.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xlx.api.common.BusinessException;
import com.xlx.api.common.PageResult;
import com.xlx.api.entity.ProductCategory;
import com.xlx.api.mapper.ProductCategoryMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductCategoryService {

    private final ProductCategoryMapper mapper;

    public ProductCategoryService(ProductCategoryMapper mapper) {
        this.mapper = mapper;
    }

    public PageResult<ProductCategory> list(int page, int size) {
        Page<ProductCategory> p = mapper.selectPage(
                new Page<>(page, size),
                new LambdaQueryWrapper<ProductCategory>().orderByAsc(ProductCategory::getSortOrder)
        );
        return new PageResult<>(p.getRecords(), p.getTotal(), page, size);
    }

    public List<ProductCategory> listAll() {
        return mapper.selectList(
                new LambdaQueryWrapper<ProductCategory>()
                        .eq(ProductCategory::getStatus, 1)
                        .orderByAsc(ProductCategory::getSortOrder)
        );
    }

    public ProductCategory getById(Long id) {
        ProductCategory entity = mapper.selectById(id);
        if (entity == null) throw new BusinessException("分类不存在");
        return entity;
    }

    public void create(ProductCategory entity) {
        mapper.insert(entity);
    }

    public void update(Long id, ProductCategory entity) {
        getById(id);
        entity.setId(id);
        mapper.updateById(entity);
    }

    public void delete(Long id) {
        getById(id);
        mapper.deleteById(id);
    }
}
