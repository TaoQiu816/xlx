# xlx-api 架构说明

## 项目概述

鑫连鑫丝网厂 — B2B 丝网工厂官网与销售系统后端。

采用 **模块化单体（Modular Monolith）** 架构，每个业务域独立 Maven 模块，通过 gateway 聚合为单一 Spring Boot 应用部署。未来可将各模块拆分为独立微服务。

## 技术栈

| 技术 | 版本 | 用途 |
|------|------|------|
| Java | 17 | 运行时 |
| Spring Boot | 3.2.5 | 应用框架 |
| MyBatis Plus | 3.5.6 | ORM |
| MySQL | 8.0 | 数据库 |
| JWT (jjwt) | 0.12.5 | 身份认证 |
| BCrypt | — | 密码加密 |

---

## 快速开始

### 前置条件

- JDK 17+（推荐 17）
- Maven 3.8+
- MySQL 8.0+（开发阶段可兼容 5.7）

```bash
# 检查版本
java -version        # 需要 17+
mvn -version         # 需要 3.8+
mysql --version      # 需要 8.0+
```

### 第一步：创建数据库

```bash
# 登录 MySQL
mysql -u root -p

# 必须先手动创建数据库（Flyway 只负责建表，不会自动建库）
CREATE DATABASE xlx DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

### 第二步：配置数据库连接

编辑 `xlx-gateway/src/main/resources/application.yml`，或通过环境变量覆盖：

```bash
# 方式一：直接编辑 application.yml
# 修改 spring.datasource.url / username / password

# 方式二：环境变量（推荐）
export DB_PASSWORD=your_mysql_password
export JWT_SECRET=your-jwt-secret-key
export UPLOAD_PATH=/path/to/uploads
```

### 第三步：编译

```bash
cd /Volumes/ES4_QT/develop/XLX/xlx-api

# 编译所有模块
mvn clean compile
```

预期输出：
```
[INFO] xlx-api ............................................ SUCCESS
[INFO] xlx-common ......................................... SUCCESS
[INFO] xlx-auth ........................................... SUCCESS
[INFO] xlx-product ........................................ SUCCESS
[INFO] xlx-content ........................................ SUCCESS
[INFO] xlx-inquiry ........................................ SUCCESS
[INFO] xlx-file ........................................... SUCCESS
[INFO] xlx-config ......................................... SUCCESS
[INFO] xlx-gateway ........................................ SUCCESS
[INFO] BUILD SUCCESS
```

### 第四步：打包

```bash
# 打包（仅 xlx-gateway 生成可执行 JAR）
mvn clean package

# 跳过测试打包
mvn clean package -DskipTests
```

### 第五步：运行

```bash
# 方式一：直接运行 JAR
java -jar xlx-gateway/target/xlx-gateway-0.0.1-SNAPSHOT.jar

# 方式二：指定环境变量运行
DB_PASSWORD=mypassword JWT_SECRET=my-secret java -jar xlx-gateway/target/xlx-gateway-0.0.1-SNAPSHOT.jar

# 方式三：Maven 直接运行（开发阶段）
mvn spring-boot:run -pl xlx-gateway
```

启动成功标志：
```
Started XlxApiApplication in X.XXX seconds
```

### 第六步：验证服务

```bash
# 健康检查 — 获取网站配置
curl http://localhost:8080/api/public/config

# 预期返回：
# {"code":200,"message":"success","data":{"site_name":"鑫连鑫丝网厂",...}}
```

---

## 本地运行完整项目（前台 + 后台 + 后端）

本项目由 3 个独立项目组成，需要同时运行才能体验完整功能：

| 项目 | 技术栈 | 端口 | 地址 |
|------|--------|------|------|
| xlx-api | Spring Boot | 8080 | `http://localhost:8080` |
| xlx-web（前台展示） | Vue 3 + Tailwind CSS | 3000 | `http://localhost:3000` |
| xlx-admin（后台管理） | Vue 3 + Element Plus | 3001 | `http://localhost:3001/admin/` |

### 运行步骤

#### 1. 启动后端（xlx-api）

```bash
# 方式一：IDE 中运行（推荐）
# 打开 xlx-api/xlx-gateway/src/main/java/com/xlx/api/XlxApiApplication.java
# 点击 main 方法左边的绿色三角按钮运行

# 方式二：命令行运行
cd /Volumes/ES4_QT/develop/XLX/xlx-api
mvn spring-boot:run -pl xlx-gateway
```

启动成功标志：控制台输出 `Started XlxApiApplication in X.XXX seconds`

#### 2. 启动前台展示网站（xlx-web）

```bash
cd /Volumes/ES4_QT/develop/XLX/xlx-web
npm install    # 首次运行需要安装依赖
npm run dev
```

启动后访问：`http://localhost:3000`

Vite 开发服务器会自动将 `/api/**` 请求代理到 `http://localhost:8080`，无需额外配置。

#### 3. 启动后台管理系统（xlx-admin）

```bash
cd /Volumes/ES4_QT/develop/XLX/xlx-admin
npm install    # 首次运行需要安装依赖
npm run dev
```

启动后访问：`http://localhost:3001/admin/`

默认管理员账号：`admin` / `admin123`

### 访问地址汇总

| 页面 | 地址 | 说明 |
|------|------|------|
| 前台首页 | `http://localhost:3000` | 展示网站首页 |
| 前台产品页 | `http://localhost:3000/products` | 产品列表 |
| 前台工厂页 | `http://localhost:3000/factory` | 工厂展示 |
| 前台证书页 | `http://localhost:3000/certificates` | 资质证书 |
| 前台联系页 | `http://localhost:3000/contact` | 联系方式 + 询盘表单 |
| 后台登录 | `http://localhost:3001/admin/login` | 管理员登录 |
| 后台仪表盘 | `http://localhost:3001/admin/` | 管理后台首页 |
| 后端 API | `http://localhost:8080/api/public/config` | 直接访问 API |

常见问题请参见文档末尾的「常见问题」章节。

---

## 模块结构

```
xlx-api/                          ← 父 POM（packaging=pom）
├── ARCHITECTURE.md               ← 本文件
├── xlx-common/                   ← 公共库（所有模块依赖）
├── xlx-auth/                     ← 认证模块
├── xlx-product/                  ← 产品模块
├── xlx-content/                  ← 内容模块
├── xlx-inquiry/                  ← 询盘模块
├── xlx-file/                     ← 文件上传模块
├── xlx-config/                   ← 站点配置模块
└── xlx-gateway/                  ← 网关/启动模块（唯一可运行的应用）
```

## 模块依赖关系

```
xlx-common  ← 所有模块依赖（最底层）
xlx-auth    ← xlx-common
xlx-product ← xlx-common
xlx-content ← xlx-common
xlx-inquiry ← xlx-common
xlx-file    ← xlx-common
xlx-config  ← xlx-common
xlx-gateway ← 以上所有模块（聚合层）
```

---

## 数据库

数据库名：`xlx`，字符集 `utf8mb4`，引擎 `InnoDB`。共 10 张表。

### 初始化方式

通过 Flyway 自动执行，脚本位于 `xlx-gateway/src/main/resources/db/migration/`：

| 脚本 | 作用 |
|------|------|
| `V1__init_schema.sql` | 建 10 张表 |
| `V2__seed_data.sql` | 插入管理员、分类、网站配置 |

**Flyway 工作流程**：后端启动时自动连接数据库，检查 `flyway_schema_history` 表，按版本号顺序执行未执行过的脚本。只需手动创建数据库，建表和插入数据全自动。

> 首次使用只需两步：
> 1. 手动建库：`CREATE DATABASE xlx DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;`
> 2. IDEA 重启后端，Flyway 自动建表 + 插入种子数据
>
> 如需手动执行脚本（不依赖 Flyway）：
> ```bash
> mysql -u root -proot < xlx-gateway/src/main/resources/db/migration/V1__init_schema.sql
> mysql -u root -proot < xlx-gateway/src/main/resources/db/migration/V2__seed_data.sql
> ```

### 表概览

| 表名 | 模块 | 说明 | 记录数（初始） |
|------|------|------|--------------|
| admin_user | xlx-auth | 管理员账号 | 1 |
| product_category | xlx-product | 产品分类（两级树形） | 10 |
| product | xlx-product | 产品信息（中英文） | 0 |
| product_spec | xlx-product | 产品规格 | 0 |
| product_image | xlx-product | 产品图片 | 0 |
| factory_image | xlx-content | 工厂展示图片 | 0 |
| certificate | xlx-content | 资质证书 | 0 |
| inquiry | xlx-inquiry | 客户询盘 | 0 |
| file_upload | xlx-file | 文件上传记录 | 0 |
| site_config | xlx-config | 网站配置（键值对） | 12 |

### 1. admin_user — 管理员表

| 字段 | 类型 | 必填 | 默认值 | 说明 |
|------|------|------|--------|------|
| id | BIGINT | PK | 自增 | 主键 |
| username | VARCHAR(50) | 是 | — | 登录名（唯一） |
| password | VARCHAR(255) | 是 | — | BCrypt 加密密码 |
| nickname | VARCHAR(50) | 否 | '' | 显示昵称 |
| role | VARCHAR(20) | 否 | 'admin' | 角色：admin / editor |
| status | TINYINT | 否 | 1 | 1=启用 0=禁用 |
| last_login_at | DATETIME | 否 | NULL | 最后登录时间 |
| created_at | DATETIME | 否 | CURRENT_TIMESTAMP | 创建时间 |
| updated_at | DATETIME | 否 | CURRENT_TIMESTAMP | 更新时间（自动） |

### 2. product_category — 产品分类表

支持两级树形结构（parent_id=0 为一级分类）。

| 字段 | 类型 | 必填 | 默认值 | 说明 |
|------|------|------|--------|------|
| id | BIGINT | PK | 自增 | 主键 |
| parent_id | BIGINT | 否 | 0 | 父分类 ID，0=顶级 |
| name_cn | VARCHAR(100) | 是 | — | 中文名称 |
| name_en | VARCHAR(100) | 否 | '' | 英文名称 |
| slug | VARCHAR(100) | 否 | '' | URL 标识（如 `stainless-steel-wire`） |
| cover_image | VARCHAR(500) | 否 | '' | 分类封面图 URL |
| sort_order | INT | 否 | 0 | 排序值（小的排前面） |
| status | TINYINT | 否 | 1 | 1=显示 0=隐藏 |
| created_at | DATETIME | 否 | CURRENT_TIMESTAMP | 创建时间 |
| updated_at | DATETIME | 否 | CURRENT_TIMESTAMP | 更新时间（自动） |

### 3. product — 产品表

所有文本字段均有 `_cn` / `_en` 双语后缀。

| 字段 | 类型 | 必填 | 默认值 | 说明 |
|------|------|------|--------|------|
| id | BIGINT | PK | 自增 | 主键 |
| category_id | BIGINT | 是 | — | 所属分类 ID |
| name_cn | VARCHAR(200) | 是 | — | 中文名称 |
| name_en | VARCHAR(200) | 否 | '' | 英文名称 |
| slug | VARCHAR(200) | 否 | '' | URL 标识 |
| summary_cn | VARCHAR(500) | 否 | '' | 中文摘要 |
| summary_en | VARCHAR(500) | 否 | '' | 英文摘要 |
| description_cn | TEXT | 否 | NULL | 中文详情（富文本） |
| description_en | TEXT | 否 | NULL | 英文详情（富文本） |
| main_image | VARCHAR(500) | 否 | '' | 主图 URL |
| price | DECIMAL(10,2) | 否 | NULL | 单价，NULL=联系报价 |
| price_unit | VARCHAR(20) | 否 | '' | 价格单位（元/件、元/米） |
| show_price | TINYINT | 否 | 0 | 1=前台显示价格 |
| support_custom | TINYINT | 否 | 0 | 1=支持定制 |
| support_inquiry | TINYINT | 否 | 1 | 1=支持询价 |
| support_order | TINYINT | 否 | 0 | 1=支持下单（V2预留） |
| moq | VARCHAR(100) | 否 | '' | 最小起订量 |
| stock_status | VARCHAR(20) | 否 | 'in_stock' | in_stock / out_of_stock / pre_order |
| packaging_cn | VARCHAR(200) | 否 | '' | 中文包装说明 |
| packaging_en | VARCHAR(200) | 否 | '' | 英文包装说明 |
| applications_cn | VARCHAR(500) | 否 | '' | 中文应用场景 |
| applications_en | VARCHAR(500) | 否 | '' | 英文应用场景 |
| certificate_note_cn | VARCHAR(500) | 否 | '' | 中文证书说明 |
| certificate_note_en | VARCHAR(500) | 否 | '' | 英文证书说明 |
| sort_order | INT | 否 | 0 | 排序值 |
| is_featured | TINYINT | 否 | 0 | 1=推荐到首页 |
| status | TINYINT | 否 | 1 | 1=上架 0=下架 |
| created_at | DATETIME | 否 | CURRENT_TIMESTAMP | 创建时间 |
| updated_at | DATETIME | 否 | CURRENT_TIMESTAMP | 更新时间（自动） |

### 4. product_spec — 产品规格表

| 字段 | 类型 | 必填 | 默认值 | 说明 |
|------|------|------|--------|------|
| id | BIGINT | PK | 自增 | 主键 |
| product_id | BIGINT | 是 | — | 所属产品 ID |
| spec_name_cn | VARCHAR(100) | 是 | — | 规格名（如：材质、丝径、目数） |
| spec_name_en | VARCHAR(100) | 否 | '' | 英文规格名 |
| spec_value_cn | VARCHAR(200) | 是 | — | 规格值（如：304、0.5mm、100目） |
| spec_value_en | VARCHAR(200) | 否 | '' | 英文规格值 |
| sort_order | INT | 否 | 0 | 排序值 |
| created_at | DATETIME | 否 | CURRENT_TIMESTAMP | 创建时间 |
| updated_at | DATETIME | 否 | CURRENT_TIMESTAMP | 更新时间（自动） |

### 5. product_image — 产品图片表

| 字段 | 类型 | 必填 | 默认值 | 说明 |
|------|------|------|--------|------|
| id | BIGINT | PK | 自增 | 主键 |
| product_id | BIGINT | 是 | — | 所属产品 ID |
| image_url | VARCHAR(500) | 是 | — | 图片 URL |
| alt_cn | VARCHAR(200) | 否 | '' | 中文 alt 文本 |
| alt_en | VARCHAR(200) | 否 | '' | 英文 alt 文本 |
| sort_order | INT | 否 | 0 | 排序值 |
| created_at | DATETIME | 否 | CURRENT_TIMESTAMP | 创建时间 |

### 6. factory_image — 工厂图片表

| 字段 | 类型 | 必填 | 默认值 | 说明 |
|------|------|------|--------|------|
| id | BIGINT | PK | 自增 | 主键 |
| title_cn | VARCHAR(200) | 否 | '' | 中文标题 |
| title_en | VARCHAR(200) | 否 | '' | 英文标题 |
| image_url | VARCHAR(500) | 是 | — | 图片 URL |
| description_cn | VARCHAR(500) | 否 | '' | 中文描述 |
| description_en | VARCHAR(500) | 否 | '' | 英文描述 |
| sort_order | INT | 否 | 0 | 排序值 |
| status | TINYINT | 否 | 1 | 1=显示 0=隐藏 |
| created_at | DATETIME | 否 | CURRENT_TIMESTAMP | 创建时间 |
| updated_at | DATETIME | 否 | CURRENT_TIMESTAMP | 更新时间（自动） |

### 7. certificate — 证书表

| 字段 | 类型 | 必填 | 默认值 | 说明 |
|------|------|------|--------|------|
| id | BIGINT | PK | 自增 | 主键 |
| title_cn | VARCHAR(200) | 否 | '' | 中文标题 |
| title_en | VARCHAR(200) | 否 | '' | 英文标题 |
| image_url | VARCHAR(500) | 是 | — | 证书图片 URL |
| description_cn | VARCHAR(500) | 否 | '' | 中文描述 |
| description_en | VARCHAR(500) | 否 | '' | 英文描述 |
| certificate_type | VARCHAR(50) | 否 | '' | 类型：certificate / report / quality |
| issue_date | DATE | 否 | NULL | 颁发日期 |
| sort_order | INT | 否 | 0 | 排序值 |
| status | TINYINT | 否 | 1 | 1=显示 0=隐藏 |
| created_at | DATETIME | 否 | CURRENT_TIMESTAMP | 创建时间 |
| updated_at | DATETIME | 否 | CURRENT_TIMESTAMP | 更新时间（自动） |

### 8. inquiry — 询盘表

| 字段 | 类型 | 必填 | 默认值 | 说明 |
|------|------|------|--------|------|
| id | BIGINT | PK | 自增 | 主键 |
| name | VARCHAR(100) | 是 | — | 联系人姓名 |
| company | VARCHAR(200) | 否 | '' | 公司名称 |
| phone | VARCHAR(50) | 否 | '' | 电话 |
| email | VARCHAR(100) | 否 | '' | 邮箱 |
| wechat | VARCHAR(50) | 否 | '' | 微信 |
| whatsapp | VARCHAR(50) | 否 | '' | WhatsApp |
| country | VARCHAR(100) | 否 | '' | 国家/地区 |
| product_id | BIGINT | 否 | NULL | 感兴趣的产品 ID |
| product_name | VARCHAR(200) | 否 | '' | 产品名称（冗余存储） |
| quantity | VARCHAR(100) | 否 | '' | 需求量 |
| specification | VARCHAR(500) | 否 | '' | 规格要求 |
| message | TEXT | 否 | NULL | 留言内容 |
| file_url | VARCHAR(500) | 否 | '' | 附件 URL |
| status | VARCHAR(20) | 否 | 'new' | 状态（见下方流转） |
| remark | TEXT | 否 | NULL | 后台备注 |
| created_at | DATETIME | 否 | CURRENT_TIMESTAMP | 创建时间 |
| updated_at | DATETIME | 否 | CURRENT_TIMESTAMP | 更新时间（自动） |

**询盘状态流转**：new → contacted → quoted → sample_confirmed → order_confirmed / closed / invalid

### 9. file_upload — 文件上传记录表

| 字段 | 类型 | 必填 | 默认值 | 说明 |
|------|------|------|--------|------|
| id | BIGINT | PK | 自增 | 主键 |
| original_name | VARCHAR(255) | 是 | — | 原始文件名 |
| file_name | VARCHAR(255) | 是 | — | 存储文件名（UUID） |
| file_url | VARCHAR(500) | 是 | — | 访问路径（如 `/uploads/xxx.jpg`） |
| file_type | VARCHAR(50) | 否 | '' | MIME 类型 |
| file_size | BIGINT | 否 | 0 | 文件大小（字节） |
| biz_type | VARCHAR(50) | 否 | '' | 业务类型：product_image / factory_image / certificate / inquiry |
| biz_id | BIGINT | 否 | NULL | 关联业务 ID |
| created_at | DATETIME | 否 | CURRENT_TIMESTAMP | 创建时间 |

### 10. site_config — 网站配置表

键值对存储，初始 12 个配置项。

| 字段 | 类型 | 必填 | 默认值 | 说明 |
|------|------|------|--------|------|
| id | BIGINT | PK | 自增 | 主键 |
| config_key | VARCHAR(100) | 是 | — | 配置键（唯一） |
| config_value | TEXT | 否 | NULL | 配置值 |
| description | VARCHAR(200) | 否 | '' | 配置说明 |
| created_at | DATETIME | 否 | CURRENT_TIMESTAMP | 创建时间 |
| updated_at | DATETIME | 否 | CURRENT_TIMESTAMP | 更新时间（自动） |

**初始配置项**：

| config_key | 说明 |
|------------|------|
| site_name | 网站名称 |
| company_name | 公司中文名 |
| company_name_en | 公司英文名 |
| phone | 联系电话 |
| email | 邮箱 |
| wechat | 微信 |
| whatsapp | WhatsApp |
| address | 公司地址 |
| banner_title | 首页 Banner 标题 |
| banner_subtitle | 首页 Banner 副标题 |
| seo_title | SEO 标题 |
| seo_description | SEO 描述 |

### 设计要点

1. **主键**：所有表使用 `BIGINT AUTO_INCREMENT`
2. **双语**：文本字段以 `_cn` / `_en` 后缀区分中英文
3. **时间**：`created_at` 自动创建，`updated_at` 自动更新
4. **软删除**：通过 `deleted` 字段实现（MyBatis Plus 逻辑删除），当前未启用
5. **索引**：外键、排序、状态、slug 等常用查询字段已建索引
6. **引擎**：全部 InnoDB，支持事务

---

## 环境变量

| 变量 | 默认值 | 说明 |
|------|--------|------|
| `DB_PASSWORD` | root | MySQL 数据库密码 |
| `JWT_SECRET` | xlx-jwt-secret-key-change-in-production | JWT 签名密钥（生产环境必须修改） |
| `UPLOAD_PATH` | ./uploads | 文件上传存储目录 |

---

## API 完整参考

### 认证接口（无需 Token）

#### 管理员登录

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

### 后台管理接口（需要 Token）

所有 `/api/admin/**` 接口（除登录外）需要在请求头中携带 JWT Token：

```bash
# 先登录获取 Token
TOKEN=$(curl -s -X POST http://localhost:8080/api/admin/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"admin","password":"admin123"}' | grep -o '"token":"[^"]*"' | cut -d'"' -f4)

# 后续请求携带 Token
curl -H "Authorization: Bearer $TOKEN" http://localhost:8080/api/admin/auth/profile
```

#### 获取管理员信息

```bash
curl -H "Authorization: Bearer $TOKEN" \
  http://localhost:8080/api/admin/auth/profile
```

#### 产品分类管理

```bash
# 查询所有分类
curl -H "Authorization: Bearer $TOKEN" \
  http://localhost:8080/api/admin/categories

# 新增分类
curl -X POST http://localhost:8080/api/admin/categories \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{"parentId":0,"nameCn":"新产品类","nameEn":"New Category","slug":"new-category","sortOrder":3}'

# 更新分类
curl -X PUT http://localhost:8080/api/admin/categories/1 \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{"nameCn":"金属丝类（已更新）"}'

# 删除分类
curl -X DELETE http://localhost:8080/api/admin/categories/11 \
  -H "Authorization: Bearer $TOKEN"
```

#### 产品管理

```bash
# 分页查询产品（第 1 页，每页 20 条）
curl -H "Authorization: Bearer $TOKEN" \
  "http://localhost:8080/api/admin/products?page=1&size=20"

# 按分类筛选
curl -H "Authorization: Bearer $TOKEN" \
  "http://localhost:8080/api/admin/products?page=1&size=20&categoryId=3"

# 产品详情
curl -H "Authorization: Bearer $TOKEN" \
  http://localhost:8080/api/admin/products/1

# 新增产品
curl -X POST http://localhost:8080/api/admin/products \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "categoryId": 3,
    "nameCn": "304不锈钢丝",
    "nameEn": "304 Stainless Steel Wire",
    "slug": "304-stainless-steel-wire",
    "summaryCn": "优质304不锈钢丝",
    "price": null,
    "supportInquiry": 1,
    "status": 1
  }'

# 更新产品
curl -X PUT http://localhost:8080/api/admin/products/1 \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{"nameCn":"304不锈钢丝（更新）"}'

# 删除产品
curl -X DELETE http://localhost:8080/api/admin/products/1 \
  -H "Authorization: Bearer $TOKEN"
```

#### 工厂图片管理

```bash
# 查询工厂图片列表
curl -H "Authorization: Bearer $TOKEN" \
  "http://localhost:8080/api/admin/factory-images?page=1&size=20"

# 新增工厂图片
curl -X POST http://localhost:8080/api/admin/factory-images \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{"titleCn":"生产车间","titleEn":"Workshop","imageUrl":"/uploads/factory/workshop.jpg","sortOrder":1}'

# 删除工厂图片
curl -X DELETE http://localhost:8080/api/admin/factory-images/1 \
  -H "Authorization: Bearer $TOKEN"
```

#### 证书管理

```bash
# 查询证书列表
curl -H "Authorization: Bearer $TOKEN" \
  "http://localhost:8080/api/admin/certificates?page=1&size=20"

# 新增证书
curl -X POST http://localhost:8080/api/admin/certificates \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{"titleCn":"ISO9001认证","titleEn":"ISO9001 Certificate","imageUrl":"/uploads/cert/iso9001.jpg","certificateType":"certificate","issueDate":"2024-01-01"}'
```

#### 询盘管理

```bash
# 查询所有询盘
curl -H "Authorization: Bearer $TOKEN" \
  "http://localhost:8080/api/admin/inquiries?page=1&size=20"

# 按状态筛选
curl -H "Authorization: Bearer $TOKEN" \
  "http://localhost:8080/api/admin/inquiries?page=1&size=20&status=new"

# 询盘详情
curl -H "Authorization: Bearer $TOKEN" \
  http://localhost:8080/api/admin/inquiries/1

# 更新状态
curl -X PUT http://localhost:8080/api/admin/inquiries/1/status \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{"status":"contacted"}'

# 更新备注
curl -X PUT http://localhost:8080/api/admin/inquiries/1/remark \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{"remark":"客户需要304不锈钢丝报价"}'

# 删除询盘
curl -X DELETE http://localhost:8080/api/admin/inquiries/1 \
  -H "Authorization: Bearer $TOKEN"
```

#### 站点配置管理

```bash
# 查询所有配置
curl -H "Authorization: Bearer $TOKEN" \
  http://localhost:8080/api/admin/site-config

# 批量更新配置
curl -X PUT http://localhost:8080/api/admin/site-config \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{"phone":"0318-1234567","email":"info@xlxcn.cn"}'
```

#### 文件上传

```bash
# 上传文件
curl -X POST http://localhost:8080/api/admin/upload \
  -H "Authorization: Bearer $TOKEN" \
  -F "file=@/path/to/image.jpg" \
  -F "bizType=product_image" \
  -F "bizId=1"
```

### 前台公开接口（无需 Token）

#### 获取网站配置

```bash
curl http://localhost:8080/api/public/config
```

#### 获取产品分类

```bash
curl http://localhost:8080/api/public/categories
```

#### 分页查询产品

```bash
# 默认每页 20 条
curl "http://localhost:8080/api/public/products?page=1&size=20"

# 按分类筛选
curl "http://localhost:8080/api/public/products?page=1&size=20&categoryId=3"
```

#### 获取推荐产品

```bash
# 默认 8 个
curl "http://localhost:8080/api/public/products/featured?limit=8"
```

#### 产品详情（按 slug）

```bash
curl http://localhost:8080/api/public/products/stainless-steel-wire
```

#### 工厂图片

```bash
curl http://localhost:8080/api/public/factory-images
```

#### 证书列表

```bash
curl http://localhost:8080/api/public/certificates
```

#### 提交询盘

```bash
curl -X POST http://localhost:8080/api/public/inquiries \
  -H "Content-Type: application/json" \
  -d '{
    "name": "张三",
    "company": "ABC公司",
    "email": "zhangsan@example.com",
    "phone": "13800138000",
    "country": "中国",
    "productId": 1,
    "productName": "304不锈钢丝",
    "quantity": "500公斤",
    "specification": "丝径0.5mm",
    "message": "请报价"
  }'
```

#### 前台文件上传

```bash
curl -X POST http://localhost:8080/api/public/upload \
  -F "file=@/path/to/drawing.pdf"
```

---

## 验证方法

### 编译验证

```bash
# 编译所有模块
mvn clean compile

# 只编译单个模块（及其依赖）
mvn clean compile -pl xlx-product -am
```

### 打包验证

```bash
# 完整打包（含测试）
mvn clean package

# 跳过测试打包
mvn clean package -DskipTests

# 验证 JAR 是否生成
ls -la xlx-gateway/target/*.jar
```

### 运行验证

```bash
# 启动服务
java -jar xlx-gateway/target/xlx-gateway-0.0.1-SNAPSHOT.jar

# 验证 1：公开接口（无需 Token）
curl http://localhost:8080/api/public/config

# 验证 2：登录获取 Token
curl -X POST http://localhost:8080/api/admin/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"admin","password":"admin123"}'

# 验证 3：带 Token 访问后台接口
curl -H "Authorization: Bearer YOUR_TOKEN" \
  http://localhost:8080/api/admin/products?page=1&size=5

# 验证 4：无 Token 访问后台接口（应返回 401）
curl http://localhost:8080/api/admin/products
```

### 数据库验证

```bash
# 检查表是否创建成功
mysql -u root -p xlx -e "SHOW TABLES;"

# 预期输出 10 张表：
# admin_user, product_category, product, product_spec,
# product_image, factory_image, certificate, inquiry,
# file_upload, site_config

# 检查种子数据
mysql -u root -p xlx -e "SELECT COUNT(*) FROM admin_user;"
mysql -u root -p xlx -e "SELECT COUNT(*) FROM product_category;"
mysql -u root -p xlx -e "SELECT COUNT(*) FROM site_config;"
```

### 单模块编译验证

```bash
# 只编译某个模块（加快开发迭代）
mvn clean compile -pl xlx-common              # 只编译 common
mvn clean compile -pl xlx-auth -am            # 编译 auth 及其依赖
mvn clean compile -pl xlx-gateway -am         # 编译 gateway 及所有依赖
```

---

## 开发工作流

### 修改单个模块的代码

```bash
# 1. 修改代码（例如 xlx-product 模块）
# 2. 编译验证
mvn clean compile -pl xlx-product -am

# 3. 运行测试
mvn test -pl xlx-product

# 4. 完整打包验证
mvn clean package
```

### 添加新的 API 接口

1. 在对应模块的 `controller/` 目录下添加控制器方法
2. 在 `service/` 目录下添加业务逻辑
3. 如需新表，在 `entity/` 和 `mapper/` 目录下添加实体和 Mapper
4. 编译验证：`mvn clean compile -pl 模块名 -am`
5. 前台接口在 `xlx-gateway` 的 `PublicController` 中聚合

### 添加新的配置项

```bash
# 1. 在 site_config 表中插入新配置
mysql -u root -p xlx -e "INSERT INTO site_config (config_key, config_value, description) VALUES ('new_key', '默认值', '配置说明');"

# 2. 前端通过 /api/public/config 自动获取
curl http://localhost:8080/api/public/config
```

---

## 项目结构详解

```
xlx-api/
├── pom.xml                          ← 父 POM，统一依赖版本管理
├── ARCHITECTURE.md                  ← 架构说明（本文件）
│
├── xlx-common/                      ← 公共库模块
│   ├── pom.xml
│   ├── README.md
│   └── src/main/java/com/xlx/api/
│       ├── common/                  ← Result、PageResult、异常处理
│       ├── util/                    ← JwtUtil
│       └── config/                  ← MyBatisPlusConfig
│
├── xlx-auth/                        ← 认证模块
│   ├── pom.xml
│   ├── README.md
│   └── src/main/java/com/xlx/api/auth/
│       ├── entity/                  ← AdminUser
│       ├── mapper/                  ← AdminUserMapper
│       ├── service/                 ← AuthService
│       ├── controller/              ← AuthController
│       └── config/                  ← AuthInterceptor
│
├── xlx-product/                     ← 产品模块
│   ├── pom.xml
│   ├── README.md
│   └── src/main/java/com/xlx/api/product/
│       ├── entity/                  ← Product, ProductCategory, ProductSpec, ProductImage
│       ├── mapper/                  ← 4 个 Mapper
│       ├── service/                 ← 4 个 Service
│       └── controller/              ← CategoryController, ProductController
│
├── xlx-content/                     ← 内容模块
│   ├── pom.xml
│   ├── README.md
│   └── src/main/java/com/xlx/api/content/
│       ├── entity/                  ← FactoryImage, Certificate
│       ├── mapper/                  ← 2 个 Mapper
│       ├── service/                 ← 2 个 Service
│       └── controller/              ← FactoryImageController, CertificateController
│
├── xlx-inquiry/                     ← 询盘模块
│   ├── pom.xml
│   ├── README.md
│   └── src/main/java/com/xlx/api/inquiry/
│       ├── entity/                  ← Inquiry
│       ├── mapper/                  ← InquiryMapper
│       ├── service/                 ← InquiryService
│       └── controller/              ← InquiryController
│
├── xlx-file/                        ← 文件上传模块
│   ├── pom.xml
│   ├── README.md
│   └── src/main/java/com/xlx/api/file/
│       ├── entity/                  ← FileUpload
│       ├── mapper/                  ← FileUploadMapper
│       ├── service/                 ← FileUploadService
│       └── controller/              ← UploadController
│
├── xlx-config/                      ← 站点配置模块
│   ├── pom.xml
│   ├── README.md
│   └── src/main/java/com/xlx/api/config/
│       ├── entity/                  ← SiteConfig
│       ├── mapper/                  ← SiteConfigMapper
│       ├── service/                 ← SiteConfigService
│       └── controller/              ← SiteConfigController
│
└── xlx-gateway/                     ← 网关/启动模块
    ├── pom.xml
    ├── README.md
    └── src/main/
        ├── java/com/xlx/api/
        │   ├── XlxApiApplication.java       ← 启动类
        │   ├── controller/publicc/          ← PublicController（BFF）
        │   └── config/                      ← WebMvcConfig
        └── resources/
            ├── application.yml              ← 应用配置
            └── db/migration/
                ├── V1__init_schema.sql      ← 建表脚本
                └── V2__seed_data.sql        ← 种子数据
```

---

## 测试

### 后端测试

```bash
# 运行所有模块的测试
mvn test

# 运行单个模块的测试
mvn test -pl xlx-auth
mvn test -pl xlx-product

# 跳过测试打包
mvn clean package -DskipTests
```

测试文件位于各模块的 `src/test/java/` 目录下。

### 前端测试

```bash
# xlx-web 类型检查
cd xlx-web
npm run build    # 编译检查（会报类型错误）

# xlx-admin 类型检查
cd xlx-admin
npm run build    # 编译检查
```

### 接口测试

```bash
# 1. 测试公开接口（无需 Token）
curl http://localhost:8080/api/public/config
curl http://localhost:8080/api/public/categories
curl "http://localhost:8080/api/public/products?page=1&size=5"

# 2. 测试登录
curl -X POST http://localhost:8080/api/admin/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"admin","password":"admin123"}'

# 3. 测试后台接口（用上一步返回的 Token）
curl -H "Authorization: Bearer YOUR_TOKEN" \
  http://localhost:8080/api/admin/auth/profile

# 4. 测试前台页面
# 浏览器打开 http://localhost:3000，检查：
# - 首页是否正常加载
# - 产品页是否显示产品列表
# - 联系页是否能提交询盘

# 5. 测试后台页面
# 浏览器打开 http://localhost:3001/admin/login
# 用 admin/admin123 登录，检查：
# - 仪表盘是否正常
# - 产品管理列表是否显示
# - 能否新增/编辑/删除产品
```

---

## 常见问题

### 后端相关

#### Q: 启动报错 `Communications link failure`（数据库连接失败）

检查 MySQL 是否运行，以及 `application.yml` 中的数据库连接配置是否正确：
```bash
mysql -u root -p -e "SELECT 1"
```

#### Q: 启动报错 `Table 'xlx.xxx' doesn't exist`

数据库表未创建。手动执行建表脚本：
```bash
mysql -u root -p xlx -e "SHOW TABLES;"
# 如果表为空：
mysql -u root -p xlx < xlx-gateway/src/main/resources/db/migration/V1__init_schema.sql
mysql -u root -p xlx < xlx-gateway/src/main/resources/db/migration/V2__seed_data.sql
```

#### Q: 登录返回 `用户名或密码错误`

检查种子数据是否插入：
```bash
mysql -u root -p xlx -e "SELECT username, nickname FROM admin_user;"
```
默认账号：`admin` / `admin123`

#### Q: 上传文件报错 `FileSizeLimitExceededException`

检查 `application.yml` 中的文件大小限制：
```yaml
spring.servlet.multipart.max-file-size: 10MB
spring.servlet.multipart.max-request-size: 20MB
```

#### Q: 前端跨域请求被拒绝

CORS 配置在 `WebMvcConfig.java` 中，允许所有来源的 `/api/**` 请求。如果仍有问题，检查：
- 请求路径是否以 `/api` 开头
- 是否携带了不允许的请求头

### API 访问相关

#### Q: 访问 API 报 404

所有 API 路径必须以 `/api` 开头。后端配置了 `server.servlet.context-path: /api`。

```
错误：http://localhost:8080/public/config
正确：http://localhost:8080/api/public/config
```

前台和后台的 Vite 开发服务器已配置代理，通过 `http://localhost:3000/api/...` 访问会自动转发到后端。

#### Q: 访问 API 报 500

通常是数据库连接问题。检查：
1. MySQL 是否已启动
2. 数据库密码是否正确（默认 `root`，可通过 `DB_PASSWORD` 环境变量覆盖）
3. 数据库 `xlx` 是否存在，表是否已创建

```bash
# 检查 MySQL 连接
mysql -u root -p -e "USE xlx; SHOW TABLES;"
```

### 前端相关

#### Q: 前台/后台页面空白或报错

确保后端已启动且数据库正常。前端页面加载时会调用后端 API 获取数据，后端不可用时会显示空白或错误。

#### Q: npm install 失败

```bash
# 清除缓存重试
rm -rf node_modules package-lock.json
npm install
```

#### Q: VPN 影响

VPN 一般不会影响 localhost 访问。如果遇到问题，检查：
- 是否有代理软件占用了 8080/3000/3001 端口
- 尝试关闭 VPN 后重试

#### Q: 端口被占用

```bash
# 检查端口占用
lsof -i :8080
lsof -i :3000
lsof -i :3001

# 或修改端口
# 后端：编辑 xlx-gateway/src/main/resources/application.yml 的 server.port
# 前台：编辑 xlx-web/vite.config.ts 的 server.port
# 后台：编辑 xlx-admin/vite.config.ts 的 server.port
```

---

## 前端项目

本项目与前端完全分离，前端为独立项目：

- `xlx-web/` — Vue 3 + Vite + Tailwind CSS（前台展示，端口 3000）
- `xlx-admin/` — Vue 3 + Vite + Element Plus（后台管理，端口 3001）

前端通过 HTTP API 与本项目通信，Vite 开发服务器已配置代理：
- `/api/**` → `http://localhost:8080`
- `/uploads/**` → `http://localhost:8080`
