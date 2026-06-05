# xlx-inquiry — 询盘模块

## 职责

客户询盘的提交、查询、状态流转和备注管理。

## 包名

`com.xlx.api.inquiry`

## 文件清单

| 文件 | 说明 |
|------|------|
| `entity/Inquiry.java` | 询盘实体（inquiry 表） |
| `mapper/InquiryMapper.java` | 询盘 Mapper |
| `service/InquiryService.java` | 询盘服务（分页查询、状态更新、备注管理） |
| `controller/InquiryController.java` | 询盘管理接口 |

## 询盘状态流转

```
new → contacted → quoted → sample_confirmed → order_confirmed → closed
                                                  ↘ invalid
```

| 状态 | 含义 |
|------|------|
| new | 新建（默认） |
| contacted | 已联系 |
| quoted | 已报价 |
| sample_confirmed | 样品确认 |
| order_confirmed | 订单确认 |
| closed | 已关闭 |
| invalid | 无效询盘 |

## API 接口

### 后台管理（需 Token）

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

# 更新状态（new → contacted）
curl -X PUT http://localhost:8080/api/admin/inquiries/1/status \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{"status":"contacted"}'

# 更新备注
curl -X PUT http://localhost:8080/api/admin/inquiries/1/remark \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{"remark":"客户需要304不锈钢丝报价，已联系"}'

# 删除询盘
curl -X DELETE http://localhost:8080/api/admin/inquiries/1 \
  -H "Authorization: Bearer $TOKEN"
```

### 前台提交（无需 Token）

```bash
# 提交询盘（由 PublicController 转发）
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

## 验证方法

```bash
# 1. 编译验证
mvn clean compile -pl xlx-inquiry -am

# 2. 提交询盘
curl -X POST http://localhost:8080/api/public/inquiries \
  -H "Content-Type: application/json" \
  -d '{"name":"测试客户","email":"test@example.com","message":"测试询盘"}'

# 3. 查询询盘
curl -H "Authorization: Bearer $TOKEN" \
  http://localhost:8080/api/admin/inquiries

# 4. 数据库验证
mysql -u root -p xlx -e "SELECT id, name, email, status, created_at FROM inquiry;"
```

## 依赖

- `xlx-common`（Result、PageResult、BusinessException）
