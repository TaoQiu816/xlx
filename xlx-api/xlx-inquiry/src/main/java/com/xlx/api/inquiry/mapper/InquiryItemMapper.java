package com.xlx.api.inquiry.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xlx.api.inquiry.entity.InquiryItem;
import org.apache.ibatis.annotations.Mapper;

/** 询盘产品明细数据访问接口 */
@Mapper
public interface InquiryItemMapper extends BaseMapper<InquiryItem> {
}
