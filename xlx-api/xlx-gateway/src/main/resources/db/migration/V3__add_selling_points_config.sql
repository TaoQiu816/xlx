-- ============================================================
-- V3 添加首页卖点配置项
-- 鑫连鑫丝网厂 — 丝网工厂官网与销售系统
-- ============================================================
-- 添加 4 个卖点区块的可配置项（标题、数字、描述）
-- 使用 INSERT IGNORE 确保幂等
-- ============================================================
USE xlx;

-- 卖点 1：自有工厂
INSERT IGNORE INTO site_config (config_key, config_value, description) VALUES
('sp1_num', '23年', '卖点1-数字'),
('sp1_title', '自有工厂', '卖点1-标题'),
('sp1_desc', '南通市产业带源头工厂，产能充足，交期可控', '卖点1-描述');

-- 卖点 2：品质保障
INSERT IGNORE INTO site_config (config_key, config_value, description) VALUES
('sp2_num', '100%', '卖点2-数字'),
('sp2_title', '品质保障', '卖点2-标题'),
('sp2_desc', '严格质量管控体系，每批产品均附检测报告', '卖点2-描述');

-- 卖点 3：定制加工
INSERT IGNORE INTO site_config (config_key, config_value, description) VALUES
('sp3_num', '48h', '卖点3-数字'),
('sp3_title', '定制加工', '卖点3-标题'),
('sp3_desc', '支持来图来样定制，灵活满足非标需求', '卖点3-描述');

-- 卖点 4：出口经验
INSERT IGNORE INTO site_config (config_key, config_value, description) VALUES
('sp4_num', '30+', '卖点4-数字'),
('sp4_title', '出口经验', '卖点4-标题'),
('sp4_desc', '产品远销海外多国，熟悉出口包装与物流', '卖点4-描述');

-- Hero 统计数字
INSERT IGNORE INTO site_config (config_key, config_value, description) VALUES
('hero_stat1_value', '23年', 'Hero统计1-数字'),
('hero_stat1_label', '生产经验', 'Hero统计1-标签'),
('hero_stat2_value', '50+', 'Hero统计2-数字'),
('hero_stat2_label', '产品型号', 'Hero统计2-标签'),
('hero_stat3_value', '30+', 'Hero统计3-数字'),
('hero_stat3_label', '出口国家', 'Hero统计3-标签'),
('hero_stat4_value', '1000+', 'Hero统计4-数字'),
('hero_stat4_label', '服务客户', 'Hero统计4-标签');

-- 修正 Banner 副标题中的"多年"为"23年"
UPDATE site_config SET config_value = '23年丝网生产经验，支持标准规格供应与定制加工，服务国内外客户。'
WHERE config_key = 'banner_subtitle' AND config_value LIKE '%多年%';
