package com.xlx.api.content.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xlx.api.common.BusinessException;
import com.xlx.api.common.PageResult;
import com.xlx.api.content.entity.Certificate;
import com.xlx.api.content.mapper.CertificateMapper;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 证书服务。
 * <p>提供资质证书和检测报告的增删改查操作。</p>
 */
@Service
public class CertificateService {

    private final CertificateMapper mapper;

    public CertificateService(CertificateMapper mapper) {
        this.mapper = mapper;
    }

    /** 分页查询证书 */
    public PageResult<Certificate> list(int page, int size) {
        Page<Certificate> p = mapper.selectPage(
                new Page<>(page, size),
                new LambdaQueryWrapper<Certificate>().orderByAsc(Certificate::getSortOrder)
        );
        return new PageResult<>(p.getRecords(), p.getTotal(), page, size);
    }

    /** 查询所有启用的证书（用于前台展示） */
    public List<Certificate> listAll() {
        return mapper.selectList(
                new LambdaQueryWrapper<Certificate>()
                        .eq(Certificate::getStatus, 1)
                        .orderByAsc(Certificate::getSortOrder)
        );
    }

    public void create(Certificate entity) {
        mapper.insert(entity);
    }

    public void update(Long id, Certificate entity) {
        Certificate existing = mapper.selectById(id);
        if (existing == null) throw new BusinessException("证书不存在");
        entity.setId(id);
        mapper.updateById(entity);
    }

    public void delete(Long id) {
        mapper.deleteById(id);
    }
}
