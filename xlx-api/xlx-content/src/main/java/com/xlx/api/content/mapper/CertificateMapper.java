package com.xlx.api.content.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xlx.api.content.entity.Certificate;
import org.apache.ibatis.annotations.Mapper;

/** 证书数据访问接口 */
@Mapper
public interface CertificateMapper extends BaseMapper<Certificate> {
}
