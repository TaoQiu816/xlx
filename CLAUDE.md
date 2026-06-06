# CLAUDE.md

## 项目定位

鑫连鑫丝网厂 — B2B 丝网工厂官网与销售系统。V1 实现中文前台展示、中文后台管理、产品管理、规格管理、图片管理、证书管理、询盘管理和文件上传。

## 技术栈

- 后端：Spring Boot 3.x + MyBatis Plus + Java 17 + Maven
- 前台：Vue 3 + Vite + Tailwind CSS
- 后台：Vue 3 + Vite + Element Plus
- 数据库：MySQL 8.0（开发阶段可临时兼容 5.7）
- 鉴权：JWT（单管理员）
- 文件存储：本地 uploads 目录，预留 OSS 接口

## 部署路径

- `/` — 前台展示网站（xlx-web）
- `/admin` — 后台管理系统（xlx-admin），Vite base: `/admin/`
- `/api` — 后端接口（xlx-api），前缀 `/api/public/**` 和 `/api/admin/**`
- `/uploads` — 上传文件静态访问

## 已完成阶段

| 阶段 | 说明 |
|------|------|
| V1 | 核心功能（前台 8 页面 + 后台管理 + 询盘系统） |
| V1.2 | 界面美化（登录页重写、Dashboard 图表、产品卡片动画等） |
| V1.3 | SEO 优化（useHead、JSON-LD、robots.txt、sitemap.xml） |
| V2.1 | 中英文切换（vue-i18n、langField、语言切换器、SiteConfig 英文字段） |

## 待实现功能

- 在线支付（支付宝、微信支付、PayPal、信用卡）
- 购物车 / 询价车
- 客户注册 / 客户中心
- 多角色权限
- 自动报价
- AI 客服 / ERP 对接

## 开发规则

1. 不要一次性实现 V2/V3/V4 功能。
2. 每次改动前先阅读 docs/ 技术方案与需求确认.md。
3. 产品数据不得写死，必须从数据库读取。
4. 图片和文件上传必须限制格式和大小（单文件 ≤ 10MB）。
5. 后台接口必须 JWT 鉴权。
6. 前台接口只开放必要展示数据。
7. 所有列表页需要分页。
8. 所有后台删除操作必须二次确认。
9. 所有表单必须做基础校验。

## Flyway 迁移规则（关键）

**已执行的迁移文件（V1-V8）永远不要修改。** 修改已执行的迁移文件会导致校验和不匹配，后端无法启动。

需要改表结构时，必须创建新的迁移文件：

```
db/migration/
├── V1__init_schema.sql        ← 已执行，不动
├── V2__seed_data.sql          ← 已执行，不动
├── V3__add_selling_points_config.sql ← 已执行，不动
├── V4__add_faq_config.sql     ← 已执行，不动
├── V5__add_admin_user_fields.sql ← 已执行，不动
├── V6__add_inquiry_fields.sql ← 已执行，不动
├── V7__add_seo_config.sql     ← 已执行，不动
├── V8__add_english_site_config.sql ← 已执行，不动
└── V9__xxx.sql                ← 新增变更用新文件
```

文件名格式：`V{版本号}__{描述}.sql`，版本号递增，不可跳跃或重复。

## 项目结构

- `xlx-api/` — Spring Boot 后端
- `xlx-web/` — Vue 3 前台展示
- `xlx-admin/` — Vue 3 后台管理（Vite base: /admin/）
- `uploads/` — 上传文件
- `docs/` — 文档

## 页面风格

前台：简洁、工业风、B2B、工厂感、移动端友好，不使用复杂动画和廉价模板。
后台：中文界面，菜单简单，表单清楚，适合非技术人员使用。
