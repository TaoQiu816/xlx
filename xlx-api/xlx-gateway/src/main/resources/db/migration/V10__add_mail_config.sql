-- V10: 邮件通知配置
INSERT IGNORE INTO site_config (config_key, config_value, description) VALUES
('mail_enabled', 'false', '邮件通知开关'),
('mail_host', '', 'SMTP 服务器地址'),
('mail_port', '587', 'SMTP 端口'),
('mail_username', '', '发件人邮箱'),
('mail_password', '', '邮箱密码/授权码'),
('mail_from_name', '鑫连鑫丝网', '发件人显示名称'),
('mail_to', '', '收件人邮箱（管理员）'),
('mail_tls', 'true', '是否启用 TLS');
