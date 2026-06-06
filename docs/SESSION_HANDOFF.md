# 会话交接文档

> 最后更新：2026-06-06
> 最后会话：V2.3 收口验收

---

## 1. 当前项目目标

鑫连鑫丝网厂（南通）B2B 官网与销售系统。当前阶段：V2.3 询盘通知与后台效率增强已完成，可进入下一阶段。

## 2. 已确认技术栈

| 层 | 技术 |
|----|------|
| 后端 | Spring Boot 3.2.5 + Java 17 + MyBatis Plus 3.5.6 + Maven 多模块 |
| 数据库 | MySQL 8.0，Flyway 迁移（V1-V10） |
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

## 4. Flyway 迁移版本

| 版本 | 文件 | 内容 |
|------|------|------|
| V1 | init_schema.sql | 10 张表建表 |
| V2 | seed_data.sql | 管理员、10 个分类、12 个配置项 |
| V3 | add_selling_points_config.sql | 卖点 sp1-sp4、Hero 统计 |
| V4 | add_faq_config.sql | 首页 FAQ 配置 |
| V5 | add_admin_user_fields.sql | 管理员昵称+角色+状态 |
| V6 | add_inquiry_fields.sql | 询盘扩展字段 |
| V7 | add_seo_config.sql | SEO 配置项 |
| V8 | add_english_site_config.sql | 英文 SiteConfig 字段 |
| V9 | add_inquiry_item.sql | 询盘产品明细表 |
| V10 | add_mail_config.sql | 邮件通知配置（8 项） |

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

## 6. V2.3 新增能力

### 邮件通知

- 管理后台「站点配置 → 邮件通知配置」中开启和配置 SMTP
- 客户提交询盘后异步发送 HTML 邮件通知管理员
- 默认关闭，配置不完整或发送失败不影响询盘提交
- 邮件内容：客户信息 + 产品明细 + 留言

### Excel 导出

- 后台询盘列表页「导出 Excel」按钮
- 支持当前筛选条件（状态、关键词、日期范围）
- 文件名带日期（如 `询盘列表_20260606.xlsx`）
- 15 列：编号、时间、姓名、公司、邮箱、电话、微信、WhatsApp、国家、产品、数量、规格、留言、状态、备注

### 一键复制

- 询盘详情页「复制联系方式」按钮
- 复制内容：客户、公司、电话、邮箱、微信、WhatsApp、国家
- `navigator.clipboard` + textarea fallback

## 7. 当前待处理事项

| 事项 | 优先级 | 说明 |
|------|--------|------|
| 产品规格参数动态化 | MEDIUM | 当前规格为静态 key-value，可支持动态规格组 |
| 工厂展示视频 | LOW | 当前仅有图片，可增加视频上传和播放 |
| 后台 UI 拥挤问题 | MEDIUM | 用户截图反馈间距不足（V1.2 遗留） |
| About 页面数据迁移 | LOW | 当前使用硬编码文字+翻译 key，可迁移至 SiteConfig |

## 8. 当前测试结果

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

## 9. 后续可选阶段

| 阶段 | 说明 |
|------|------|
| V2.4 | 产品规格动态化 / 工厂视频 / 后台 UI 优化 |
| V3 | 订单/支付系统 |
| V4 | AI 客服/ERP 对接 |

## 10. 必须遵守的限制

1. **Flyway 已执行的迁移文件（V1-V10）永远不要修改**
2. 需要改表结构时创建新版本文件（V11、V12...）
3. 产品数据从数据库读取，不得写死
4. 文件上传限制格式和大小（单文件 ≤ 10MB）
5. 后台接口必须 JWT 鉴权
6. 前台接口只开放必要展示数据
7. 所有列表页需要分页
8. 所有后台删除操作必须二次确认
9. 所有表单必须做基础校验
10. Controller 的 `@RequestMapping` 不写 `/api` 前缀

## 11. 启动命令

| 服务 | 命令 | 端口 |
|------|------|------|
| 后端 | IDEA 运行 `XlxApiApplication.java` 或 `mvn spring-boot:run -pl xlx-gateway` | 8080 |
| 前台 | `cd xlx-web && npm run dev` | 3000 |
| 后台 | `cd xlx-admin && npm run dev` | 3001 |

## 12. 后续给 Claude Code 的执行提示词

```
XLX 项目当前状态：
- V1 + V1.2 + V1.3 + V2.1 + V2.2 + V2.3 已完成
- 中英文切换已实现（vue-i18n + langField + SiteConfig 英文字段）
- 询价车已实现（useQuoteCart + localStorage + 多产品询盘提交）
- 邮件通知已实现（site_config 存储 SMTP 配置，@Async 异步发送）
- Excel 导出已实现（EasyExcel，GET /api/admin/inquiries/export，JWT 鉴权）
- 一键复制已实现（navigator.clipboard + textarea fallback）
- Flyway 迁移 V1-V10，不要修改已执行的迁移文件
- 后端 Controller @RequestMapping 不写 /api 前缀
- 后端共 27 个测试（InquiryServiceTest=15, AuthServiceTest=4, FileUploadServiceTest=8）

当前待处理：
1. 产品规格参数动态化
2. 工厂展示视频
3. 后台 UI 拥挤问题

执行时遵守：
- 不修改已执行的 Flyway 迁移文件
- 改表结构用新迁移文件（V11、V12...）
- 后台接口必须 JWT 鉴权
- 删除操作二次确认，表单基础校验
- 先阅读 docs/技术方案与需求确认.md
```
