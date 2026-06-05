package com.xlx.api.config;

import com.xlx.api.auth.config.AuthInterceptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Web MVC 全局配置
 * 配置跨域（CORS）、静态资源映射、JWT 拦截器注册
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Value("${upload.path:./uploads}")
    private String uploadPath;

    @Value("${cors.allowed-origins:http://localhost:3000,http://localhost:3001}")
    private String allowedOrigins;

    private final AuthInterceptor authInterceptor;

    public WebMvcConfig(AuthInterceptor authInterceptor) {
        this.authInterceptor = authInterceptor;
    }

    /**
     * 配置跨域规则
     * 通过 cors.allowed-origins 环境变量控制，开发环境默认 localhost:3000/3001
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        String[] origins = allowedOrigins.split(",");
        registry.addMapping("/api/**")
                .allowedOriginPatterns(origins)
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .allowCredentials(true)
                .maxAge(3600);
    }

    /**
     * 配置静态资源映射
     * 将 /uploads/** 路径映射到本地上传目录
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/uploads/**")
                .addResourceLocations("file:" + uploadPath + "/");
    }

    /**
     * 注册 JWT 认证拦截器
     * 拦截 /api/admin/** 请求，排除登录接口
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(authInterceptor)
                .addPathPatterns("/admin/**")
                .excludePathPatterns("/admin/auth/login");
    }
}
