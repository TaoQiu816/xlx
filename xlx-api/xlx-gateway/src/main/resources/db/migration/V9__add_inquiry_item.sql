-- V9: 添加询盘产品明细表
-- 支持多产品询盘，每个询盘可关联多条明细记录

CREATE TABLE inquiry_item (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  inquiry_id BIGINT NOT NULL COMMENT '关联 inquiry.id',
  product_id BIGINT DEFAULT NULL COMMENT '关联 product.id，可为空（产品删除后保留历史）',
  product_name_cn VARCHAR(200) NOT NULL COMMENT '产品名称快照（中文）',
  product_name_en VARCHAR(200) DEFAULT '' COMMENT '产品名称快照（英文）',
  quantity VARCHAR(100) DEFAULT '' COMMENT '需求数量',
  specification VARCHAR(500) DEFAULT '' COMMENT '规格要求',
  sort_order INT DEFAULT 0 COMMENT '排序',
  deleted TINYINT(1) DEFAULT 0 COMMENT '逻辑删除：0-正常，1-已删除',
  created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
  updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  INDEX idx_inquiry_item_inquiry_id (inquiry_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='询盘产品明细';
