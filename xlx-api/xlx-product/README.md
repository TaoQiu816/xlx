# xlx-product — 产品模块

## 职责

产品分类、产品信息、产品规格、产品图片的增删改查管理。

## 包名

`com.xlx.api.product`

## 文件清单

| 文件 | 说明 |
|------|------|
| `entity/ProductCategory.java` | 产品分类实体（product_category 表，支持两级树形） |
| `entity/Product.java` | 产品实体（product 表，含中英文双语字段） |
| `entity/ProductSpec.java` | 产品规格实体（product_spec 表，如材质、丝径） |
| `entity/ProductImage.java` | 产品图片实体（product_image 表） |
| `mapper/ProductCategoryMapper.java` | 分类 Mapper |
| `mapper/ProductMapper.java` | 产品 Mapper |
| `mapper/ProductSpecMapper.java` | 规格 Mapper |
| `mapper/ProductImageMapper.java` | 图片 Mapper |
| `service/ProductCategoryService.java` | 分类服务（树形查询、CRUD） |
| `service/ProductService.java` | 产品服务（分页、推荐、详情组装） |
| `service/ProductSpecService.java` | 规格服务 |
| `service/ProductImageService.java` | 图片服务 |
| `controller/CategoryController.java` | 分类管理接口 |
| `controller/ProductController.java` | 产品管理接口 |

## API 接口

### 分类管理

```bash
# 查询所有分类
curl -H "Authorization: Bearer $TOKEN" \
  http://localhost:8080/api/admin/categories

# 新增分类
curl -X POST http://localhost:8080/api/admin/categories \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{"parentId":0,"nameCn":"新产品类","nameEn":"New Category","slug":"new-category","sortOrder":3}'

# 更新分类
curl -X PUT http://localhost:8080/api/admin/categories/1 \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{"nameCn":"金属丝类（已更新）"}'

# 删除分类
curl -X DELETE http://localhost:8080/api/admin/categories/11 \
  -H "Authorization: Bearer $TOKEN"
```

### 产品管理

```bash
# 分页查询产品
curl -H "Authorization: Bearer $TOKEN" \
  "http://localhost:8080/api/admin/products?page=1&size=20"

# 按分类筛选
curl -H "Authorization: Bearer $TOKEN" \
  "http://localhost:8080/api/admin/products?page=1&size=20&categoryId=3"

# 产品详情
curl -H "Authorization: Bearer $TOKEN" \
  http://localhost:8080/api/admin/products/1

# 新增产品
curl -X POST http://localhost:8080/api/admin/products \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "categoryId": 3,
    "nameCn": "304不锈钢丝",
    "nameEn": "304 Stainless Steel Wire",
    "slug": "304-stainless-steel-wire",
    "summaryCn": "优质304不锈钢丝",
    "supportInquiry": 1,
    "status": 1
  }'

# 更新产品
curl -X PUT http://localhost:8080/api/admin/products/1 \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{"nameCn":"304不锈钢丝（更新）"}'

# 删除产品
curl -X DELETE http://localhost:8080/api/admin/products/1 \
  -H "Authorization: Bearer $TOKEN"
```

## 验证方法

```bash
# 1. 编译验证
mvn clean compile -pl xlx-product -am

# 2. 查询分类（需先登录获取 Token）
curl -H "Authorization: Bearer $TOKEN" \
  http://localhost:8080/api/admin/categories

# 3. 查询产品
curl -H "Authorization: Bearer $TOKEN" \
  "http://localhost:8080/api/admin/products?page=1&size=5"

# 4. 数据库验证
mysql -u root -p xlx -e "SELECT COUNT(*) FROM product_category;"
mysql -u root -p xlx -e "SELECT id, name_cn, category_id FROM product LIMIT 5;"
```

## 依赖

- `xlx-common`（Result、PageResult、BusinessException）
