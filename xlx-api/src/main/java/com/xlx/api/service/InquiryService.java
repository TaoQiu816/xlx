package com.xlx.api.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xlx.api.common.BusinessException;
import com.xlx.api.common.PageResult;
import com.xlx.api.entity.Inquiry;
import com.xlx.api.mapper.InquiryMapper;
import org.springframework.stereotype.Service;

@Service
public class InquiryService {

    private final InquiryMapper mapper;

    public InquiryService(InquiryMapper mapper) {
        this.mapper = mapper;
    }

    public PageResult<Inquiry> list(int page, int size, String status) {
        LambdaQueryWrapper<Inquiry> wrapper = new LambdaQueryWrapper<>();
        if (status != null && !status.isEmpty()) {
            wrapper.eq(Inquiry::getStatus, status);
        }
        wrapper.orderByDesc(Inquiry::getCreatedAt);
        Page<Inquiry> p = mapper.selectPage(new Page<>(page, size), wrapper);
        return new PageResult<>(p.getRecords(), p.getTotal(), page, size);
    }

    public Inquiry getById(Long id) {
        Inquiry inquiry = mapper.selectById(id);
        if (inquiry == null) throw new BusinessException("询盘不存在");
        return inquiry;
    }

    public void create(Inquiry inquiry) {
        inquiry.setStatus("new");
        mapper.insert(inquiry);
    }

    public void updateStatus(Long id, String status) {
        Inquiry inquiry = getById(id);
        inquiry.setStatus(status);
        mapper.updateById(inquiry);
    }

    public void updateRemark(Long id, String remark) {
        Inquiry inquiry = getById(id);
        inquiry.setRemark(remark);
        mapper.updateById(inquiry);
    }

    public void delete(Long id) {
        mapper.deleteById(id);
    }
}
