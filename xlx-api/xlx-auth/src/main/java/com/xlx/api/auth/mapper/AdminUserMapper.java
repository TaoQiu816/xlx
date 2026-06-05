package com.xlx.api.auth.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xlx.api.auth.entity.AdminUser;
import org.apache.ibatis.annotations.Mapper;

/**
 * 管理员用户数据访问接口。
 * <p>继承 MyBatis Plus 的 BaseMapper，提供基础 CRUD 操作。</p>
 */
@Mapper
public interface AdminUserMapper extends BaseMapper<AdminUser> {
}
