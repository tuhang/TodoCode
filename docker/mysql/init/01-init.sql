-- TodoCode MySQL 初始化脚本
-- 此脚本在容器首次启动时自动运行

-- 如果需要，为不同模块创建额外的数据库
CREATE DATABASE IF NOT EXISTS todocode_test;

-- 授予权限
GRANT ALL PRIVILEGES ON todocode.* TO 'todocode'@'%';
GRANT ALL PRIVILEGES ON todocode_test.* TO 'todocode'@'%';
FLUSH PRIVILEGES;

-- 系统设计模块的示例表
USE todocode;

CREATE TABLE IF NOT EXISTS users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    email VARCHAR(100) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_username (username),
    INDEX idx_email (email)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS orders (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    order_number VARCHAR(50) NOT NULL UNIQUE,
    total_amount DECIMAL(10,2) NOT NULL,
    status VARCHAR(20) DEFAULT 'PENDING',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id),
    INDEX idx_user_id (user_id),
    INDEX idx_order_number (order_number),
    INDEX idx_status (status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 插入示例数据
INSERT INTO users (username, email, password_hash) VALUES
    ('admin', 'admin@todocode.org', '$2a$10$dummyhashforadmin'),
    ('testuser', 'test@todocode.org', '$2a$10$dummyhashfortestuser')
ON DUPLICATE KEY UPDATE username=username;
