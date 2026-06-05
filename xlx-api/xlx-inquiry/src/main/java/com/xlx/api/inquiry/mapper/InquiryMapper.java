package com.xlx.api.inquiry.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xlx.api.inquiry.entity.Inquiry;
import org.apache.ibatis.annotations.Mapper;

/** 询盘数据访问接口 */
@Mapper
public interface InquiryMapper extends BaseMapper<Inquiry> {
}
