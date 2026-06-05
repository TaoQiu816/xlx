package com.xlx.api.content.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xlx.api.common.BusinessException;
import com.xlx.api.common.PageResult;
import com.xlx.api.content.entity.FactoryImage;
import com.xlx.api.content.mapper.FactoryImageMapper;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 工厂图片服务。
 * <p>提供工厂展示图片的增删改查操作。</p>
 */
@Service
public class FactoryImageService {

    private final FactoryImageMapper mapper;

    public FactoryImageService(FactoryImageMapper mapper) {
        this.mapper = mapper;
    }

    /** 分页查询工厂图片 */
    public PageResult<FactoryImage> list(int page, int size) {
        Page<FactoryImage> p = mapper.selectPage(
                new Page<>(page, size),
                new LambdaQueryWrapper<FactoryImage>().orderByAsc(FactoryImage::getSortOrder)
        );
        return new PageResult<>(p.getRecords(), p.getTotal(), page, size);
    }

    /** 查询所有启用的工厂图片（用于前台展示） */
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
