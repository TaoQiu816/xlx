package com.xlx.api.product.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xlx.api.product.entity.ProductCategory;
import org.apache.ibatis.annotations.Mapper;

/** 产品分类数据访问接口 */
@Mapper
public interface ProductCategoryMapper extends BaseMapper<ProductCategory> {
}
