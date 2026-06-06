-- V11: 添加关于我们页面配置项
-- 公司介绍文字（中英文）和关键数字（中英文）

INSERT IGNORE INTO site_config (config_key, config_value, description) VALUES
('about_intro', '鑫连鑫丝网厂位于中国丝网之都——江苏省南通市，是一家专业生产金属丝和丝网产品的企业。公司拥有多年的生产经验，技术力量雄厚，检测设备齐全。我们主要产品涵盖金属丝类（镀锌铁丝、不锈钢丝、退火丝、PVC涂塑丝等）和丝网类（电焊网片、荷兰网、护栏网、窗纱、钢板网、钢格板等），广泛应用于建筑、农业、工业、交通等领域。公司始终坚持"质量第一、客户至上"的经营理念，产品远销东南亚、中东、非洲、欧洲等多个国家和地区，赢得了国内外客户的信赖与好评。', '关于我们-公司介绍'),
('about_intro_en', 'Xinlianxin Wire Mesh Factory is located in Nantong, Jiangsu Province, the wire mesh capital of China. We are a professional manufacturer of metal wire and mesh products with years of production experience, strong technical capabilities, and complete testing equipment. Our main products include metal wire (galvanized iron wire, stainless steel wire, annealed wire, PVC coated wire, etc.) and wire mesh (welded wire mesh, Holland mesh, fence mesh, window screen, steel grating, etc.), widely used in construction, agriculture, industry, and transportation. We adhere to the business philosophy of "Quality First, Customer Supreme," with products exported to Southeast Asia, Middle East, Africa, Europe, and other countries and regions.', '关于我们-公司介绍(EN)'),
('about_years', '10+', '关于我们-年经验'),
('about_years_en', '10+', '关于我们-年经验(EN)'),
('about_products', '50+', '关于我们-产品种类'),
('about_products_en', '50+', '关于我们-产品种类(EN)'),
('about_countries', '30+', '关于我们-出口国家'),
('about_countries_en', '30+', '关于我们-出口国家(EN)'),
('about_clients', '1000+', '关于我们-服务客户'),
('about_clients_en', '1000+', '关于我们-服务客户(EN)');
