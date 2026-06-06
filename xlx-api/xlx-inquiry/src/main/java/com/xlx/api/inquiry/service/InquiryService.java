package com.xlx.api.inquiry.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xlx.api.common.BusinessException;
import com.xlx.api.common.PageResult;
import com.xlx.api.inquiry.entity.Inquiry;
import com.xlx.api.inquiry.entity.InquiryItem;
import com.xlx.api.inquiry.mapper.InquiryMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

/**
 * 询盘服务。
 * <p>提供询盘的查询、创建、状态更新和备注管理。</p>
 */
@Service
public class InquiryService {

    /** 有效的询盘状态值 */
    private static final Set<String> VALID_STATUSES = Set.of(
            "new", "contacted", "quoted", "sample_confirmed",
            "order_confirmed", "closed", "invalid"
    );

    private final InquiryMapper mapper;
    private final InquiryItemService inquiryItemService;

    public InquiryService(InquiryMapper mapper, InquiryItemService inquiryItemService) {
        this.mapper = mapper;
        this.inquiryItemService = inquiryItemService;
    }

    /** 分页查询询盘（可按状态、关键词、日期范围筛选） */
    public PageResult<Inquiry> list(int page, int size, String status, String keyword,
                                     String startDate, String endDate) {
        LambdaQueryWrapper<Inquiry> wrapper = new LambdaQueryWrapper<>();
        if (status != null && !status.isEmpty()) {
            wrapper.eq(Inquiry::getStatus, status);
        }
        if (keyword != null && !keyword.isBlank()) {
            wrapper.and(w -> w
                    .like(Inquiry::getName, keyword)
                    .or().like(Inquiry::getCompany, keyword)
                    .or().like(Inquiry::getEmail, keyword)
                    .or().like(Inquiry::getProductName, keyword)
            );
        }
        if (startDate != null && !startDate.isEmpty()) {
            wrapper.ge(Inquiry::getCreatedAt, LocalDate.parse(startDate).atStartOfDay());
        }
        if (endDate != null && !endDate.isEmpty()) {
            wrapper.le(Inquiry::getCreatedAt, LocalDate.parse(endDate).plusDays(1).atStartOfDay());
        }
        wrapper.orderByDesc(Inquiry::getCreatedAt);
        Page<Inquiry> p = mapper.selectPage(new Page<>(page, size), wrapper);
        return new PageResult<>(p.getRecords(), p.getTotal(), page, size);
    }

    /** 根据 ID 获取询盘详情 */
    public Inquiry getById(Long id) {
        Inquiry inquiry = mapper.selectById(id);
        if (inquiry == null) throw new BusinessException("询盘不存在");
        return inquiry;
    }

    /** 创建询盘（前台提交，默认状态为 new） */
    @Transactional
    public void create(Inquiry inquiry) {
        inquiry.setStatus("new");
        mapper.insert(inquiry);
    }

    /** 创建多产品询盘（带 items 明细） */
    @Transactional
    public Inquiry createWithItems(Inquiry inquiry, List<InquiryItem> items) {
        inquiry.setStatus("new");
        inquiry.setProductId(null);
        inquiry.setProductName(null);
        inquiry.setQuantity(null);
        inquiry.setSpecification(null);
        mapper.insert(inquiry);
        if (items != null && !items.isEmpty()) {
            inquiryItemService.createBatch(inquiry.getId(), items);
        }
        return inquiry;
    }

    /** 获取询盘的产品明细列表 */
    public List<InquiryItem> getItems(Long inquiryId) {
        return inquiryItemService.listByInquiryId(inquiryId);
    }

    /** 更新询盘处理状态 */
    public void updateStatus(Long id, String status) {
        if (!VALID_STATUSES.contains(status)) {
            throw new BusinessException("无效的询盘状态: " + status);
        }
        Inquiry inquiry = getById(id);
        inquiry.setStatus(status);
        mapper.updateById(inquiry);
    }

    /** 更新后台备注 */
    public void updateRemark(Long id, String remark) {
        Inquiry inquiry = getById(id);
        inquiry.setRemark(remark);
        mapper.updateById(inquiry);
    }

    /** 删除询盘 */
    public void delete(Long id) {
        mapper.deleteById(id);
    }
}
