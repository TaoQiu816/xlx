# xlx-auth — 认证模块

## 职责

管理员身份认证：登录验证、JWT Token 签发与校验、请求拦截鉴权。

## 包名

`com.xlx.api.auth`

## 文件清单

| 文件 | 说明 |
|------|------|
| `entity/AdminUser.java` | 管理员实体（对应 admin_user 表） |
| `mapper/AdminUserMapper.java` | 管理员数据访问层 |
| `service/AuthService.java` | 认证服务（登录验证、BCrypt 密码校验、Token 签发） |
| `controller/AuthController.java` | 认证接口 |
| `config/AuthInterceptor.java` | JWT 拦截器（校验 Token、注入 adminId 到请求属性） |

## API 接口

### POST /api/admin/auth/login — 管理员登录

```bash
curl -X POST http://localhost:8080/api/admin/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"admin","password":"admin123"}'
```

响应：
```json
{
  "code": 200,
  "message": "success",
  "data": {
    "token": "eyJhbGciOiJIUzI1NiJ9...",
    "username": "admin",
    "nickname": "管理员",
    "role": "admin"
  }
}
```

### GET /api/admin/auth/profile — 获取当前管理员信息

```bash
curl -H "Authorization: Bearer YOUR_TOKEN" \
  http://localhost:8080/api/admin/auth/profile
```

响应：
```json
{
  "code": 200,
  "message": "success",
  "data": {
    "id": 1,
    "username": "admin",
    "nickname": "管理员",
    "role": "admin",
    "status": 1,
    "password": null
  }
}
```

## 鉴权机制

1. 用户调用 `/api/admin/auth/login` 获取 JWT Token
2. 后续请求在 Header 中携带 `Authorization: Bearer <token>`
3. `AuthInterceptor` 拦截所有 `/api/admin/**` 请求（排除登录接口）
4. 校验 Token 有效性，将 `adminId` 注入 `request.setAttribute("adminId", id)`
5. Token 过期时间：24 小时

## 验证方法

```bash
# 1. 编译验证
mvn clean compile -pl xlx-auth -am

# 2. 登录测试
curl -X POST http://localhost:8080/api/admin/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"admin","password":"admin123"}'

# 3. Token 校验测试（应返回 401）
curl http://localhost:8080/api/admin/auth/profile

# 4. 错误密码测试
curl -X POST http://localhost:8080/api/admin/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"admin","password":"wrong"}'
```

## 依赖

- `xlx-common`（Result、BusinessException、JwtUtil）
