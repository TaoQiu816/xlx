# xlx-gateway — 网关/启动模块

## 职责

唯一可运行的 Spring Boot 应用，聚合所有业务模块。负责：
- Spring Boot 启动入口
- 公开 API 聚合（BFF 模式）
- CORS 跨域配置
- 静态资源映射
- JWT 拦截器注册
- MyBatis Mapper 扫描

## 包名

`com.xlx.api`

## Java 文件

| 文件 | 说明 |
|------|------|
| `XlxApiApplication.java` | Spring Boot 启动类，`@MapperScan("com.xlx.api.**.mapper")` 扫描所有模块 Mapper |
| `controller/publicc/PublicController.java` | BFF 聚合控制器，聚合各模块 Service 为前台提供统一接口 |
| `config/WebMvcConfig.java` | 全局配置（CORS、静态资源 /uploads/**、JWT 拦截器注册） |

## 资源文件

| 文件 | 说明 |
|------|------|
| `application.yml` | 应用配置（服务器、数据库、JWT、文件上传、日志） |
| `db/migration/V1__init_schema.sql` | 数据库表结构（10 张表） |
| `db/migration/V2__seed_data.sql` | 初始种子数据（管理员、分类、配置） |

## 运行方式

```bash
# 方式一：Maven 直接运行（开发阶段推荐）
mvn spring-boot:run -pl xlx-gateway

# 方式二：打包后运行
mvn clean package -DskipTests
java -jar target/xlx-gateway-0.0.1-SNAPSHOT.jar

# 方式三：指定环境变量运行
DB_PASSWORD=mypassword \
JWT_SECRET=my-secret-key \
UPLOAD_PATH=/data/uploads \
java -jar target/xlx-gateway-0.0.1-SNAPSHOT.jar
```

## 公开 API（前台使用，无需鉴权）

| 方法 | 路径 | 说明 |
|------|------|------|
| GET | `/api/public/config` | 获取网站配置 |
| GET | `/api/public/categories` | 获取产品分类 |
| GET | `/api/public/products` | 分页查询产品 |
| GET | `/api/public/products/featured` | 获取推荐产品 |
| GET | `/api/public/products/{slug}` | 产品详情 |
| GET | `/api/public/factory-images` | 工厂图片列表 |
| GET | `/api/public/certificates` | 证书列表 |
| POST | `/api/public/inquiries` | 提交询盘 |
| POST | `/api/public/upload` | 上传文件 |

## 公开 API 调用示例

```bash
# 获取网站配置
curl http://localhost:8080/api/public/config

# 获取产品分类
curl http://localhost:8080/api/public/categories

# 分页查询产品
curl "http://localhost:8080/api/public/products?page=1&size=20"

# 按分类筛选产品
curl "http://localhost:8080/api/public/products?page=1&size=20&categoryId=3"

# 获取推荐产品
curl "http://localhost:8080/api/public/products/featured?limit=8"

# 产品详情（按 slug）
curl http://localhost:8080/api/public/products/stainless-steel-wire

# 工厂图片
curl http://localhost:8080/api/public/factory-images

# 证书列表
curl http://localhost:8080/api/public/certificates

# 提交询盘
curl -X POST http://localhost:8080/api/public/inquiries \
  -H "Content-Type: application/json" \
  -d '{
    "name": "张三",
    "company": "ABC公司",
    "email": "zhangsan@example.com",
    "phone": "13800138000",
    "country": "中国",
    "message": "请报价304不锈钢丝"
  }'

# 上传文件
curl -X POST http://localhost:8080/api/public/upload \
  -F "file=@/path/to/drawing.pdf"
```

## 后台管理 API（需 JWT Token）

所有 `/api/admin/**` 接口（除登录外）需要在请求头中携带 Token：

```bash
# 1. 登录获取 Token
TOKEN=$(curl -s -X POST http://localhost:8080/api/admin/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"admin","password":"admin123"}' | grep -o '"token":"[^"]*"' | cut -d'"' -f4)

# 2. 使用 Token 访问后台接口
curl -H "Authorization: Bearer $TOKEN" \
  http://localhost:8080/api/admin/auth/profile

curl -H "Authorization: Bearer $TOKEN" \
  "http://localhost:8080/api/admin/products?page=1&size=20"

curl -H "Authorization: Bearer $TOKEN" \
  http://localhost:8080/api/admin/inquiries
```

## 测试

```bash
# 运行后端测试
mvn test -pl xlx-gateway

# 运行所有模块测试
mvn test

# 跳过测试打包
mvn clean package -DskipTests
```

## 本地完整运行（后端 + 前台 + 后台）

本模块是后端入口，完整体验需要同时运行前端项目：

```bash
# 终端 1：启动后端（本模块）
cd /Volumes/ES4_QT/develop/XLX/xlx-api
mvn spring-boot:run -pl xlx-gateway

# 终端 2：启动前台展示网站
cd /Volumes/ES4_QT/develop/XLX/xlx-web
npm install && npm run dev

# 终端 3：启动后台管理系统
cd /Volumes/ES4_QT/develop/XLX/xlx-admin
npm install && npm run dev
```

| 服务 | 地址 |
|------|------|
| 后端 API | `http://localhost:8080/api/public/config` |
| 前台展示 | `http://localhost:3000` |
| 后台管理 | `http://localhost:3001/admin/` |

默认管理员账号：`admin` / `admin123`

## 验证方法

```bash
# 1. 编译验证
mvn clean compile -pl xlx-gateway -am

# 2. 打包验证
mvn clean package -DskipTests
ls -la target/*.jar

# 3. 启动验证
java -jar target/xlx-gateway-0.0.1-SNAPSHOT.jar
# 等待看到 "Started XlxApiApplication in X.XXX seconds"

# 4. 接口验证
curl http://localhost:8080/api/public/config
curl -X POST http://localhost:8080/api/admin/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"admin","password":"admin123"}'

# 5. 静态资源验证
# 上传文件后访问
curl http://localhost:8080/uploads/test.jpg
```

## 依赖

| 依赖 | 说明 |
|------|------|
| xlx-auth | 认证模块 |
| xlx-product | 产品模块 |
| xlx-content | 内容模块 |
| xlx-inquiry | 询盘模块 |
| xlx-file | 文件上传模块 |
| xlx-config | 站点配置模块 |
| mysql-connector-j (runtime) | MySQL 驱动 |
| spring-boot-starter-test (test) | 测试框架 |
