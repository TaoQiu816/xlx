-- ============================================================
-- V2 初始化种子数据
-- 鑫连鑫丝网厂 — 丝网工厂官网与销售系统
-- ============================================================
-- 本脚本插入系统运行所需的初始数据：
--   1. 管理员账号（admin / admin123，BCrypt 加密）
--   2. 产品分类（2 个一级分类 + 8 个二级分类）
--   3. 网站配置项（12 个键值对，含公司信息、SEO、Banner）
--
-- 注意：生产环境部署时务必修改管理员密码和 JWT 密钥
-- ============================================================
USE xlx;

-- ============================================================
-- 管理员账号（密码: admin123，BCrypt 加密）
-- ============================================================
INSERT INTO admin_user (username, password, nickname, role, status) VALUES
('admin', '$2a$12$b49isKSb7mT6faoHLcjVXOZAbfBuVLpVAzJwlazsuq7nm6uUcvoRm', '管理员', 'admin', 1);

-- ============================================================
-- 产品分类
-- ============================================================
INSERT INTO product_category (id, parent_id, name_cn, name_en, slug, sort_order, status) VALUES
(1, 0, '金属丝类', 'Metal Wire', 'metal-wire', 1, 1),
(2, 0, '丝网类', 'Wire Mesh', 'wire-mesh', 2, 1);

INSERT INTO product_category (id, parent_id, name_cn, name_en, slug, sort_order, status) VALUES
(3, 1, '不锈钢丝', 'Stainless Steel Wire', 'stainless-steel-wire', 1, 1),
(4, 1, '钛丝', 'Titanium Wire', 'titanium-wire', 2, 1),
(5, 1, '蒙乃尔丝', 'Monel Wire', 'monel-wire', 3, 1),
(6, 1, '铜丝', 'Copper Wire', 'copper-wire', 4, 1),
(7, 2, '不锈钢丝网', 'Stainless Steel Wire Mesh', 'stainless-steel-wire-mesh', 1, 1),
(8, 2, '钛丝网', 'Titanium Wire Mesh', 'titanium-wire-mesh', 2, 1),
(9, 2, '蒙乃尔丝网', 'Monel Wire Mesh', 'monel-wire-mesh', 3, 1),
(10, 2, '铜丝网', 'Copper Wire Mesh', 'copper-wire-mesh', 4, 1);

-- ============================================================
-- 网站配置默认值
-- ============================================================
INSERT INTO site_config (config_key, config_value, description) VALUES
('site_name', '鑫连鑫丝网厂', '网站名称'),
('company_name', '鑫连鑫丝网厂', '公司名称'),
('company_name_en', 'XINLIANXIN WIRE MESH FACTORY', '公司英文名'),
('phone', '', '联系电话'),
('email', '', '邮箱'),
('wechat', '', '微信'),
('whatsapp', '', 'WhatsApp'),
('address', '', '公司地址'),
('banner_title', '专业丝网与金属丝产品制造商', '首页Banner标题'),
('banner_subtitle', '多年丝网生产经验，支持标准规格供应与定制加工，服务国内外客户。', '首页Banner副标题'),
('seo_title', '鑫连鑫丝网厂 — 不锈钢丝、丝网、金属丝产品', 'SEO标题'),
('seo_description', '鑫连鑫丝网厂，专业生产不锈钢丝、钛丝、蒙乃尔丝、铜丝及各类丝网产品，支持标准规格与定制加工。', 'SEO描述');
