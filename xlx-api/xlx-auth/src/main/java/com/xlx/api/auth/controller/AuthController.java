package com.xlx.api.auth.controller;

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
}
