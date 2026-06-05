# 会话交接文档

> 生成时间：2026-06-05
> 最后会话上下文：Phase 6-8 完成（询盘闭环、移动端适配、测试与修复）

---

## 1. 当前项目目标

鑫连鑫丝网厂（南通）的 B2B 官网与销售系统，V1 目标是中文前台展示 + 后台管理 + 询盘系统。

## 2. 已确认技术栈

| 层 | 技术 |
|----|------|
| 后端 | Spring Boot 3.2.5 + Java 17 + MyBatis Plus 3.5.6 + Maven |
| 数据库 | MySQL 8.0（开发兼容 5.7） |
| 鉴权 | JWT（单管理员） |
| 数据库迁移 | Flyway（刚引入） |
| 前台 | Vue 3 + Vite + Tailwind CSS（端口 3000） |
| 后台 | Vue 3 + Vite + Element Plus（端口 3001） |

## 3. V1 功能边界

**V1 做**：中文前台 8 页面、后台管理、产品/分类/规格/图片 CRUD、工厂图片、证书、询盘、文件上传、移动端适配、中英文字段预留

**V1 不做**（严格排除）：
- 在线支付（支付宝/微信/PayPal/信用卡）
- 购物车 / 询价车
- 客户注册 / 客户中心
- 多角色权限
- 多语言切换（仅预留字段）
- 自动报价
- AI 客服 / ERP 对接

## 4. 当前已完成的修改

### 4.1 模块化重构（已完成）

原单体 `xlx-api/src/` 已拆分为 8 个 Maven 模块：
```
xlx-common → xlx-auth / xlx-product / xlx-content / xlx-inquiry / xlx-file / xlx-config → xlx-gateway
```
旧 `xlx-api/src/` 目录已删除，所有代码已迁移到新模块结构。`mvn clean compile` 通过。

### 4.2 路由路径修复（已完成，关键）

**问题**：`application.yml` 配置了 `context-path: /api`，但所有 Controller 的 `@RequestMapping` 也写了 `/api/...`，导致实际路径变成 `/api/api/...`。

**修复**：所有 9 个 Controller 和 WebMvcConfig 拦截器路径已去掉多余的 `/api` 前缀：
- `PublicController`: `/api/public` → `/public`
- `AuthController`: `/api/admin/auth` → `/admin/auth`
- `CategoryController`: `/api/admin/categories` → `/admin/categories`
- `ProductController`: `/api/admin/products` → `/admin/products`
- `FactoryImageController`: `/api/admin/factory-images` → `/admin/factory-images`
- `CertificateController`: `/api/admin/certificates` → `/admin/certificates`
- `InquiryController`: `/api/admin/inquiries` → `/admin/inquiries`
- `UploadController`: `/api/admin/upload` → `/admin/upload`
- `SiteConfigController`: `/api/admin/site-config` → `/admin/site-config`
- `WebMvcConfig` 拦截器: `/api/admin/**` → `/admin/**`

### 4.3 Flyway 引入（已完成）

- 在 `xlx-gateway/pom.xml` 添加了 `flyway-core` + `flyway-mysql` 依赖
- 在 `application.yml` 添加了 Flyway 配置（`enabled: true`, `baseline-on-migrate: true`）
- 从 `V1__init_schema.sql` 移除了 `CREATE DATABASE` 语句（Flyway 只管建表，库需手动建）
- 数据库必须手动创建：`CREATE DATABASE xlx DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;`
- 后端启动时 Flyway 自动建表 + 插入种子数据

### 4.4 公司名称修正（已完成）

全项目范围内修正：
- "安平县鑫联信金属丝网厂" → "鑫连鑫丝网厂"
- "安平县" → "南通市"
- "ANPING XINLIANXIN" → "XINLIANXIN"

涉及文件：ARCHITECTURE.md、README.md、技术方案与需求确认.md、xlx-admin/index.html、xlx-admin/src/views/Layout.vue、xlx-admin/src/views/Login.vue、xlx-web/index.html、xlx-web/src/components/Footer.vue、xlx-web/src/components/Header.vue、xlx-web/src/views/About.vue、xlx-web/src/views/Factory.vue、xlx-web/src/views/Home.vue

### 4.5 数据库文档（已完成）

ARCHITECTURE.md 的数据库章节已重写，包含全部 10 张表的字段级文档（字段名、类型、是否必填、默认值、说明）。

### 4.6 文档体系（已完成）

- `ARCHITECTURE.md`（914+ 行）：完整架构文档，含快速启动、本地开发、模块结构、数据库、API 参考、FAQ
- 8 个后端模块 README.md：各自职责、文件列表、API curl 示例、验证方法
- `xlx-web/README.md`、`xlx-admin/README.md`：前端启动说明

### 4.7 Logo 修复（已完成）

- Logo 文件已复制到 `xlx-web/public/logo.png`
- 两个 `index.html` 的 `<title>` 已修正为"鑫连鑫丝网厂"/"鑫连鑫后台管理"

### 4.8 Phase 6: 询盘与文件上传闭环（已完成）

**后端：**
- InquiryService 添加状态白名单校验（7 个有效状态值）
- Inquiry 实体添加 @NotBlank、@Email Bean Validation
- PublicController.submitInquiry 添加 @Valid 校验
- FileUploadService 添加 list()、delete() 方法
- UploadController 添加 GET /admin/upload（分页列表）、DELETE /admin/upload/{id}

**前端：**
- Contact.vue 修复空 catch 块，添加错误提示 UI
- Contact.vue 修复 uploadRes.data?.url → fileUrl 字段名
- ProductDetail.vue 修复空 catch 块，添加错误提示 UI

**测试：**
- InquiryServiceTest：10 个测试用例，全部通过
- FileUploadServiceTest：8 个测试用例，全部通过

### 4.9 Phase 7: 移动端适配（已完成）

**后台：**
- Layout.vue 添加移动端侧边栏（<768px 隐藏，汉堡菜单切换）
- Dashboard.vue 统计卡片响应式（xs=12, sm=12, md=6）
- Dashboard.vue 修复表格字段 snake_case → camelCase

**前台：**
- 所有 8 个页面已验证响应式布局
- Faq.vue 已适配（max-w-3xl mx-auto）

### 4.10 Phase 8: 测试与修复（已完成）

**测试：**
- AuthServiceTest：4 个测试用例，全部通过
- 总计 22 个单元测试，全部通过
- 前端构建验证：xlx-web ✅、xlx-admin ✅

**Bug 修复：**
- Contact.vue:29 uploadRes.data?.url → fileUrl
- Dashboard.vue:35,40 created_at/product_name → createdAt/productName

**文档：**
- docs/phase6-inquiry-file-upload.md
- docs/phase7-mobile-adaptation.md
- docs/phase8-test-checklist.md

## 5. 当前尚未开始的阶段

按 docs/技术方案与需求确认.md 的开发顺序：

1. ~~项目骨架初始化~~ ✅
2. ~~数据库设计与初始化~~ ✅
3. ~~后端接口开发~~ ✅（Phase 1-5 已完成）
4. ~~后台管理系统~~ ✅
5. ~~前台展示网站~~ ✅
6. ~~询盘与文件上传闭环~~ ✅
7. ~~移动端适配~~ ✅
8. ~~测试与修复~~ ✅

**V1 功能已全部完成。**

## 6. 下一步应该执行的任务

**V1 功能已全部完成。** 以下是可选的后续优化：

**可选优化：**
- 添加 Controller 层集成测试（@WebMvcTest）
- 添加前端单元测试（Vitest + Vue Test Utils）
- 添加 E2E 测试（Playwright 关键流程）
- 集成 CI/CD（GitHub Actions 自动运行测试）
- 添加文件内容 Magic Bytes 校验（防止扩展名绕过）
- Inquiry 引入 DTO 隔离（避免暴露内部字段）

**部署准备：**
- Docker Compose 配置
- Nginx 反向代理配置
- SSL 证书配置
- 生产环境数据库初始化

## 7. 必须遵守的限制

- 不实现 V2/V3/V4 功能（支付、购物车、客户注册、多角色权限等）
- 每次改动前先阅读 docs/技术方案与需求确认.md
- 产品数据从数据库读取，不得写死
- 图片和文件上传限制格式和大小（单文件 ≤ 10MB）
- 后台接口必须 JWT 鉴权
- 前台接口只开放必要展示数据
- 所有列表页需要分页
- 所有后台删除操作必须二次确认
- 所有表单必须做基础校验

## 8. 已确认不做的功能

- 在线支付（支付宝、微信支付、PayPal、信用卡）
- 购物车 / 询价车
- 客户注册 / 客户中心
- 多角色权限
- 多语言正式上线（数据字段预留，但不提供语言切换）
- 自动报价
- AI 客服 / ERP 对接

## 9. 当前配置注意事项

### 数据库初始化

```bash
# 1. 手动建库（Flyway 只建表，不建库）
mysql -u root -proot -e "CREATE DATABASE xlx DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;"

# 2. IDEA 重启后端，Flyway 自动建表 + 种子数据
```

### 启动方式

| 服务 | 方式 | 端口 |
|------|------|------|
| 后端 | IDEA 运行 `XlxApiApplication.java` | 8080 |
| 前台 | `cd xlx-web && npm run dev` | 3000 |
| 后台 | `cd xlx-admin && npm run dev` | 3001 |

### 关键配置（application.yml）

- `context-path: /api`：所有 Controller 的 `@RequestMapping` 不要写 `/api` 前缀
- `flyway.enabled: true` + `flyway.baseline-on-migrate: true`
- JWT 密钥：`${JWT_SECRET:xlx-jwt-secret-key-change-in-production}`
- 文件上传路径：`${UPLOAD_PATH:./uploads}`

### API 路径格式

```
实际完整 URL = context-path + @RequestMapping + 方法路径
即: /api       + /public       + /config    → /api/public/config
```

## 10. 后续给 Claude Code 的执行提示词

```
XLX 项目 V1 功能已全部完成。请先阅读 docs/技术方案与需求确认.md 了解 V1 范围。
当前状态：Phase 1-8 全部完成，22 个单元测试通过，前后端构建成功。
下一步：可选优化（Controller 集成测试、前端测试、E2E 测试、CI/CD）或部署准备（Docker、Nginx、SSL）。
注意：不实现 V2+ 功能，遵守 docs/SESSION_HANDOFF.md 中的所有限制。
```
