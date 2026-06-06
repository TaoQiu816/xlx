# 会话交接文档

> 最后更新：2026-06-06
> 最后会话：V2.1 收口验收 + V2.2 询价车规划

---

## 1. 当前项目目标

鑫连鑫丝网厂（南通）B2B 官网与销售系统。当前阶段：V2.1 中英文切换已完成，可进入下一阶段。

## 2. 已确认技术栈

| 层 | 技术 |
|----|------|
| 后端 | Spring Boot 3.2.5 + Java 17 + MyBatis Plus 3.5.6 + Maven 多模块 |
| 数据库 | MySQL 8.0，Flyway 迁移（V1-V8） |
| 鉴权 | JWT 单管理员，BCrypt 密码 |
| 前台 | Vue 3 + Vite + Tailwind CSS + vue-i18n@9，端口 3000 |
| 后台 | Vue 3 + Vite + Element Plus + ECharts，端口 3001 |
| 国际化 | vue-i18n@9 Composition API 模式，localStorage 持久化 |

## 3. 已完成阶段

| 阶段 | 说明 | 状态 |
|------|------|------|
| V1 | 核心功能（前台 8 页面 + 后台管理 + 询盘系统） | ✅ |
| V1.2 | 界面美化（登录页重写、Dashboard 图表、产品卡片动画等） | ✅ |
| V1.3 | SEO 优化（useHead、JSON-LD、robots.txt、sitemap.xml） | ✅ |
| V2.1 | 中英文切换（vue-i18n、langField、语言切换器、SiteConfig 英文字段） | ✅ |

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

## 6. 当前待处理事项

| 事项 | 优先级 | 说明 |
|------|--------|------|
| V2.2 询价车 | MEDIUM | 规划文档已创建（docs/V2.2询价车规划.md），待开发 |
| About 页面数据 | LOW | 当前使用硬编码文字+翻译 key，后续可迁移至 SiteConfig |
| 后台 UI 拥挤问题 | MEDIUM | 用户截图反馈间距不足（V1.2 遗留） |

## 7. 后续可选阶段

| 阶段 | 说明 |
|------|------|
| V2.2 | 询价车（规划文档：docs/V2.2询价车规划.md） |
| V2.3 | 客户注册/客户中心 |
| V3 | 订单/支付系统 |
| V4 | AI 客服/ERP 对接 |

## 8. 必须遵守的限制

1. **Flyway 已执行的迁移文件（V1-V8）永远不要修改**
2. 需要改表结构时创建新版本文件（V9、V10...）
3. 产品数据从数据库读取，不得写死
4. 文件上传限制格式和大小（单文件 ≤ 10MB）
5. 后台接口必须 JWT 鉴权
6. 前台接口只开放必要展示数据
7. 所有列表页需要分页
8. 所有后台删除操作必须二次确认
9. 所有表单必须做基础校验
10. Controller 的 `@RequestMapping` 不写 `/api` 前缀

## 9. 启动命令

| 服务 | 命令 | 端口 |
|------|------|------|
| 后端 | IDEA 运行 `XlxApiApplication.java` 或 `mvn spring-boot:run -pl xlx-gateway` | 8080 |
| 前台 | `cd xlx-web && npm run dev` | 3000 |
| 后台 | `cd xlx-admin && npm run dev` | 3001 |

## 10. 后续给 Claude Code 的执行提示词

```
XLX 项目当前状态：
- V1 + V1.2 + V1.3 + V2.1 已完成
- 中英文切换已实现（vue-i18n + langField + SiteConfig 英文字段）
- Flyway 迁移 V1-V8，不要修改已执行的迁移文件
- 后端 Controller @RequestMapping 不写 /api 前缀

当前待处理：
1. Faq.vue 数据库多语言扩展
2. 后台 UI 拥挤问题
3. About 页面数据迁移至 SiteConfig

执行时遵守：
- 不修改已执行的 Flyway 迁移文件
- 改表结构用新迁移文件（V9、V10...）
- 后台接口必须 JWT 鉴权
- 删除操作二次确认，表单基础校验
- 先阅读 docs/技术方案与需求确认.md
```
