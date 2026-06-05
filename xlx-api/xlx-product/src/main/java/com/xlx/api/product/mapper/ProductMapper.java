package com.xlx.api.product.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xlx.api.product.entity.Product;
import org.apache.ibatis.annotations.Mapper;

/** 产品数据访问接口 */
@Mapper
public interface ProductMapper extends BaseMapper<Product> {
}
