package com.xlx.api.auth.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xlx.api.common.BusinessException;
import com.xlx.api.common.PageResult;
import com.xlx.api.auth.entity.AdminUser;
import com.xlx.api.auth.mapper.AdminUserMapper;
import com.xlx.api.util.JwtUtil;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 认证服务。
 * <p>处理管理员登录验证和用户信息查询。</p>
 */
@Service
public class AuthService {

    private final AdminUserMapper adminUserMapper;
    private final JwtUtil jwtUtil;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public AuthService(AdminUserMapper adminUserMapper, JwtUtil jwtUtil) {
        this.adminUserMapper = adminUserMapper;
        this.jwtUtil = jwtUtil;
    }

    /**
     * 管理员登录
     * @param username 用户名
     * @param password 密码（明文）
     * @return 包含 token、username、nickname、role 的 Map
     * @throws BusinessException 用户名或密码错误时抛出
     */
    public Map<String, Object> login(String username, String password) {
        AdminUser user = adminUserMapper.selectOne(
                new LambdaQueryWrapper<AdminUser>()
                        .eq(AdminUser::getUsername, username)
                        .eq(AdminUser::getStatus, 1)
        );
        if (user == null) {
            throw new BusinessException("用户名或密码错误");
        }
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new BusinessException("用户名或密码错误");
        }

        // 更新最后登录时间
        AdminUser update = new AdminUser();
        update.setId(user.getId());
        update.setLastLoginAt(LocalDateTime.now());
        adminUserMapper.updateById(update);

        String token = jwtUtil.generateToken(user.getId(), user.getUsername());

        Map<String, Object> result = new HashMap<>();
        result.put("token", token);
        result.put("username", user.getUsername());
        result.put("nickname", user.getNickname());
        result.put("role", user.getRole());
        return result;
    }

    /**
     * 获取管理员个人信息
     * @param adminId 管理员 ID
     * @return 管理员实体（密码字段已置空）
     * @throws BusinessException 用户不存在时抛出
     */
    public AdminUser getProfile(Long adminId) {
        AdminUser user = adminUserMapper.selectById(adminId);
        if (user == null) {
            throw new BusinessException("用户不存在");
        }
        user.setPassword(null); // 不返回密码
        return user;
    }

    /**
     * 分页查询管理员列表
     */
    public PageResult<AdminUser> listAdmins(int page, int size) {
        Page<AdminUser> pageObj = adminUserMapper.selectPage(
                new Page<>(page, size),
                new LambdaQueryWrapper<AdminUser>().orderByDesc(AdminUser::getCreatedAt)
        );
        List<AdminUser> records = pageObj.getRecords();
        records.forEach(u -> u.setPassword(null));
        return new PageResult<>(records, pageObj.getTotal(), page, size);
    }

    /**
     * 创建管理员
     */
    public AdminUser createAdmin(String username, String password, String nickname, String role) {
        long count = adminUserMapper.selectCount(
                new LambdaQueryWrapper<AdminUser>().eq(AdminUser::getUsername, username)
        );
        if (count > 0) {
            throw new BusinessException("用户名已存在");
        }
        AdminUser user = new AdminUser();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password));
        user.setNickname(nickname != null ? nickname : username);
        user.setRole(role != null ? role : "editor");
        user.setStatus(1);
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());
        adminUserMapper.insert(user);
        user.setPassword(null);
        return user;
    }

    /**
     * 更新管理员信息（不含密码）
     */
    public AdminUser updateAdmin(Long id, String nickname, String role, Integer status) {
        AdminUser user = adminUserMapper.selectById(id);
        if (user == null) {
            throw new BusinessException("管理员不存在");
        }
        if (nickname != null) user.setNickname(nickname);
        if (role != null) user.setRole(role);
        if (status != null) user.setStatus(status);
        user.setUpdatedAt(LocalDateTime.now());
        adminUserMapper.updateById(user);
        user.setPassword(null);
        return user;
    }

    /**
     * 修改管理员密码
     */
    public void changePassword(Long id, String newPassword) {
        AdminUser user = adminUserMapper.selectById(id);
        if (user == null) {
            throw new BusinessException("管理员不存在");
        }
        AdminUser update = new AdminUser();
        update.setId(id);
        update.setPassword(passwordEncoder.encode(newPassword));
        update.setUpdatedAt(LocalDateTime.now());
        adminUserMapper.updateById(update);
    }

    /**
     * 删除管理员
     */
    public void deleteAdmin(Long id) {
        AdminUser user = adminUserMapper.selectById(id);
        if (user == null) {
            throw new BusinessException("管理员不存在");
        }
        if ("admin".equals(user.getUsername())) {
            throw new BusinessException("不能删除默认管理员");
        }
        adminUserMapper.deleteById(id);
    }
}
