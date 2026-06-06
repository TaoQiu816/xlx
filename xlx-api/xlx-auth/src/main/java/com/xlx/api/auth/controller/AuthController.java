package com.xlx.api.auth.controller;

import com.xlx.api.common.PageResult;
import com.xlx.api.common.Result;
import com.xlx.api.auth.entity.AdminUser;
import com.xlx.api.auth.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 认证控制器。
 * <p>提供管理员登录和个人信息查询接口。</p>
 */
@RestController
@RequestMapping("/admin/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    /**
     * 管理员登录
     * @param body 请求体，包含 username 和 password
     * @return 登录结果（含 JWT 令牌）
     */
    @PostMapping("/login")
    public Result<Map<String, Object>> login(@RequestBody Map<String, String> body) {
        String username = body.get("username");
        String password = body.get("password");
        if (username == null || password == null) {
            return Result.error(400, "用户名和密码不能为空");
        }
        return Result.ok(authService.login(username, password));
    }

    /**
     * 获取当前登录管理员的个人信息
     * @param request HTTP 请求（通过拦截器注入 adminId 属性）
     * @return 管理员信息
     */
    @GetMapping("/profile")
    public Result<AdminUser> profile(HttpServletRequest request) {
        Long adminId = (Long) request.getAttribute("adminId");
        return Result.ok(authService.getProfile(adminId));
    }

    /**
     * 分页查询管理员列表
     */
    @GetMapping("/admins")
    public Result<PageResult<AdminUser>> listAdmins(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20") int size) {
        return Result.ok(authService.listAdmins(page, size));
    }

    /**
     * 创建管理员
     */
    @PostMapping("/admins")
    public Result<AdminUser> createAdmin(@RequestBody Map<String, Object> body) {
        String username = (String) body.get("username");
        String password = (String) body.get("password");
        String nickname = (String) body.get("nickname");
        String role = (String) body.get("role");
        if (username == null || password == null) {
            return Result.error(400, "用户名和密码不能为空");
        }
        return Result.ok(authService.createAdmin(username, password, nickname, role));
    }

    /**
     * 更新管理员信息
     */
    @PutMapping("/admins/{id}")
    public Result<AdminUser> updateAdmin(@PathVariable Long id, @RequestBody Map<String, Object> body) {
        String nickname = (String) body.get("nickname");
        String role = (String) body.get("role");
        Integer status = body.get("status") != null ? ((Number) body.get("status")).intValue() : null;
        return Result.ok(authService.updateAdmin(id, nickname, role, status));
    }

    /**
     * 修改管理员密码
     */
    @PutMapping("/admins/{id}/password")
    public Result<Void> changePassword(@PathVariable Long id, @RequestBody Map<String, String> body) {
        String password = body.get("password");
        if (password == null || password.length() < 6) {
            return Result.error(400, "密码不能少于6位");
        }
        authService.changePassword(id, password);
        return Result.ok(null);
    }

    /**
     * 删除管理员
     */
    @DeleteMapping("/admins/{id}")
    public Result<Void> deleteAdmin(@PathVariable Long id) {
        authService.deleteAdmin(id);
        return Result.ok(null);
    }
}
