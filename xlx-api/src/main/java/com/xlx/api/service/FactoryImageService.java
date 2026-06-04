package com.xlx.api.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xlx.api.common.BusinessException;
import com.xlx.api.common.PageResult;
import com.xlx.api.entity.FactoryImage;
import com.xlx.api.mapper.FactoryImageMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FactoryImageService {

    private final FactoryImageMapper mapper;

    public FactoryImageService(FactoryImageMapper mapper) {
        this.mapper = mapper;
    }

    public PageResult<FactoryImage> list(int page, int size) {
        Page<FactoryImage> p = mapper.selectPage(
                new Page<>(page, size),
                new LambdaQueryWrapper<FactoryImage>().orderByAsc(FactoryImage::getSortOrder)
        );
        return new PageResult<>(p.getRecords(), p.getTotal(), page, size);
    }

    public List<FactoryImage> listAll() {
        return mapper.selectList(
                new LambdaQueryWrapper<FactoryImage>()
                        .eq(FactoryImage::getStatus, 1)
                        .orderByAsc(FactoryImage::getSortOrder)
        );
    }

    public void create(FactoryImage entity) {
        mapper.insert(entity);
    }

    public void update(Long id, FactoryImage entity) {
        FactoryImage existing = mapper.selectById(id);
        if (existing == null) throw new BusinessException("记录不存在");
        entity.setId(id);
        mapper.updateById(entity);
    }

    public void delete(Long id) {
        mapper.deleteById(id);
    }
}
