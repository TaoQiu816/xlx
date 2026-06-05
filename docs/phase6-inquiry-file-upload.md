# Phase 6: 询盘与文件上传闭环

> 执行时间：2026-06-05
> 状态：已完成

---

## 1. 需求概述

Phase 6 的目标是完善询盘提交和文件上传功能的闭环，包括：

- 后端输入校验（防止非法数据写入）
- 前端错误处理（用户可感知提交结果）
- 文件管理 API（后台可查看/删除上传记录）
- 单元测试覆盖

### 1.1 修复的问题

| 级别 | 问题 | 修复方案 |
|------|------|----------|
| P1 | InquiryService.updateStatus() 接受任意字符串 | 添加状态白名单校验 |
| P1 | PublicController.submitInquiry() 无输入校验 | 添加 @Valid + Bean Validation |
| P1 | Contact.vue catch 块为空，用户无反馈 | 添加错误提示 UI |
| P1 | Contact.vue uploadRes.data?.url 字段名错误 | 修正为 fileUrl |
| P2 | 无文件管理 API | 添加 list/delete 端点 |
| P2 | 零测试文件 | 创建单元测试 |

---

## 2. API 变更

### 2.1 询盘提交接口（POST /api/public/inquiries）

**新增校验规则：**

| 字段 | 校验规则 | 错误信息 |
|------|----------|----------|
| name | @NotBlank | 客户姓名不能为空 |
| email | @Email | 邮箱格式不正确 |

**请求示例：**
```bash
curl -X POST http://localhost:8080/api/public/inquiries \
  -H "Content-Type: application/json" \
  -d '{"name":"张三","email":"test@example.com","company":"测试公司"}'
```

**校验失败响应：**
```json
{
  "code": 400,
  "message": "客户姓名不能为空",
  "data": null
}
```

### 2.2 询盘状态更新接口（PUT /api/admin/inquiries/{id}/status）

**新增校验规则：**

有效状态值：
- `new` — 新询盘
- `contacted` — 已联系
- `quoted` — 已报价
- `sample_confirmed` — 样品确认
- `order_confirmed` — 订单确认
- `closed` — 已关闭
- `invalid` — 无效询盘

**无效状态响应：**
```json
{
  "code": 400,
  "message": "无效的询盘状态: hacked",
  "data": null
}
```

### 2.3 文件列表接口（GET /api/admin/upload）

**新增端点：**

```bash
# 查询所有上传记录
curl -H "Authorization: Bearer <token>" \
  "http://localhost:8080/api/admin/upload?page=1&size=20"

# 按业务类型筛选
curl -H "Authorization: Bearer <token>" \
  "http://localhost:8080/api/admin/upload?bizType=inquiry"
```

**响应：**
```json
{
  "code": 200,
  "message": "ok",
  "data": {
    "records": [...],
    "total": 100,
    "page": 1,
    "size": 20
  }
}
```

### 2.4 文件删除接口（DELETE /api/admin/upload/{id}）

```bash
curl -X DELETE -H "Authorization: Bearer <token>" \
  http://localhost:8080/api/admin/upload/1
```

---

## 3. 修改文件清单

### 3.1 后端

| 文件 | 修改内容 |
|------|----------|
| `xlx-api/pom.xml` | dependencyManagement 添加测试依赖 |
| `xlx-api/xlx-inquiry/pom.xml` | 添加 spring-boot-starter-test + h2 |
| `xlx-api/xlx-file/pom.xml` | 添加 spring-boot-starter-test + h2 |
| `xlx-api/xlx-gateway/pom.xml` | 添加 h2 |
| `xlx-inquiry/.../Inquiry.java` | 添加 @NotBlank、@Email 校验注解 |
| `xlx-inquiry/.../InquiryService.java` | 添加状态白名单 VALID_STATUSES |
| `xlx-gateway/.../PublicController.java` | submitInquiry 添加 @Valid |
| `xlx-file/.../FileUploadService.java` | 添加 list()、delete() 方法 |
| `xlx-file/.../UploadController.java` | 添加 GET list、DELETE 端点 |

### 3.2 前端

| 文件 | 修改内容 |
|------|----------|
| `xlx-web/src/views/Contact.vue` | 添加错误处理 + 修复 fileUrl 字段 |
| `xlx-web/src/views/ProductDetail.vue` | 添加错误处理 |

### 3.3 测试

| 文件 | 说明 |
|------|------|
| `xlx-inquiry/src/test/.../InquiryServiceTest.java` | 10 个测试用例 |
| `xlx-file/src/test/.../FileUploadServiceTest.java` | 8 个测试用例 |

---

## 4. 测试覆盖

### 4.1 InquiryServiceTest（10 用例）

| 测试方法 | 验证内容 |
|----------|----------|
| list_returnsPaginatedResults | 分页查询返回正确结果 |
| list_withStatusFilter_filtersByStatus | 按状态筛选 |
| getById_existingInquiry_returnsInquiry | 存在时返回询盘 |
| getById_nonExisting_throwsBusinessException | 不存在时抛异常 |
| create_setsStatusToNew | 创建时默认状态为 new |
| updateStatus_validStatus_persists | 有效状态正常保存 |
| updateStatus_invalidStatus_throwsBusinessException | 无效状态抛异常 |
| updateStatus_allValidStatuses_pass | 所有有效状态均可通过 |
| updateRemark_updatesRemark | 更新备注 |
| delete_deletesInquiry | 删除询盘 |

### 4.2 FileUploadServiceTest（8 用例）

| 测试方法 | 验证内容 |
|----------|----------|
| upload_emptyFile_throwsBusinessException | 空文件拒绝 |
| upload_fileExceedsMaxSize_throwsBusinessException | 超大文件拒绝 |
| upload_blockedExtension_throwsBusinessException | 危险文件拒绝 |
| upload_unsupportedExtension_throwsBusinessException | 不支持类型拒绝 |
| upload_allowedExtension_savesAndReturns | 允许类型正常保存 |
| upload_createsDirectoryStructure | 按业务类型创建目录 |
| delete_removesRecordAndFile | 删除记录和文件 |
| delete_nonExisting_throwsBusinessException | 不存在时抛异常 |

### 4.3 运行测试

```bash
cd xlx-api
mvn test -pl xlx-inquiry   # 10 tests
mvn test -pl xlx-file      # 8 tests
```

---

## 5. 验证步骤

### 5.1 后端验证

```bash
# 1. 编译
cd xlx-api && mvn clean compile

# 2. 运行测试
mvn test -pl xlx-inquiry -pl xlx-file

# 3. 启动后端（需要先建库）
mysql -u root -proot -e "CREATE DATABASE IF NOT EXISTS xlx DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;"
mvn spring-boot:run -pl xlx-gateway

# 4. 测试询盘提交（空 name 应返回错误）
curl -X POST http://localhost:8080/api/public/inquiries \
  -H "Content-Type: application/json" \
  -d '{"name":"","email":"test@example.com"}'

# 5. 测试有效询盘提交
curl -X POST http://localhost:8080/api/public/inquiries \
  -H "Content-Type: application/json" \
  -d '{"name":"张三","email":"test@example.com","company":"测试公司"}'

# 6. 登录获取 token
curl -X POST http://localhost:8080/api/admin/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"admin","password":"admin123"}'

# 7. 测试文件列表
curl -H "Authorization: Bearer <token>" \
  "http://localhost:8080/api/admin/upload?page=1&size=10"
```

### 5.2 前端验证

```bash
# 1. 启动前台
cd xlx-web && npm run dev

# 2. 访问 /contact 页面
# 3. 提交空表单 → 应显示"客户姓名不能为空"错误
# 4. 提交有效表单 → 应显示成功提示
# 5. 上传超大文件 → 应显示错误提示

# 6. 启动后台
cd xlx-admin && npm run dev

# 7. 登录后访问 /admin/inquiries
# 8. 测试状态更新（输入无效状态应被拒绝）
```

---

## 6. 后续优化建议

| 优先级 | 建议 | 说明 |
|--------|------|------|
| P2 | 添加文件内容 Magic Bytes 校验 | 防止通过修改扩展名绕过白名单 |
| P2 | Inquiry 引入 DTO 隔离 | 避免暴露内部字段 |
| P3 | 添加文件管理前端页面 | xlx-admin 中查看/管理所有上传记录 |
