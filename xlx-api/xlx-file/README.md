# xlx-file — 文件上传模块

## 职责

文件上传、类型/大小校验、危险文件拦截、上传记录管理。

## 包名

`com.xlx.api.file`

## 文件清单

| 文件 | 说明 |
|------|------|
| `entity/FileUpload.java` | 文件上传记录实体（file_upload 表） |
| `mapper/FileUploadMapper.java` | 文件上传 Mapper |
| `service/FileUploadService.java` | 文件上传服务（校验、存储、记录） |
| `controller/UploadController.java` | 文件上传接口 |

## 安全限制

| 项目 | 限制 |
|------|------|
| 允许的文件类型 | jpg, jpeg, png, gif, webp, pdf, doc, docx, xls, xlsx, txt |
| 单文件大小上限 | 10MB |
| 拒绝的文件类型 | .exe, .bat, .sh, .js, .php, .py, .jar 等可执行文件 |

## API 接口

### 后台上传（需 Token）

```bash
# 上传文件（通用）
curl -X POST http://localhost:8080/api/admin/upload \
  -H "Authorization: Bearer $TOKEN" \
  -F "file=@/path/to/image.jpg" \
  -F "bizType=general"

# 上传产品图片（关联产品 ID）
curl -X POST http://localhost:8080/api/admin/upload \
  -H "Authorization: Bearer $TOKEN" \
  -F "file=@/path/to/product-photo.jpg" \
  -F "bizType=product_image" \
  -F "bizId=1"

# 上传工厂图片
curl -X POST http://localhost:8080/api/admin/upload \
  -H "Authorization: Bearer $TOKEN" \
  -F "file=@/path/to/factory.jpg" \
  -F "bizType=factory_image"

# 上传证书图片
curl -X POST http://localhost:8080/api/admin/upload \
  -H "Authorization: Bearer $TOKEN" \
  -F "file=@/path/to/certificate.jpg" \
  -F "bizType=certificate"
```

响应：
```json
{
  "code": 200,
  "message": "success",
  "data": {
    "id": 1,
    "originalName": "product-photo.jpg",
    "fileName": "abc123.jpg",
    "fileUrl": "/uploads/abc123.jpg",
    "fileType": "image/jpeg",
    "fileSize": 102400,
    "bizType": "product_image",
    "bizId": 1
  }
}
```

### 前台上传（无需 Token）

```bash
# 前台上传文件（用于询盘附件等）
curl -X POST http://localhost:8080/api/public/upload \
  -F "file=@/path/to/drawing.pdf"
```

## 上传后的文件访问

上传的文件存储在 `UPLOAD_PATH` 目录（默认 `./uploads`），通过以下 URL 访问：

```
http://localhost:8080/uploads/{fileName}
```

## 验证方法

```bash
# 1. 编译验证
mvn clean compile -pl xlx-file -am

# 2. 上传测试
curl -X POST http://localhost:8080/api/admin/upload \
  -H "Authorization: Bearer $TOKEN" \
  -F "file=@/path/to/test.jpg" \
  -F "bizType=general"

# 3. 文件大小限制测试（应返回错误）
# 上传一个超过 10MB 的文件

# 4. 文件类型限制测试（应返回错误）
# 上传一个 .exe 文件

# 5. 数据库验证
mysql -u root -p xlx -e "SELECT id, original_name, file_url, biz_type FROM file_upload;"
```

## 依赖

- `xlx-common`（Result、BusinessException）
