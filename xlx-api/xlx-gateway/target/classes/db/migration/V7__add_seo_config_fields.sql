-- V7: 扩展 SEO 配置字段
-- 为 site_config 表添加更多 SEO 相关配置项

INSERT INTO site_config (config_key, config_value, remark) VALUES
('seo_keywords', '鑫连鑫,丝网,不锈钢丝,镀锌铁丝,电焊网片,护栏网,窗纱,钢板网,金属丝网', 'SEO关键词，逗号分隔'),
('seo_og_image', '/logo.png', 'Open Graph 分享图片路径'),
('seo_company_name', '鑫连鑫丝网厂', 'SEO 公司名称'),
('seo_phone', '', 'SEO 联系电话'),
('seo_address', '河北省衡水市安平县', 'SEO 公司地址')
ON DUPLICATE KEY UPDATE config_value = VALUES(config_value);
