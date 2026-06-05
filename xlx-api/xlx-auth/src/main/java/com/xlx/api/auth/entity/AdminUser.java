package com.xlx.api.auth.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.time.LocalDateTime;

/**
 * 管理员用户实体。
 * <p>对应数据库表 admin_user，存储后台管理员的账号信息。</p>
 */
@TableName("admin_user")
public class AdminUser {

    /** 主键 ID（自增） */
    @TableId(type = IdType.AUTO)
    private Long id;
    /** 登录用户名 */
    private String username;
    /** 登录密码（BCrypt 加密存储） */
    private String password;
    /** 显示昵称 */
    private String nickname;
    /** 角色标识：admin-管理员，editor-编辑 */
    private String role;
    /** 账号状态：1-启用，0-禁用 */
    private Integer status;
    /** 最后登录时间 */
    private LocalDateTime lastLoginAt;
    /** 创建时间 */
    private LocalDateTime createdAt;
    /** 更新时间 */
    private LocalDateTime updatedAt;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public String getNickname() { return nickname; }
    public void setNickname(String nickname) { this.nickname = nickname; }
    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
    public Integer getStatus() { return status; }
    public void setStatus(Integer status) { this.status = status; }
    public LocalDateTime getLastLoginAt() { return lastLoginAt; }
    public void setLastLoginAt(LocalDateTime lastLoginAt) { this.lastLoginAt = lastLoginAt; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}
