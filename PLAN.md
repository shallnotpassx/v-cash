# v-cash 项目计划

## 1. 项目概述

- **项目名称**: v-cash
- **项目类型**: 股票数据查询与分析系统
- **核心功能**: 获取A股/港股财务数据、公司公告信息
- **目标用户**: 个人投资者，用于选股参考

## 2. 技术架构

### 2.1 技术栈
| 层级 | 技术选型 | 版本 |
|------|----------|------|
| 后端 | Spring Boot | 3.2.5 |
| 数据库 | MySQL | 8.0 |
| ORM | MyBatis Plus | 3.5.5 |
| 前端 | Vue 3 | 3.4.x |
| UI框架 | Element Plus | 2.5.x |
| 构建工具 | Maven | 3.9.x |

### 2.2 部署方案
- 部署环境: 阿里云ECS
- 部署方式: 前后端分离，后端打包为jar运行

## 3. 功能模块

### 3.1 股票数据模块
- 股票基本信息管理（A股、港股）
- 财务指标数据（营收、净利润、ROE、PE等）
- 多数据源适配（可切换/扩展）

### 3.2 公告数据模块
- 公告信息查询（支持历史）
- 公告类型筛选（年报、业绩预告、增减持等）
- 公告原文链接

### 3.3 数据更新模块
- 定时任务自动更新
- 手动触发更新
- 数据源优先级策略

### 3.4 接口模块
- REST API 供前端调用
- 股票查询接口、财务数据接口、公告查询接口

## 4. 数据源规划

### 4.1 数据源优先级
| 功能 | 首选数据源 | 备选数据源 |
|------|------------|------------|
| A股财务数据 | Tushare Pro | 东方财富 |
| 港股财务数据 | 东方财富 | Tushare Pro |
| A股公告 | 巨潮资讯网 | 东方财富 |
| 港股公告 | 东方财富 | - |

### 4.2 适配器模式
```
数据源接口 (StockDataAdapter)
    ├── TushareAdapter
    ├── EastMoneyAdapter
    └── CninfoAdapter (公告)
```

## 5. 数据库设计

### 5.1 表结构

**stock_basic** - 股票基本信息
| 字段 | 类型 | 说明 |
|------|------|------|
| id | BIGINT | 主键 |
| stock_code | VARCHAR(20) | 股票代码 |
| stock_name | VARCHAR(100) | 股票名称 |
| market | VARCHAR(20) | 市场(A股/港股) |
| industry | VARCHAR(100) | 所属行业 |
| list_date | DATE | 上市日期 |
| create_time | DATETIME | 创建时间 |
| update_time | DATETIME | 更新时间 |

**stock_financial** - 财务数据
| 字段 | 类型 | 说明 |
|------|------|------|
| id | BIGINT | 主键 |
| stock_id | BIGINT | 股票ID |
| report_period | DATE | 报告期 |
| revenue | DECIMAL(20,2) | 营业收入 |
| net_profit | DECIMAL(20,2) | 净利润 |
| roe | DECIMAL(10,4) | ROE |
| pe | DECIMAL(10,4) | 市盈率 |
| pb | DECIMAL(10,4) | 市净率 |
| create_time | DATETIME | 创建时间 |

**stock_announcement** - 公告信息
| 字段 | 类型 | 说明 |
|------|------|------|
| id | BIGINT | 主键 |
| stock_id | BIGINT | 股票ID |
| title | VARCHAR(500) | 公告标题 |
| announcement_type | VARCHAR(50) | 公告类型 |
| publish_date | DATE | 发布日期 |
| source_url | VARCHAR(500) | 原文链接 |
| create_time | DATETIME | 创建时间 |

## 6. API设计

### 6.1 股票接口
- `GET /api/stock/list` - 股票列表
- `GET /api/stock/{code}` - 股票详情

### 6.2 财务数据接口
- `GET /api/financial/{stockCode}` - 获取财务数据
- `GET /api/financial/{stockCode}/latest` - 最新财务数据

### 6.3 公告接口
- `GET /api/announcement/{stockCode}` - 获取公告列表
- `GET /api/announcement/{stockCode}/latest` - 最新公告

### 6.4 数据更新接口
- `POST /api/refresh/financial` - 手动刷新财务数据
- `POST /api/refresh/announcement` - 手动刷新公告

## 7. 项目进度

### ✅ Phase 1: 基础框架 (已完成)
- [x] 项目初始化（Maven配置）
- [x] Spring Boot基础配置
- [x] 数据库环境搭建（sql/init.sql）
- [x] 后端包结构创建
  - domain/entity/ - 实体类
  - domain/enums/ - 枚举类
  - data/storage/mapper/ - MyBatis Mapper
  - config/ - 配置类
- [x] 前端项目目录结构

### Phase 2: 核心功能 (已完成)
- [x] 股票数据模型定义
- [x] 数据源适配层实现
  - [x] StockDataAdapter 接口
  - [x] TushareAdapter 实现
  - [x] EastMoneyStockAdapter 实现
- [x] 财务数据获取功能
  - [x] StockService
  - [x] FinancialService

### Phase 3: 公告功能 (待开发)
- [ ] 公告数据模型
- [ ] 公告获取功能
- [ ] 定时任务配置

### Phase 4: 后端接口层 (待开发)
- [ ] REST API开发
- [ ] 前端项目初始化

### Phase 5: 前端开发 (待开发)
- [ ] Vue 3 项目初始化
- [ ] 页面组件开发
- [ ] API调用封装
- [ ] 前后端联调

### Phase 6: 部署 (待开发)
- [ ] ECS部署配置
- [ ] 数据源配置优化

## 8. 风险与应对

| 风险 | 应对措施 |
|------|----------|
| 免费API调用限制 | 预留多个数据源，支持切换 |
| 数据更新延迟 | 定时任务+手动刷新双保险 |
| 港股数据获取难 | 优先东方财富，备选AkShare封装 |

## 9. 项目结构

### 后端 (Spring Boot)
```
v-cash/
├── pom.xml
├── sql/
│   └── init.sql
├── src/main/
│   ├── java/com/gongbotao/vcash/
│   │   ├── VCashApplication.java
│   │   ├── domain/
│   │   │   ├── entity/          # 实体类
│   │   │   └── enums/            # 枚举类
│   │   ├── data/
│   │   │   ├── source/           # 数据源适配器
│   │   │   │   ├── adapter/      # 适配器接口
│   │   │   │   ├── tushare/      # Tushare实现
│   │   │   │   ├── eastmoney/    # 东方财富实现
│   │   │   │   └── cninfo/       # 巨潮资讯实现
│   │   │   └── storage/          # 数据库操作
│   │   │       ├── mapper/       # MyBatis映射
│   │   │       └── repository/   # 仓储实现
│   │   ├── service/              # 业务逻辑
│   │   │   ├── stock/
│   │   │   ├── financial/
│   │   │   └── announcement/
│   │   ├── task/                 # 定时任务
│   │   ├── config/               # 配置类
│   │   └── controller/          # 接口层
│   └── resources/
│       └── application.yml
```

### 前端 (Vue 3)
```
vcash-web/                      # 前端项目
├── src/
│   ├── api/                    # API调用
│   ├── components/             # 公共组件
│   ├── views/                  # 页面视图
│   │   ├── stock/              # 股票页面
│   │   ├── financial/          # 财务数据页面
│   │   └── announcement/        # 公告页面
│   ├── router/                 # 路由配置
│   ├── store/                  # 状态管理
│   └── assets/                 # 静态资源
├── index.html
├── vite.config.js
└── package.json
```