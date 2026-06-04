package com.xlx.api.controller.admin;

import com.xlx.api.common.Result;
import com.xlx.api.entity.AdminUser;
import com.xlx.api.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/admin/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public Result<Map<String, Object>> login(@RequestBody Map<String, String> body) {
        String username = body.get("username");
        String password = body.get("password");
        if (username == null || password == null) {
            return Result.error(400, "用户名和密码不能为空");
        }
        return Result.ok(authService.login(username, password));
    }

    @GetMapping("/profile")
    public Result<AdminUser> profile(HttpServletRequest request) {
        Long adminId = (Long) request.getAttribute("adminId");
        return Result.ok(authService.getProfile(adminId));
    }
}
