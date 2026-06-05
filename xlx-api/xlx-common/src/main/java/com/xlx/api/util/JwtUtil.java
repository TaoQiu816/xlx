package com.xlx.api.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

/**
 * JWT 工具类。
 * <p>负责 JWT 令牌的生成、解析和验证。
 * 使用 HMAC-SHA 签名算法，密钥和过期时间通过配置文件注入。</p>
 */
@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private long expiration;

    /** 生成 HMAC-SHA 签名密钥 */
    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * 生成 JWT 令牌
     * @param adminId 管理员 ID（作为 subject）
     * @param username 用户名（作为自定义 claim）
     * @return JWT 令牌字符串
     */
    public String generateToken(Long adminId, String username) {
        return Jwts.builder()
                .subject(String.valueOf(adminId))
                .claim("username", username)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getSigningKey())
                .compact();
    }

    /**
     * 解析 JWT 令牌，返回 Claims
     * @param token JWT 令牌
     * @return 解析后的 Claims 对象
     */
    public Claims parseToken(String token) {
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    /**
     * 从令牌中提取管理员 ID
     * @param token JWT 令牌
     * @return 管理员 ID
     */
    public Long getAdminId(String token) {
        return Long.parseLong(parseToken(token).getSubject());
    }

    /**
     * 验证令牌是否有效（签名正确且未过期）
     * @param token JWT 令牌
     * @return true 表示有效
     */
    public boolean isTokenValid(String token) {
        try {
            Claims claims = parseToken(token);
            return !claims.getExpiration().before(new Date());
        } catch (Exception e) {
            return false;
        }
    }
}
