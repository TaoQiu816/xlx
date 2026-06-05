# xlx-content — 内容模块

## 职责

工厂展示图片和企业资质证书的管理。

## 包名

`com.xlx.api.content`

## 文件清单

| 文件 | 说明 |
|------|------|
| `entity/FactoryImage.java` | 工厂图片实体（factory_image 表） |
| `entity/Certificate.java` | 证书实体（certificate 表，含证书类型和颁发日期） |
| `mapper/FactoryImageMapper.java` | 工厂图片 Mapper |
| `mapper/CertificateMapper.java` | 证书 Mapper |
| `service/FactoryImageService.java` | 工厂图片服务（分页、全量查询、CRUD） |
| `service/CertificateService.java` | 证书服务（分页、全量查询、CRUD） |
| `controller/FactoryImageController.java` | 工厂图片管理接口 |
| `controller/CertificateController.java` | 证书管理接口 |

## API 接口

### 工厂图片

```bash
# 查询工厂图片列表
curl -H "Authorization: Bearer $TOKEN" \
  "http://localhost:8080/api/admin/factory-images?page=1&size=20"

# 新增工厂图片
curl -X POST http://localhost:8080/api/admin/factory-images \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "titleCn": "生产车间",
    "titleEn": "Production Workshop",
    "imageUrl": "/uploads/factory/workshop.jpg",
    "descriptionCn": "现代化生产车间",
    "sortOrder": 1
  }'

# 更新工厂图片
curl -X PUT http://localhost:8080/api/admin/factory-images/1 \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{"titleCn":"新车间"}'

# 删除工厂图片
curl -X DELETE http://localhost:8080/api/admin/factory-images/1 \
  -H "Authorization: Bearer $TOKEN"
```

### 证书管理

```bash
# 查询证书列表
curl -H "Authorization: Bearer $TOKEN" \
  "http://localhost:8080/api/admin/certificates?page=1&size=20"

# 新增证书
curl -X POST http://localhost:8080/api/admin/certificates \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "titleCn": "ISO9001质量管理体系认证",
    "titleEn": "ISO9001 Quality Management System",
    "imageUrl": "/uploads/cert/iso9001.jpg",
    "certificateType": "certificate",
    "issueDate": "2024-01-01",
    "sortOrder": 1
  }'

# 删除证书
curl -X DELETE http://localhost:8080/api/admin/certificates/1 \
  -H "Authorization: Bearer $TOKEN"
```

## 验证方法

```bash
# 1. 编译验证
mvn clean compile -pl xlx-content -am

# 2. 查询工厂图片
curl -H "Authorization: Bearer $TOKEN" \
  http://localhost:8080/api/admin/factory-images

# 3. 查询证书
curl -H "Authorization: Bearer $TOKEN" \
  http://localhost:8080/api/admin/certificates

# 4. 数据库验证
mysql -u root -p xlx -e "SELECT COUNT(*) FROM factory_image;"
mysql -u root -p xlx -e "SELECT id, title_cn, certificate_type FROM certificate;"
```

## 依赖

- `xlx-common`（Result、PageResult、BusinessException）
