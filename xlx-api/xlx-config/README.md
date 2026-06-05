# xlx-config — 站点配置模块

## 职责

网站全局配置项的管理，以键值对形式存储。

## 包名

`com.xlx.api.config`

## 文件清单

| 文件 | 说明 |
|------|------|
| `entity/SiteConfig.java` | 站点配置实体（site_config 表，键值对结构） |
| `mapper/SiteConfigMapper.java` | 站点配置 Mapper |
| `service/SiteConfigService.java` | 配置服务（getConfigMap 返回 Map、批量更新） |
| `controller/SiteConfigController.java` | 配置管理接口 |

## 配置项说明

| 配置键 | 说明 | 示例值 |
|--------|------|--------|
| site_name | 网站名称 | 鑫连鑫丝网厂 |
| company_name | 公司名称（中文） | 鑫连鑫丝网厂 |
| company_name_en | 公司名称（英文） | XINLIANXIN WIRE MESH FACTORY |
| phone | 联系电话 | 0318-1234567 |
| email | 邮箱 | info@xlxcn.cn |
| wechat | 微信 | xlx_wire |
| whatsapp | WhatsApp | +8613800138000 |
| address | 公司地址 | 江苏省南通市... |
| banner_title | 首页 Banner 标题 | 专业丝网与金属丝产品制造商 |
| banner_subtitle | 首页 Banner 副标题 | 多年丝网生产经验... |
| seo_title | SEO 标题 | 鑫连鑫丝网厂 — 不锈钢丝... |
| seo_description | SEO 描述 | 鑫连鑫丝网厂，专业生产... |

## API 接口

### 后台管理（需 Token）

```bash
# 查询所有配置
curl -H "Authorization: Bearer $TOKEN" \
  http://localhost:8080/api/admin/site-config

# 响应示例：
# [
#   {"id":1,"configKey":"site_name","configValue":"鑫连鑫丝网厂","description":"网站名称"},
#   {"id":2,"configKey":"company_name","configValue":"鑫连鑫丝网厂","description":"公司名称"},
#   ...
# ]

# 批量更新配置
curl -X PUT http://localhost:8080/api/admin/site-config \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "phone": "0318-7654321",
    "email": "contact@xlxcn.cn",
    "whatsapp": "+8613900139000"
  }'
```

### 前台获取（无需 Token）

```bash
# 获取配置（返回 Map 格式，前端直接使用）
curl http://localhost:8080/api/public/config

# 响应示例：
# {
#   "code": 200,
#   "message": "success",
#   "data": {
#     "site_name": "鑫连鑫丝网厂",
#     "company_name": "鑫连鑫丝网厂",
#     "phone": "0318-1234567",
#     "banner_title": "专业丝网与金属丝产品制造商",
#     ...
#   }
# }
```

## 添加新配置项

```bash
# 1. 在数据库中插入新配置
mysql -u root -p xlx -e "
  INSERT INTO site_config (config_key, config_value, description)
  VALUES ('wechat_qrcode', '', '微信二维码图片URL');
"

# 2. 后台更新配置值
curl -X PUT http://localhost:8080/api/admin/site-config \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{"wechat_qrcode": "/uploads/qrcode/wechat.jpg"}'

# 3. 前台自动获取
curl http://localhost:8080/api/public/config
```

## 验证方法

```bash
# 1. 编译验证
mvn clean compile -pl xlx-config -am

# 2. 查询配置
curl -H "Authorization: Bearer $TOKEN" \
  http://localhost:8080/api/admin/site-config

# 3. 更新配置
curl -X PUT http://localhost:8080/api/admin/site-config \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{"phone":"0318-9999999"}'

# 4. 验证更新生效
curl http://localhost:8080/api/public/config | grep phone

# 5. 数据库验证
mysql -u root -p xlx -e "SELECT config_key, config_value FROM site_config;"
```

## 依赖

- `xlx-common`（Result、BusinessException）
