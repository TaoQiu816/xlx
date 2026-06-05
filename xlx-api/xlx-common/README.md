# xlx-common — 公共库模块

## 职责

提供所有模块共用的基础类和工具，是整个项目的最底层依赖。所有其他模块都依赖本模块。

## 包名

- `com.xlx.api.common` — 公共类（响应封装、异常处理）
- `com.xlx.api.util` — 工具类（JWT）
- `com.xlx.api.config` — 公共配置（MyBatis Plus）

## 文件清单

| 文件 | 说明 |
|------|------|
| `common/Result.java` | 统一 API 响应封装 |
| `common/PageResult.java` | 分页查询结果封装 |
| `common/BusinessException.java` | 业务异常类 |
| `common/GlobalExceptionHandler.java` | 全局异常处理器 |
| `util/JwtUtil.java` | JWT 工具类 |
| `config/MyBatisPlusConfig.java` | MyBatis Plus 分页插件配置 |

## 各类使用说明

### Result — 统一响应封装

所有 API 接口的返回值都使用此类封装，保证响应格式一致。

```java
// 成功（带数据）
return Result.ok(productList);

// 成功（无数据）
return Result.ok();

// 错误
return Result.error(400, "参数错误");
```

响应格式：
```json
{
  "code": 200,
  "message": "success",
  "data": { ... }
}
```

### PageResult — 分页结果封装

用于分页查询的返回值。

```java
PageResult<Product> page = productService.list(page, size, categoryId, status);
return Result.ok(page);
```

响应格式：
```json
{
  "code": 200,
  "message": "success",
  "data": {
    "total": 100,
    "list": [ ... ]
  }
}
```

### BusinessException — 业务异常

用于主动抛出可预期的业务错误，由 GlobalExceptionHandler 统一捕获。

```java
if (user == null) {
    throw new BusinessException("用户不存在");
}
```

### GlobalExceptionHandler — 全局异常处理器

自动捕获并处理以下异常：
- `BusinessException` — 业务异常，返回对应错误码和消息
- `MethodArgumentNotValidException` — 参数校验失败
- `Exception` — 未知异常，返回 500

无需手动调用，Spring 自动生效。

### JwtUtil — JWT 工具类

```java
// 生成 Token
String token = jwtUtil.generateToken(userId, username);

// 解析 Token
Claims claims = jwtUtil.parseToken(token);
Long userId = claims.get("userId", Long.class);
String username = claims.getSubject();
```

### MyBatisPlusConfig — 分页配置

配置 MyBatis Plus 分页插件，使分页查询生效。无需手动调用，Spring 自动生效。

## 验证方法

```bash
# 编译验证
mvn clean compile -pl xlx-common

# 所有模块依赖 common，编译 gateway 即可验证 common
mvn clean compile -pl xlx-gateway -am
```

## 依赖

无内部模块依赖（最底层模块）。

外部依赖：
- `spring-boot-starter-web` — Web 框架
- `spring-boot-starter-validation` — 参数校验
- `mybatis-plus-spring-boot3-starter` — ORM
- `jjwt-api / jjwt-impl / jjwt-jackson` — JWT 令牌
- `spring-security-crypto` — BCrypt 密码加密
