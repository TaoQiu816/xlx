-- ============================================================
-- V6 工厂展示支持视频 — 新增 media_type 字段
-- 鑫连鑫丝网厂 — 丝网工厂官网与销售系统
-- ============================================================
USE xlx;

ALTER TABLE factory_image
    ADD COLUMN media_type VARCHAR(20) DEFAULT 'image' COMMENT '媒体类型：image / video'
    AFTER image_url;
