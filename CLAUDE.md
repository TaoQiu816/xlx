# CLAUDE.md

## 项目定位

鑫连鑫丝网厂 B2B 官网与询盘管理系统。核心能力包括中英文产品展示、后台内容维护、单产品询盘、多产品询价车、文件上传、邮件通知和询盘导出。不是 AI 系统、电商平台、ERP、CRM 或自动报价系统。

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
| V2.2 | 询价车 + 多产品询盘 + 收口优化 |
| V2.3 | 询盘通知 + Excel 导出 + 一键复制 + 后台效率增强 |
| V2.4 | 上线前安全修复 + 内容录入准备 + 功能盘点 + 预期评估 |
| V2.5 | 开发收口 + About 页面迁移 + SQL 日志可配置 + SEO 占位域名 |
| V2.6 | 本地试运行与后台操作验证 |
| V2.6.1 | 前台 API 缓存优化（sessionStorage + 请求去重） |

## 不需要的功能（不纳入近期规划）

- AI 客服 / AI 推荐 / AI 自动文案 / AI 自动报价 — 丝网产品报价需人工确认，AI 不能直接提升核心业务闭环
- Elasticsearch — 产品量不大，MySQL LIKE 够用
- 消息队列 — 询盘量不大，@Async 已够用
- 复杂 CRM / 大数据分析 — 当前数据量和客户量不足
- 微服务 / GraphQL — 单体应用足够

## 暂不接入的功能（未来可按需接入）

- Redis 缓存 — 当前访问量不大，MySQL 足够
- OSS/COS 对象存储 — 当前本地 uploads 足够
- CDN / Docker / Nginx / CI/CD — 属于部署阶段，服务器确认后
- 客户注册 / 客户中心 / 订单系统 / 支付系统 / 报价单系统 — 属于后续销售流程增强

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

**已执行的迁移文件（V1-V11）永远不要修改。** 修改已执行的迁移文件会导致校验和不匹配，后端无法启动。

需要改表结构时，必须创建新的迁移文件：

```
db/migration/
├── V1__init_schema.sql        ← 已执行，不动
├── V2__seed_data.sql          ← 已执行，不动
├── V3__add_selling_points_config.sql ← 已执行，不动
├── V4__add_faq_config.sql     ← 已执行，不动
├── V5__add_faq_page_groups_config.sql ← 已执行，不动
├── V6__add_media_type_to_factory_image.sql ← 已执行，不动
├── V7__add_seo_config_fields.sql ← 已执行，不动
├── V8__add_english_site_config.sql ← 已执行，不动
├── V9__add_inquiry_item.sql   ← 已执行，不动
├── V10__add_mail_config.sql   ← 已执行，不动
├── V11__add_about_page_config.sql ← 已执行，不动
└── V12__xxx.sql               ← 新增变更用新文件
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
