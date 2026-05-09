# v-cash

股票数据查询与分析系统，提供 A 股/港股财务数据及公告信息。

## 技术栈

| 层级 | 技术 | 版本 |
|------|------|------|
| 后端 | Spring Boot | 3.2.5 |
| JDK | Eclipse Temurin | 17 |
| ORM | MyBatis Plus | 3.5.5 |
| 数据库 | MySQL | 8.0 |
| 前端 | Vue 3 + Element Plus | 3.4.x |
| 构建 | Maven | 3.9.x |
| 部署 | Docker Compose | - |

## 项目结构

```
v-cash/
├── v-cash-domain/          # 领域层 - 实体、仓储接口、领域服务
├── v-cash-application/     # 应用层 - 用例编排、应用服务
├── v-cash-infrastructure/  # 基础设施层 - 适配器、持久化、配置
├── v-cash-api/             # 接口层 - REST 控制器、启动类
├── vcash-web/              # 前端 Vue 3
├── sql/init.sql            # 数据库初始化脚本
└── docs/
    ├── openspec/           # OpenSpec 规范
    ├── PLAN.md             # 项目路线图
    ├── SETUP.md            # 环境搭建
    ├── TEST_CASES.md       # 测试用例
    └── TEST_REPORT.md      # 测试报告
```

## 数据源

| 功能 | 首选 | 备选 |
|------|------|------|
| A 股财务 | Tushare Pro | 东方财富 |
| 港股财务 | 东方财富 | Tushare Pro |
| A 股公告 | 巨潮资讯网 | 东方财富 |
| 港股公告 | 东方财富 | - |

适配器采用策略 + 兜底模式：按顺序尝试，返回第一个成功结果。

## 数据库设计

### stock_basic — 股票基本信息

| 字段 | 类型 | 说明 |
|------|------|------|
| id | BIGINT | 主键 |
| stock_code | VARCHAR(20) | 股票代码 |
| stock_name | VARCHAR(100) | 股票名称 |
| market | VARCHAR(20) | 市场（A/HK） |
| industry | VARCHAR(100) | 所属行业 |
| list_date | DATE | 上市日期 |
| deleted | TINYINT | 逻辑删除（0=正常，1=删除） |
| create_time | DATETIME | 创建时间 |
| update_time | DATETIME | 更新时间 |

### stock_financial — 财务数据

| 字段 | 类型 | 说明 |
|------|------|------|
| id | BIGINT | 主键 |
| stock_id | BIGINT | 股票 ID |
| report_period | DATE | 报告期 |
| revenue | DECIMAL(20,2) | 营业收入 |
| net_profit | DECIMAL(20,2) | 净利润 |
| roe | DECIMAL(10,4) | ROE |
| pe | DECIMAL(10,4) | 市盈率 |
| pb | DECIMAL(10,4) | 市净率 |
| create_time | DATETIME | 创建时间 |

### stock_announcement — 公告信息

| 字段 | 类型 | 说明 |
|------|------|------|
| id | BIGINT | 主键 |
| stock_id | BIGINT | 股票 ID |
| title | VARCHAR(500) | 公告标题 |
| announcement_type | VARCHAR(50) | 公告类型 |
| publish_date | DATE | 发布日期 |
| source_url | VARCHAR(500) | 原文链接 |
| create_time | DATETIME | 创建时间 |

## API 文档

### 股票接口

| 方法 | 路径 | 说明 |
|------|------|------|
| GET | `/api/stock/list?market=A/HK&keyword=xxx` | 股票列表（按市场过滤 + 关键字搜索） |
| GET | `/api/stock/{code}` | 股票详情 |
| POST | `/api/stock/refresh?market=A/HK` | 手动刷新股票列表 |

### 财务数据接口

| 方法 | 路径 | 说明 |
|------|------|------|
| GET | `/api/financial/{stockCode}` | 历史财务数据 |
| GET | `/api/financial/{stockCode}/latest` | 最新财务指标 |
| POST | `/api/financial/{stockCode}/refresh` | 手动刷新财务数据 |

### 公告接口

| 方法 | 路径 | 说明 |
|------|------|------|
| GET | `/api/announcement/{stockCode}?type=xxx` | 公告列表（按类型筛选） |
| POST | `/api/announcement/{stockCode}/refresh` | 手动刷新公告 |

## 启动方式

### 本地开发

分别启动后端（JDK 17 + Maven）和前端（Node.js 18+）。

### Docker 部署

```bash
docker-compose up -d
```

访问 `http://localhost` 即可使用。

详细环境搭建见 [SETUP.md](docs/SETUP.md)。
