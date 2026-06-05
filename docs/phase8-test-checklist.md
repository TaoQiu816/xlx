# Phase 8: 测试与修复

> 执行时间：2026-06-05
> 状态：已完成

---

## 1. 测试覆盖总结

### 1.1 后端单元测试

| 模块 | 测试类 | 用例数 | 状态 |
|------|--------|--------|------|
| xlx-auth | AuthServiceTest | 4 | ✅ 通过 |
| xlx-inquiry | InquiryServiceTest | 10 | ✅ 通过 |
| xlx-file | FileUploadServiceTest | 8 | ✅ 通过 |
| **合计** | | **22** | **全部通过** |

### 1.2 测试运行命令

```bash
cd xlx-api

# 运行所有测试
mvn clean test

# 运行单个模块测试
mvn test -pl xlx-auth
mvn test -pl xlx-inquiry
mvn test -pl xlx-file
```

### 1.3 前端构建验证

| 项目 | 状态 | 构建时间 |
|------|------|----------|
| xlx-web | ✅ 构建成功 | ~900ms |
| xlx-admin | ✅ 构建成功 | ~2.6s |

```bash
cd xlx-web && npm run build
cd xlx-admin && npm run build
```

---

## 2. Bug 修复清单

### 2.1 已修复

| Bug | 文件 | 修复内容 |
|-----|------|----------|
| 上传 URL 字段名错误 | `xlx-web/src/views/Contact.vue:29` | `uploadRes.data?.url` → `uploadRes.data?.fileUrl` |
| 空 catch 块无错误提示 | `xlx-web/src/views/Contact.vue:36` | 添加 errorMessage ref 和错误提示 UI |
| 空 catch 块无错误提示 | `xlx-web/src/views/ProductDetail.vue:42,51` | 添加 errorMessage ref 和错误提示 UI |
| Dashboard 表格字段 snake_case | `xlx-admin/src/views/Dashboard.vue:35,40` | `created_at` → `createdAt`, `product_name` → `productName` |

---

## 3. 手动测试清单

### 3.1 前台（xlx-web）

- [ ] 首页加载：分类、推荐产品、工厂图片、证书正常显示
- [ ] 产品列表：分页正常、分类筛选正常
- [ ] 产品详情：图片画廊、规格表、询盘按钮正常
- [ ] 联系我们：表单提交成功（必填姓名）
- [ ] 联系我们：提交失败时显示错误提示
- [ ] 联系我们：文件上传正常（PDF、图片）
- [ ] 联系我们：超大文件被拒绝
- [ ] 工厂展示：图片画廊正常
- [ ] 证书展示：图片正常
- [ ] 常见问题：手风琴展开/收起正常
- [ ] 移动端：导航菜单汉堡按钮正常
- [ ] 移动端：所有页面在 375px 宽度正常显示
- [ ] 移动端：规格表可横向滚动

### 3.2 后台（xlx-admin）

- [ ] 登录：admin/admin123 登录成功
- [ ] 控制台：统计数据显示正确
- [ ] 产品管理：增删改查正常
- [ ] 分类管理：增删改查正常
- [ ] 工厂图片：上传、删除正常
- [ ] 证书管理：上传、删除正常
- [ ] 询盘管理：列表分页正常
- [ ] 询盘详情：状态修改正常
- [ ] 询盘详情：备注保存正常
- [ ] 询盘删除：二次确认正常
- [ ] 网站配置：编辑保存正常
- [ ] 移动端：侧边栏隐藏/显示正常

### 3.3 API 接口（curl）

```bash
# 1. 获取网站配置
curl http://localhost:8080/api/public/config

# 2. 获取产品分类
curl http://localhost:8080/api/public/categories

# 3. 获取产品列表
curl "http://localhost:8080/api/public/products?page=1&size=10"

# 4. 获取产品详情
curl http://localhost:8080/api/public/products/steel-wire-mesh

# 5. 提交询盘（空 name 应返回错误）
curl -X POST http://localhost:8080/api/public/inquiries \
  -H "Content-Type: application/json" \
  -d '{"name":"","email":"test@example.com"}'

# 6. 提交询盘（有效数据）
curl -X POST http://localhost:8080/api/public/inquiries \
  -H "Content-Type: application/json" \
  -d '{"name":"张三","email":"test@example.com","company":"测试公司"}'

# 7. 登录获取 token
curl -X POST http://localhost:8080/api/admin/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"admin","password":"admin123"}'

# 8. 获取询盘列表（需 token）
curl -H "Authorization: Bearer <token>" \
  "http://localhost:8080/api/admin/inquiries?page=1&size=10"

# 9. 修改询盘状态（需 token）
curl -X PUT -H "Authorization: Bearer <token>" \
  -H "Content-Type: application/json" \
  -d '{"status":"contacted"}' \
  http://localhost:8080/api/admin/inquiries/1/status

# 10. 获取文件列表（需 token）
curl -H "Authorization: Bearer <token>" \
  "http://localhost:8080/api/admin/upload?page=1&size=10"

# 11. 删除文件（需 token）
curl -X DELETE -H "Authorization: Bearer <token>" \
  http://localhost:8080/api/admin/upload/1
```

---

## 4. 测试数据初始化

### 4.1 数据库初始化

```bash
# 1. 创建数据库
mysql -u root -proot -e "CREATE DATABASE IF NOT EXISTS xlx DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;"

# 2. 启动后端，Flyway 自动建表 + 种子数据
cd xlx-api && mvn spring-boot:run -pl xlx-gateway
```

### 4.2 种子数据

Flyway V2 脚本会自动插入：
- 管理员账户：admin / admin123
- 默认产品分类
- 网站配置项

---

## 5. 测试覆盖率

### 5.1 目标

- 后端单元测试覆盖率：≥80%（Service 层）
- 前端：构建成功，无 TypeScript 错误

### 5.2 当前状态

| 模块 | 覆盖方法 | 总方法 | 覆盖率 |
|------|----------|--------|--------|
| AuthService | 2 | 2 | 100% |
| InquiryService | 6 | 6 | 100% |
| FileUploadService | 4 | 4 | 100% |

### 5.3 未覆盖

- Controller 层：需要 `@WebMvcTest` + MockBean，复杂度较高
- Mapper 层：MyBatis Plus 基础 CRUD，无需测试
- 实体类：纯 POJO，无需测试

---

## 6. 文档清单

| 文档 | 路径 | 内容 |
|------|------|------|
| Phase 6 文档 | `docs/phase6-inquiry-file-upload.md` | 询盘与文件上传闭环 |
| Phase 7 文档 | `docs/phase7-mobile-adaptation.md` | 移动端适配 |
| Phase 8 文档 | `docs/phase8-test-checklist.md` | 本文档 |
| 架构文档 | `xlx-api/ARCHITECTURE.md` | 完整架构参考 |

---

## 7. 后续优化建议

| 优先级 | 建议 | 说明 |
|--------|------|------|
| P2 | 添加 Controller 层集成测试 | 使用 @WebMvcTest + MockBean |
| P2 | 添加前端单元测试 | Vitest + Vue Test Utils |
| P2 | 添加 E2E 测试 | Playwright 关键流程测试 |
| P3 | 集成 CI/CD | GitHub Actions 自动运行测试 |
