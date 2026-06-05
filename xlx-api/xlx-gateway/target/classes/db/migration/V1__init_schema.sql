-- ============================================================
-- V1 初始化数据库结构
-- 鑫连鑫丝网厂 — 丝网工厂官网与销售系统
-- ============================================================
-- 前置条件：数据库 xlx 必须已存在（Flyway 只管建表，不管建库）
--   手动建库：CREATE DATABASE xlx DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
--
-- 共 10 张表，按模块归属如下：
--   xlx-auth    → admin_user
--   xlx-product → product_category, product, product_spec, product_image
--   xlx-content → factory_image, certificate
--   xlx-inquiry → inquiry
--   xlx-file    → file_upload
--   xlx-config  → site_config
--
-- 设计要点：
--   1. 所有表使用 BIGINT 自增主键
--   2. 中英文双语字段以 _cn/_en 后缀区分
--   3. 时间字段使用 DATETIME + 自动更新
--   4. 软删除通过 deleted 字段实现（MyBatis Plus 逻辑删除）
--   5. 所有表使用 InnoDB 引擎，支持事务
-- ============================================================

USE xlx;

-- ============================================================
-- 1. 管理员表
-- ============================================================
CREATE TABLE admin_user (
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    username    VARCHAR(50)  NOT NULL UNIQUE,
    password    VARCHAR(255) NOT NULL COMMENT 'BCrypt 加密密码',
    nickname    VARCHAR(50)  DEFAULT '',
    role        VARCHAR(20)  DEFAULT 'admin' COMMENT 'admin / editor',
    status      TINYINT      DEFAULT 1 COMMENT '1=启用 0=禁用',
    last_login_at DATETIME   DEFAULT NULL,
    created_at  DATETIME     DEFAULT CURRENT_TIMESTAMP,
    updated_at  DATETIME     DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB COMMENT='管理员表';

-- ============================================================
-- 2. 产品分类表
-- ============================================================
CREATE TABLE product_category (
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    parent_id   BIGINT       DEFAULT 0 COMMENT '父分类ID，0为顶级',
    name_cn     VARCHAR(100) NOT NULL,
    name_en     VARCHAR(100) DEFAULT '',
    slug        VARCHAR(100) DEFAULT '' COMMENT 'URL 友好标识',
    cover_image VARCHAR(500) DEFAULT '',
    sort_order  INT          DEFAULT 0,
    status      TINYINT      DEFAULT 1 COMMENT '1=显示 0=隐藏',
    created_at  DATETIME     DEFAULT CURRENT_TIMESTAMP,
    updated_at  DATETIME     DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_parent_id (parent_id),
    INDEX idx_sort_order (sort_order)
) ENGINE=InnoDB COMMENT='产品分类表';

-- ============================================================
-- 3. 产品表
-- ============================================================
CREATE TABLE product (
    id                  BIGINT AUTO_INCREMENT PRIMARY KEY,
    category_id         BIGINT       NOT NULL,
    name_cn             VARCHAR(200) NOT NULL,
    name_en             VARCHAR(200) DEFAULT '',
    slug                VARCHAR(200) DEFAULT '' COMMENT 'URL 友好标识',
    summary_cn          VARCHAR(500) DEFAULT '',
    summary_en          VARCHAR(500) DEFAULT '',
    description_cn      TEXT         COMMENT '中文详情（富文本）',
    description_en      TEXT         COMMENT '英文详情（富文本）',
    main_image          VARCHAR(500) DEFAULT '',
    price               DECIMAL(10,2) DEFAULT NULL COMMENT '单价，NULL 表示联系报价',
    price_unit          VARCHAR(20)  DEFAULT '' COMMENT '价格单位，如 元/件、元/米',
    show_price          TINYINT      DEFAULT 0 COMMENT '1=前台显示价格 0=隐藏',
    support_custom      TINYINT      DEFAULT 0 COMMENT '1=支持定制',
    support_inquiry     TINYINT      DEFAULT 1 COMMENT '1=支持询价',
    support_order       TINYINT      DEFAULT 0 COMMENT '1=支持后续下单',
    moq                 VARCHAR(100) DEFAULT '' COMMENT '最小起订量',
    stock_status        VARCHAR(20)  DEFAULT 'in_stock' COMMENT 'in_stock / out_of_stock / pre_order',
    packaging_cn        VARCHAR(200) DEFAULT '',
    packaging_en        VARCHAR(200) DEFAULT '',
    applications_cn     VARCHAR(500) DEFAULT '',
    applications_en     VARCHAR(500) DEFAULT '',
    certificate_note_cn VARCHAR(500) DEFAULT '',
    certificate_note_en VARCHAR(500) DEFAULT '',
    sort_order          INT          DEFAULT 0,
    is_featured         TINYINT      DEFAULT 0 COMMENT '1=推荐到首页',
    status              TINYINT      DEFAULT 1 COMMENT '1=上架 0=下架',
    created_at          DATETIME     DEFAULT CURRENT_TIMESTAMP,
    updated_at          DATETIME     DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_category_id (category_id),
    INDEX idx_slug (slug),
    INDEX idx_sort_order (sort_order),
    INDEX idx_is_featured (is_featured),
    INDEX idx_status (status)
) ENGINE=InnoDB COMMENT='产品表';

-- ============================================================
-- 4. 产品规格表
-- ============================================================
CREATE TABLE product_spec (
    id             BIGINT AUTO_INCREMENT PRIMARY KEY,
    product_id     BIGINT       NOT NULL,
    spec_name_cn   VARCHAR(100) NOT NULL COMMENT '规格名，如 材质、丝径',
    spec_name_en   VARCHAR(100) DEFAULT '',
    spec_value_cn  VARCHAR(200) NOT NULL COMMENT '规格值，如 304 / 316',
    spec_value_en  VARCHAR(200) DEFAULT '',
    sort_order     INT          DEFAULT 0,
    created_at     DATETIME     DEFAULT CURRENT_TIMESTAMP,
    updated_at     DATETIME     DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_product_id (product_id)
) ENGINE=InnoDB COMMENT='产品规格表';

-- ============================================================
-- 5. 产品图片表
-- ============================================================
CREATE TABLE product_image (
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    product_id  BIGINT       NOT NULL,
    image_url   VARCHAR(500) NOT NULL,
    alt_cn      VARCHAR(200) DEFAULT '',
    alt_en      VARCHAR(200) DEFAULT '',
    sort_order  INT          DEFAULT 0,
    created_at  DATETIME     DEFAULT CURRENT_TIMESTAMP,
    INDEX idx_product_id (product_id)
) ENGINE=InnoDB COMMENT='产品图片表';

-- ============================================================
-- 6. 工厂图片表
-- ============================================================
CREATE TABLE factory_image (
    id              BIGINT AUTO_INCREMENT PRIMARY KEY,
    title_cn        VARCHAR(200) DEFAULT '',
    title_en        VARCHAR(200) DEFAULT '',
    image_url       VARCHAR(500) NOT NULL,
    description_cn  VARCHAR(500) DEFAULT '',
    description_en  VARCHAR(500) DEFAULT '',
    sort_order      INT          DEFAULT 0,
    status          TINYINT      DEFAULT 1,
    created_at      DATETIME     DEFAULT CURRENT_TIMESTAMP,
    updated_at      DATETIME     DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_sort_order (sort_order)
) ENGINE=InnoDB COMMENT='工厂图片表';

-- ============================================================
-- 7. 证书表
-- ============================================================
CREATE TABLE certificate (
    id               BIGINT AUTO_INCREMENT PRIMARY KEY,
    title_cn         VARCHAR(200) DEFAULT '',
    title_en         VARCHAR(200) DEFAULT '',
    image_url        VARCHAR(500) NOT NULL,
    description_cn   VARCHAR(500) DEFAULT '',
    description_en   VARCHAR(500) DEFAULT '',
    certificate_type VARCHAR(50)  DEFAULT '' COMMENT 'certificate / report / quality',
    issue_date       DATE         DEFAULT NULL,
    sort_order       INT          DEFAULT 0,
    status           TINYINT      DEFAULT 1,
    created_at       DATETIME     DEFAULT CURRENT_TIMESTAMP,
    updated_at       DATETIME     DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_sort_order (sort_order)
) ENGINE=InnoDB COMMENT='证书表';

-- ============================================================
-- 8. 询盘表
-- ============================================================
CREATE TABLE inquiry (
    id            BIGINT AUTO_INCREMENT PRIMARY KEY,
    name          VARCHAR(100)  NOT NULL,
    company       VARCHAR(200)  DEFAULT '',
    phone         VARCHAR(50)   DEFAULT '',
    email         VARCHAR(100)  DEFAULT '',
    wechat        VARCHAR(50)   DEFAULT '',
    whatsapp      VARCHAR(50)   DEFAULT '',
    country       VARCHAR(100)  DEFAULT '',
    product_id    BIGINT        DEFAULT NULL COMMENT '感兴趣的产品ID',
    product_name  VARCHAR(200)  DEFAULT '',
    quantity       VARCHAR(100)  DEFAULT '',
    specification VARCHAR(500)  DEFAULT '' COMMENT '规格要求',
    message       TEXT           COMMENT '留言内容',
    file_url      VARCHAR(500)  DEFAULT '' COMMENT '上传文件地址',
    status        VARCHAR(20)   DEFAULT 'new' COMMENT 'new/contacted/quoted/sample_confirmed/order_confirmed/closed/invalid',
    remark        TEXT           COMMENT '后台备注',
    created_at    DATETIME      DEFAULT CURRENT_TIMESTAMP,
    updated_at    DATETIME      DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_status (status),
    INDEX idx_created_at (created_at),
    INDEX idx_product_id (product_id)
) ENGINE=InnoDB COMMENT='询盘表';

-- ============================================================
-- 9. 文件上传记录表
-- ============================================================
CREATE TABLE file_upload (
    id            BIGINT AUTO_INCREMENT PRIMARY KEY,
    original_name VARCHAR(255) NOT NULL COMMENT '原始文件名',
    file_name     VARCHAR(255) NOT NULL COMMENT '存储文件名',
    file_url      VARCHAR(500) NOT NULL COMMENT '访问路径',
    file_type     VARCHAR(50)  DEFAULT '' COMMENT '文件 MIME 类型',
    file_size     BIGINT       DEFAULT 0 COMMENT '文件大小（字节）',
    biz_type      VARCHAR(50)  DEFAULT '' COMMENT '业务类型：product_image / factory_image / certificate / inquiry',
    biz_id        BIGINT       DEFAULT NULL COMMENT '关联业务ID',
    created_at    DATETIME     DEFAULT CURRENT_TIMESTAMP,
    INDEX idx_biz (biz_type, biz_id)
) ENGINE=InnoDB COMMENT='文件上传记录表';

-- ============================================================
-- 10. 网站配置表
-- ============================================================
CREATE TABLE site_config (
    id           BIGINT AUTO_INCREMENT PRIMARY KEY,
    config_key   VARCHAR(100) NOT NULL UNIQUE COMMENT '配置键',
    config_value TEXT         COMMENT '配置值',
    description  VARCHAR(200) DEFAULT '',
    created_at   DATETIME     DEFAULT CURRENT_TIMESTAMP,
    updated_at   DATETIME     DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    UNIQUE INDEX idx_config_key (config_key)
) ENGINE=InnoDB COMMENT='网站配置表';
