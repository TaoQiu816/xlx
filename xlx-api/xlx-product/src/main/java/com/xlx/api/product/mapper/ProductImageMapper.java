package com.xlx.api.product.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xlx.api.product.entity.ProductImage;
import org.apache.ibatis.annotations.Mapper;

/** 产品图片数据访问接口 */
@Mapper
public interface ProductImageMapper extends BaseMapper<ProductImage> {
}
