package com.xlx.api.auth.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.xlx.api.auth.entity.AdminUser;
import com.xlx.api.auth.mapper.AdminUserMapper;
import com.xlx.api.common.BusinessException;
import com.xlx.api.util.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("AuthService 单元测试")
class AuthServiceTest {

    @Mock
    private AdminUserMapper adminUserMapper;

    @InjectMocks
    private AuthService authService;

    private AdminUser sampleUser;
    private String encodedPassword;

    @BeforeEach
    void setUp() {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        encodedPassword = encoder.encode("admin123");

        sampleUser = new AdminUser();
        sampleUser.setId(1L);
        sampleUser.setUsername("admin");
        sampleUser.setPassword(encodedPassword);
        sampleUser.setNickname("管理员");
        sampleUser.setRole("admin");
        sampleUser.setStatus(1);
    }

    @Test
    @DisplayName("login — 用户不存在抛出异常")
    void login_nonExistingUser_throwsBusinessException() {
        when(adminUserMapper.selectOne(any(LambdaQueryWrapper.class))).thenReturn(null);

        assertThatThrownBy(() -> authService.login("nonexistent", "password"))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining("用户名或密码错误");
    }

    @Test
    @DisplayName("login — 密码错误抛出异常")
    void login_invalidPassword_throwsBusinessException() {
        when(adminUserMapper.selectOne(any(LambdaQueryWrapper.class))).thenReturn(sampleUser);

        assertThatThrownBy(() -> authService.login("admin", "wrongpassword"))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining("用户名或密码错误");
    }

    @Test
    @DisplayName("getProfile — 存在时返回用户信息（密码置空）")
    void getProfile_existingUser_returnsUser() {
        when(adminUserMapper.selectById(1L)).thenReturn(sampleUser);

        AdminUser result = authService.getProfile(1L);

        assertThat(result.getUsername()).isEqualTo("admin");
        assertThat(result.getPassword()).isNull();
    }

    @Test
    @DisplayName("getProfile — 不存在时抛出异常")
    void getProfile_nonExisting_throwsBusinessException() {
        when(adminUserMapper.selectById(99L)).thenReturn(null);

        assertThatThrownBy(() -> authService.getProfile(99L))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining("用户不存在");
    }
}
