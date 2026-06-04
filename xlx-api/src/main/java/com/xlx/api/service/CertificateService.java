package com.xlx.api.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xlx.api.common.BusinessException;
import com.xlx.api.common.PageResult;
import com.xlx.api.entity.Certificate;
import com.xlx.api.mapper.CertificateMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CertificateService {

    private final CertificateMapper mapper;

    public CertificateService(CertificateMapper mapper) {
        this.mapper = mapper;
    }

    public PageResult<Certificate> list(int page, int size) {
        Page<Certificate> p = mapper.selectPage(
                new Page<>(page, size),
                new LambdaQueryWrapper<Certificate>().orderByAsc(Certificate::getSortOrder)
        );
        return new PageResult<>(p.getRecords(), p.getTotal(), page, size);
    }

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
