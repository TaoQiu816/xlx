# 会话交接文档

> 最后更新：2026-06-06
> 最后会话：V2.6.1 前台 API 缓存优化

---

## 1. 当前项目目标

鑫连鑫丝网厂 B2B 官网与询盘管理系统。核心能力包括中英文产品展示、后台内容维护、单产品询盘、多产品询价车、文件上传、邮件通知和询盘导出。当前阶段：V2.5 开发收口已完成，可进入真实内容录入与本地试运行。

## 2. 已确认技术栈

| 层 | 技术 |
|----|------|
| 后端 | Spring Boot 3.2.5 + Java 17 + MyBatis Plus 3.5.6 + Maven 多模块 |
| 数据库 | MySQL 8.0，Flyway 迁移（V1-V11） |
| 鉴权 | JWT 单管理员，BCrypt 密码 |
| 前台 | Vue 3 + Vite + Tailwind CSS + vue-i18n@9，端口 3000 |
| 后台 | Vue 3 + Vite + Element Plus + ECharts，端口 3001 |
| 国际化 | vue-i18n@9 Composition API 模式，localStorage 持久化 |
| 邮件 | spring-boot-starter-mail + EasyExcel 4.0.3 |

## 3. 已完成阶段

| 阶段 | 说明 | 状态 |
|------|------|------|
| V1 | 核心功能（前台 8 页面 + 后台管理 + 询盘系统） | ✅ |
| V1.2 | 界面美化（登录页重写、Dashboard 图表、产品卡片动画等） | ✅ |
| V1.3 | SEO 优化（useHead、JSON-LD、robots.txt、sitemap.xml） | ✅ |
| V2.1 | 中英文切换（vue-i18n、langField、语言切换器、SiteConfig 英文字段） | ✅ |
| V2.2 | 询价车 + 多产品询盘 + 收口优化 | ✅ 已关闭 |
| V2.3 | 询盘通知 + Excel 导出 + 一键复制 + 后台效率增强 | ✅ 已关闭 |
| V2.4 | 上线前安全修复 + 内容录入准备 + 功能盘点 + 预期评估 | ✅ 已关闭 |
| V2.5 | 开发收口 + About 页面迁移 + SQL 日志可配置 + SEO 占位域名 | ✅ 已关闭 |
| V2.6 | 本地试运行与后台操作验证 | ✅ 进行中 |
| V2.6.1 | 前台 API 缓存优化（sessionStorage + 请求去重） | ✅ |

## 4. Flyway 迁移版本

| 版本 | 文件 | 内容 |
|------|------|------|
| V1 | init_schema.sql | 10 张表建表 |
| V2 | seed_data.sql | 管理员、10 个分类、12 个配置项 |
| V3 | add_selling_points_config.sql | 卖点 sp1-sp4、Hero 统计 |
| V4 | add_faq_config.sql | 首页 FAQ 配置 |
| V5 | add_faq_page_groups_config.sql | FAQ 页面分组配置 |
| V6 | add_media_type_to_factory_image.sql | 工厂图片视频类型支持 |
| V7 | add_seo_config_fields.sql | SEO 配置字段 |
| V8 | add_english_site_config.sql | 英文 SiteConfig 字段 |
| V9 | add_inquiry_item.sql | 询盘产品明细表 |
| V10 | add_mail_config.sql | 邮件通知配置（8 项） |
| V11 | add_about_page_config.sql | 关于我们页面配置（10 项） |

## 5. 国际化架构

### 三层语言适配

| 层 | 工具 | 文件 |
|---|------|------|
| UI 标签 | `vue-i18n` `$t()` | `locales/zh.ts`、`locales/en.ts` |
| 实体字段 | `langField(obj, 'base')` | `composables/useLangField.ts` |
| SiteConfig | 动态 `_en` 后缀 | 各页面组件 |

### 语言切换

- Header 中 EN/中 按钮，调用 `setLocale('en' | 'zh')`
- 持久化到 localStorage（key: `xlx_lang`）
- 默认中文，回退中文

### 实体字段双语模式

Product、ProductCategory、ProductSpec、Certificate、FactoryImage 等表使用 `*Cn/*En` 字段模式。

## 6. 当前功能全景

### 前台展示（xlx-web，9 个页面）

| 页面 | 路由 | 状态 |
|------|------|------|
| 首页 | `/` | ✅ |
| 关于我们 | `/about` | ✅ SiteConfig 驱动 |
| 产品中心 | `/products` | ✅ |
| 产品详情 | `/products/:slug` | ✅ |
| 工厂展示 | `/factory` | ✅ |
| 证书展示 | `/certificates` | ✅ |
| FAQ | `/faq` | ✅ |
| 联系我们 | `/contact` | ✅ |
| 询价车 | `/quote-cart` | ✅ |

### 后台管理（xlx-admin，11 个页面）

| 页面 | 路由 | 状态 |
|------|------|------|
| 登录 | `/login` | ✅ |
| Dashboard | `/dashboard` | ✅ |
| 分类管理 | `/categories` | ✅ |
| 产品管理 | `/products` | ✅ |
| 产品编辑 | `/products/create` `/products/:id/edit` | ✅ |
| 工厂图片 | `/factory-images` | ✅ |
| 证书管理 | `/certificates` | ✅ |
| 询盘列表 | `/inquiries` | ✅ |
| 询盘详情 | `/inquiries/:id` | ✅ |
| 站点配置 | `/site-config` | ✅ 10 个配置区 |
| 管理员管理 | `/admin-users` | ✅ |

## 7. 内容录入入口确认

| 内容类型 | 入口 | 状态 |
|----------|------|------|
| 网站基本信息 | 站点配置 → 基本信息 | ✅ |
| 联系方式 | 站点配置 → 联系方式 | ✅ |
| 首页 Banner | 站点配置 → 首页 Banner | ✅ |
| Hero 统计数字 | 站点配置 → Hero 统计数字 | ✅ |
| 卖点区块 | 站点配置 → 首页卖点区块 | ✅ |
| 首页 FAQ | 站点配置 → 首页 FAQ | ✅ |
| FAQ 页面分组 | 站点配置 → FAQ 页面分组 | ✅ |
| SEO 配置 | 站点配置 → SEO 配置 | ✅ |
| 邮件通知配置 | 站点配置 → 邮件通知配置 | ✅ |
| 关于我们页面 | 站点配置 → 关于我们页面 | ✅ V2.5 新增 |
| 产品分类 | 分类管理 | ✅ |
| 产品信息 | 产品管理 | ✅ |
| 产品规格 | 产品编辑 → 规格标签页 | ✅ |
| 产品图片 | 产品编辑 → 图片标签页 | ✅ |
| 工厂图片 | 工厂图片管理 | ✅ |
| 证书管理 | 证书管理 | ✅ |

## 8. 技术组件取舍

### 当前不需要（不纳入近期规划）

| 组件 | 不需要的原因 |
|------|-------------|
| Elasticsearch | 产品量不大，MySQL LIKE 够用，引入后增加运维成本 |
| 消息队列 | 询盘量不大，邮件通知已用 @Async 异步，无需 MQ 解耦 |
| AI 客服/推荐/文案/报价 | 丝网产品报价需人工确认，AI 不能直接提升核心业务闭环 |
| 复杂 CRM | 当前后台询盘管理已够用，客户量不足以支撑 |
| 大数据分析 | 当前数据量不足以支撑 |
| 微服务 | 单体应用足够，团队规模小 |

### 当前阶段暂不接入（未来可按需接入）

| 组件 | 暂不接入的原因 | 后续触发条件 |
|------|----------------|-------------|
| Redis | 当前访问量和数据量不大，MySQL 足够 | 产品 > 1000 或 QPS > 100 |
| OSS/COS | 当前本地 uploads 足够 | 文件 > 10GB 或需 CDN |
| CDN | 域名未确认 | 域名确认后上线时配置 |
| Docker/Nginx/CI-CD | 属于部署阶段 | 服务器确认后 |

## 9. 当前待处理事项

| 事项 | 优先级 | 说明 |
|------|--------|------|
| 录入真实内容 | CRITICAL | 按清单在后台录入真实产品、图片、证书等 |
| 配置服务器环境 | HIGH | MySQL、Nginx、HTTPS、环境变量 |
| 替换 sitemap/robots.txt 域名 | HIGH | 当前为 YOUR_DOMAIN 占位 |
| 管理员密码修改 | HIGH | 默认 admin123 需改为强密码 |

## 10. 上线前必须配置

| 配置项 | 当前值 | 目标值 |
|--------|--------|--------|
| JWT_SECRET | 默认值 | 随机 64 字节 |
| DB_PASSWORD | root | 强密码 |
| CORS_ORIGINS | localhost | 生产域名 |
| MYBATIS_LOG_IMPL | StdOutImpl | 生产环境关闭或设为 WARN |
| 管理员密码 | admin123 | 强密码 |
| MySQL 用户 | root | 专用用户 xlx_app |
| SMTP 配置 | 空 | 完整 SMTP 信息 |
| sitemap.xml | YOUR_DOMAIN | 实际域名 |
| robots.txt | YOUR_DOMAIN | 实际域名 |

## 11. 当前测试结果

| 项目 | 结果 |
|------|------|
| xlx-api | BUILD SUCCESS，27 测试全部通过 |
| xlx-admin | BUILD SUCCESS |
| xlx-web | BUILD SUCCESS |

### 后端测试明细

| 测试类 | 模块 | 数量 |
|--------|------|------|
| InquiryServiceTest | xlx-inquiry | 15 |
| AuthServiceTest | xlx-auth | 4 |
| FileUploadServiceTest | xlx-file | 8 |
| **合计** | | **27** |

## 12. 必须遵守的限制

1. **Flyway 已执行的迁移文件（V1-V11）永远不要修改**
2. 需要改表结构时创建新版本文件（V12、V13...）
3. 产品数据从数据库读取，不得写死
4. 文件上传限制格式和大小（单文件 ≤ 10MB）
5. 后台接口必须 JWT 鉴权
6. 前台接口只开放必要展示数据
7. 所有列表页需要分页
8. 所有后台删除操作必须二次确认
9. 所有表单必须做基础校验
10. Controller 的 `@RequestMapping` 不写 `/api` 前缀

## 13. 启动命令

| 服务 | 命令 | 端口 |
|------|------|------|
| 后端 | IDEA 运行 `XlxApiApplication.java` 或 `mvn spring-boot:run -pl xlx-gateway` | 8080 |
| 前台 | `cd xlx-web && npm run dev` | 3000 |
| 后台 | `cd xlx-admin && npm run dev` | 3001 |

## 14. 后续给 Claude Code 的执行提示词

```
XLX 项目当前状态：
- V1-V2.6.1 已完成
- 中英文切换已实现（vue-i18n + langField + SiteConfig 英文字段）
- 询价车已实现（useQuoteCart + localStorage + 多产品询盘提交）
- 邮件通知已实现（site_config 存储 SMTP 配置，@Async 异步发送）
- Excel 导出已实现（EasyExcel，GET /api/admin/inquiries/export，JWT 鉴权）
- 一键复制已实现（navigator.clipboard + textarea fallback）
- About 页面已迁移至 SiteConfig（V11 迁移），支持中英文
- SQL 日志可通过 MYBATIS_LOG_IMPL 环境变量控制
- sitemap.xml/robots.txt 使用 YOUR_DOMAIN 占位，待域名确认后替换
- Flyway 迁移 V1-V11，不要修改已执行的迁移文件
- 后端 Controller @RequestMapping 不写 /api 前缀
- 后端共 27 个测试（InquiryServiceTest=15, AuthServiceTest=4, FileUploadServiceTest=8）
- 前台 API 缓存已实现（xlx-web/src/utils/apiCache.ts，sessionStorage + 请求去重）
- 缓存范围：config/categories/products/factory-images/certificates（TTL 3-10分钟）
- 缓存不覆盖：POST 询盘、文件上传、后台接口
- @unhead/vue 使用 createHead()（从 @unhead/vue/client 导入），不再用 VueHeadMixin
- admin Dashboard API 路径已修复（去掉重复 /admin 前缀）
- Vite allowedHosts 通过 VITE_ALLOWED_HOSTS 环境变量配置，默认 localhost/127.0.0.1
- .env.example 已创建（xlx-web 和 xlx-admin）
- Flyway V7/V8 已核查：已成功执行，checksum 一致，无 mismatch 风险

当前阶段：真实内容录入与本地试运行
- 按 docs/真实内容录入清单.md 逐项录入
- 按 docs/真实内容录入入口与操作说明.md 操作
- 录入完成后按 docs/真实内容录入验收清单.md 验收（88 项）
- 上线前需配置环境变量：JWT_SECRET / DB_PASSWORD / CORS_ORIGINS / MYBATIS_LOG_IMPL

不需要的技术组件（不纳入近期规划）：
- Elasticsearch（产品量不大，MySQL LIKE 够用）
- 消息队列（询盘量不大，@Async 已够用）
- AI 客服/推荐/文案/报价（丝网产品报价需人工确认）
- 复杂 CRM（当前询盘管理已够用）
- 大数据分析（当前数据量不足）

暂不接入的组件（未来可按需接入）：
- Redis（当前访问量不大，MySQL 足够）
- OSS/COS（当前本地 uploads 足够）
- CDN/Docker/Nginx/CI-CD（属于部署阶段，服务器确认后）

执行时遵守：
- 不修改已执行的 Flyway 迁移文件
- 改表结构用新迁移文件（V12、V13...）
- 后台接口必须 JWT 鉴权
- 删除操作二次确认，表单基础校验
- 先阅读 docs/技术方案与需求确认.md
```
