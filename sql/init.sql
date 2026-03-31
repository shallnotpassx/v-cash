-- v-cash 数据库初始化脚本

CREATE DATABASE IF NOT EXISTS vcash DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE vcash;

-- 股票基本信息表
CREATE TABLE IF NOT EXISTS stock_basic (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    stock_code VARCHAR(20) NOT NULL COMMENT '股票代码',
    stock_name VARCHAR(100) NOT NULL COMMENT '股票名称',
    market VARCHAR(20) NOT NULL COMMENT '市场(A股/港股)',
    industry VARCHAR(100) COMMENT '所属行业',
    list_date DATE COMMENT '上市日期',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted TINYINT DEFAULT 0 COMMENT '逻辑删除标志',
    INDEX idx_stock_code (stock_code),
    INDEX idx_market (market)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='股票基本信息表';

-- 财务数据表
CREATE TABLE IF NOT EXISTS stock_financial (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    stock_id BIGINT NOT NULL COMMENT '股票ID',
    report_period DATE NOT NULL COMMENT '报告期',
    revenue DECIMAL(20,2) COMMENT '营业收入(元)',
    net_profit DECIMAL(20,2) COMMENT '净利润(元)',
    roe DECIMAL(10,4) COMMENT '净资产收益率',
    pe DECIMAL(10,4) COMMENT '市盈率',
    pb DECIMAL(10,4) COMMENT '市净率',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    deleted TINYINT DEFAULT 0 COMMENT '逻辑删除标志',
    INDEX idx_stock_id (stock_id),
    INDEX idx_report_period (report_period),
    UNIQUE KEY uk_stock_period (stock_id, report_period)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='财务数据表';

-- 公告信息表
CREATE TABLE IF NOT EXISTS stock_announcement (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    stock_id BIGINT NOT NULL COMMENT '股票ID',
    title VARCHAR(500) NOT NULL COMMENT '公告标题',
    announcement_type VARCHAR(50) COMMENT '公告类型',
    publish_date DATE NOT NULL COMMENT '发布日期',
    source_url VARCHAR(500) COMMENT '原文链接',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    deleted TINYINT DEFAULT 0 COMMENT '逻辑删除标志',
    INDEX idx_stock_id (stock_id),
    INDEX idx_publish_date (publish_date)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='公告信息表';