-- v-cash 数据库初始化脚本

CREATE TABLE IF NOT EXISTS stock (
    id          BIGINT       AUTO_INCREMENT PRIMARY KEY,
    market      VARCHAR(8)   NOT NULL COMMENT '市场: SH/SZ/HK',
    stock_code  VARCHAR(16)  NOT NULL COMMENT '股票代码',
    stock_name  VARCHAR(128) NOT NULL COMMENT '股票名称',
    listed_status VARCHAR(16) NOT NULL DEFAULT 'LISTED' COMMENT '上市状态: LISTED/SUSPENDED/DELISTED',
    listing_date DATE        NULL     COMMENT '上市日期',
    exchange    VARCHAR(32)  NULL     COMMENT '交易所',
    UNIQUE KEY uk_market_code (market, stock_code),
    INDEX idx_name (stock_name),
    INDEX idx_status (listed_status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='股票主数据';

CREATE TABLE IF NOT EXISTS sync_log (
    id          VARCHAR(64)  PRIMARY KEY,
    market      VARCHAR(8)   NOT NULL,
    stock_code  VARCHAR(16)  NOT NULL,
    source      VARCHAR(32)  NOT NULL COMMENT '数据源: eastmoney/tushare',
    status      VARCHAR(16)  NOT NULL COMMENT 'SUCCESS/PARTIAL/FAILED',
    record_count INT         NOT NULL DEFAULT 0,
    message     TEXT         NULL,
    INDEX idx_market_code (market, stock_code),
    INDEX idx_status (status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='同步日志';

CREATE TABLE IF NOT EXISTS financial_record (
    id          BIGINT       AUTO_INCREMENT PRIMARY KEY,
    market      VARCHAR(8)   NOT NULL,
    stock_code  VARCHAR(16)  NOT NULL,
    period      VARCHAR(16)  NOT NULL COMMENT '报告期: 2024Q4/2024FY',
    report_date DATE         NULL,
    revenue     DECIMAL(24,4) NULL COMMENT '营业收入',
    net_profit  DECIMAL(24,4) NULL COMMENT '净利润',
    total_assets DECIMAL(24,4) NULL COMMENT '总资产',
    net_assets  DECIMAL(24,4) NULL COMMENT '净资产',
    roe         DECIMAL(12,4) NULL COMMENT '净资产收益率(%)',
    eps         DECIMAL(12,4) NULL COMMENT '每股收益',
    UNIQUE KEY uk_stock_period (market, stock_code, period),
    INDEX idx_period (period)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='财务历史记录';

CREATE TABLE IF NOT EXISTS financial_snapshot (
    id          BIGINT       AUTO_INCREMENT PRIMARY KEY,
    market      VARCHAR(8)   NOT NULL,
    stock_code  VARCHAR(16)  NOT NULL,
    latest_period VARCHAR(16) NOT NULL COMMENT '最新报告期',
    updated_at  DATE         NOT NULL COMMENT '快照更新时间',
    revenue     DECIMAL(24,4) NULL,
    net_profit  DECIMAL(24,4) NULL,
    roe         DECIMAL(12,4) NULL,
    eps         DECIMAL(12,4) NULL,
    UNIQUE KEY uk_snapshot_stock (market, stock_code)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='财务筛选快照';

CREATE TABLE IF NOT EXISTS announcement (
    id          VARCHAR(64)  PRIMARY KEY,
    market      VARCHAR(8)   NOT NULL,
    stock_code  VARCHAR(16)  NOT NULL,
    title       VARCHAR(512) NOT NULL COMMENT '公告标题',
    publish_date DATE        NULL     COMMENT '发布日期',
    source_site VARCHAR(32)  NOT NULL COMMENT '来源: cninfo/hkexnews',
    source_url  VARCHAR(2048) NOT NULL COMMENT '原文链接',
    file_type   VARCHAR(8)   NOT NULL DEFAULT 'pdf',
    summary     TEXT         NULL     COMMENT '摘要',
    INDEX idx_market_code (market, stock_code),
    INDEX idx_publish_date (publish_date),
    INDEX idx_source_site (source_site)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='公告元数据';
