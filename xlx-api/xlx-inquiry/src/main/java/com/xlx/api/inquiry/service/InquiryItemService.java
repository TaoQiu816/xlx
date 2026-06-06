package com.xlx.api.inquiry.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.xlx.api.inquiry.entity.InquiryItem;
import com.xlx.api.inquiry.mapper.InquiryItemMapper;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 询盘产品明细服务。
 * <p>提供询盘明细的查询和批量创建。</p>
 */
@Service
public class InquiryItemService {

    private final InquiryItemMapper mapper;

    public InquiryItemService(InquiryItemMapper mapper) {
        this.mapper = mapper;
    }

    /** 查询询盘下的所有明细（按排序升序） */
    public List<InquiryItem> listByInquiryId(Long inquiryId) {
        return mapper.selectList(
                new LambdaQueryWrapper<InquiryItem>()
                        .eq(InquiryItem::getInquiryId, inquiryId)
                        .orderByAsc(InquiryItem::getSortOrder)
        );
    }

    /** 批量创建明细 */
    public void createBatch(Long inquiryId, List<InquiryItem> items) {
        for (int i = 0; i < items.size(); i++) {
            InquiryItem item = items.get(i);
            item.setInquiryId(inquiryId);
            item.setSortOrder(i);
            item.setDeleted(0);
            mapper.insert(item);
        }
    }

    /** 删除询盘下的所有明细 */
    public void deleteByInquiryId(Long inquiryId) {
        mapper.delete(
                new LambdaQueryWrapper<InquiryItem>()
                        .eq(InquiryItem::getInquiryId, inquiryId)
        );
    }
}
