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
- 部署方式: 本地 Docker 容器编排 (docker-compose)
- 后端: Spring Boot jar 打包为 Docker 镜像
- 前端: Nginx 容器托管 Vue 3 构建产物
- 数据库: MySQL 8.0 容器 + 自动初始化

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
- `GET /api/stock/list?market=A|HK` - 股票列表（支持按市场过滤）
- `GET /api/stock/list?keyword=xxx` - 按名称/代码搜索股票
- `GET /api/stock/{code}` - 股票详情
- `POST /api/stock/refresh?market=A|HK` - 手动刷新股票列表

### 6.2 财务数据接口
- `GET /api/financial/{stockCode}` - 获取历史财务数据
- `GET /api/financial/{stockCode}/latest` - 最新财务数据
- `POST /api/financial/{stockCode}/refresh` - 手动刷新财务数据

### 6.3 公告接口
- `GET /api/announcement/{stockCode}` - 获取公告列表
- `GET /api/announcement/{stockCode}?type=xxx` - 按类型筛选公告
- `POST /api/announcement/{stockCode}/refresh` - 手动刷新公告

## 7. 项目进度

### ✅ 已完成 (旧体系迁移)
- [x] 项目初始化（Maven 多模块配置）
- [x] Spring Boot 基础配置 + MyBatis Plus 配置
- [x] 数据库环境搭建（sql/init.sql）
- [x] DDD 四层架构搭建（domain / application / infrastructure / api）
- [x] 股票后端核心: Entity + 仓储接口/实现 + 领域服务 + 应用服务
- [x] 数据源适配: StockDataAdapter 接口 + TushareAdapter + EastMoneyStockAdapter
- [x] 财务后端核心: Entity + 仓储接口
- [x] 公告后端核心: Entity + 仓储接口
- [x] Legacy 单模块代码移除，全部迁移到 DDD 多模块

---

### Phase 0: 项目骨架搭通 ✅

- [x] StockController 实现（列表/详情/刷新接口）
- [x] 前端项目初始化
  - [x] Vue 3 + Vite + Element Plus 项目搭建
  - [x] Axios 请求封装
  - [x] 路由配置 + 基础布局
- [x] 股票模块全链路验证
  - [x] 股票列表页开发（表格展示代码/名称/市场/行业）
  - [x] 股票详情页开发（展示股票基本信息）
  - [x] 刷新按钮联动后端 API
  - [ ] 前后端联调验证（待部署后验证）

### Phase 1: 股票模块功能补全

| 功能点 | 后端 | 前端 |
|--------|------|------|
| F1: 按名称/代码搜索股票 | IStockRepository.findByKeyword() + listByKeyword() | 列表页搜索框 |
| F2: 定时自动同步股票列表 | StockSyncTask @Scheduled 每日同步 | 后台执行 |

### Phase 2: 财务模块开发

- [ ] 后端基础建设
  - [ ] FinancialRepository 实现 IFinancialRepository（upsert 模式）
  - [ ] FinancialDomainService（适配器编排）
  - [ ] FinancialApplicationService（业务编排）
  - [ ] FinancialController（REST 接口）

| 功能点 | 后端 | 前端 |
|--------|------|------|
| F3: 查看历史财务数据 | FinancialApplicationService.listByStockCode() | 财务表格页（多期 revenue/netProfit/roe/pe/pb） |
| F4: 查看最新财务指标 | FinancialApplicationService.getLatestByStockCode() | 股票详情页嵌入指标卡片 |
| F5: 手动刷新财务数据 | POST /api/financial/{stockCode}/refresh | 详情页刷新按钮 |
| F6: 定时自动同步财务 | FinancialSyncTask @Scheduled | 后台执行 |

### Phase 3: 公告模块开发

- [ ] 后端基础建设
  - [ ] AnnouncementMapper（MyBatis Plus BaseMapper）
  - [ ] AnnouncementRepository 实现 IAnnouncementRepository
  - [ ] AnnouncementDataAdapter 接口 + CninfoAdapter + EastMoneyAnnouncementAdapter
  - [ ] AnnouncementDomainService（适配器编排）
  - [ ] AnnouncementApplicationService（业务编排）
  - [ ] AnnouncementController（REST 接口）

| 功能点 | 后端 | 前端 |
|--------|------|------|
| F7: 查看公告列表 | AnnouncementApplicationService.listByStockCode() | 公告列表页 |
| F8: 按公告类型筛选 | 同上 + type 参数过滤 | 列表页筛选下拉框 |
| F9: 查看公告原文 | 返回 source_url | 列表页"查看原文"链接 |
| F10: 手动刷新公告 | POST /api/announcement/{stockCode}/refresh | 刷新按钮 |
| F11: 定时自动同步公告 | AnnouncementSyncTask @Scheduled | 后台执行 |

### Phase 4: 本地 Docker 部署

| 功能点 | 说明 |
|--------|------|
| F12: Docker 化后端 | 编写 Dockerfile，Spring Boot jar 打包镜像 |
| F13: Docker 化前端 | Nginx 容器托管 Vue 3 构建产物 |
| F14: MySQL 容器 | docker-compose 编排 MySQL 8.0 + 自动初始化 |
| F15: docker-compose 一键启动 | 三容器编排，网络互通，端口映射 |
| F16: 本地验证 | 浏览器访问确认全链路正常 |

## 8. 风险与应对

| 风险 | 应对措施 |
|------|----------|
| 免费API调用限制 | 预留多个数据源，支持切换 |
| 数据更新延迟 | 定时任务+手动刷新双保险 |
| 港股数据获取难 | 优先东方财富，备选AkShare封装 |

## 9. 项目结构

### 后端 (Spring Boot DDD)
```
v-cash/
├── pom.xml
├── sql/
│   └── init.sql
├── v-cash-domain/              # 领域层
│   └── src/main/java/com/gongbotao/vcash/domain/
│       ├── stock/
│       │   ├── entity/StockBasic.java
│       │   ├── repository/IStockRepository.java
│       │   ├── service/StockDomainService.java
│       │   └── adapter/StockDataAdapter.java
│       ├── financial/
│       │   ├── entity/StockFinancial.java
│       │   └── repository/IFinancialRepository.java
│       └── announcement/
│           ├── entity/StockAnnouncement.java
│           └── repository/IAnnouncementRepository.java
├── v-cash-application/         # 应用层
│   └── src/main/java/com/gongbotao/vcash/application/
│       └── stock/service/StockApplicationService.java
├── v-cash-infrastructure/      # 基础设施层
│   └── src/main/java/com/gongbotao/vcash/infrastructure/
│       ├── config/
│       ├── tushare/TushareAdapter.java
│       ├── eastmoney/EastMoneyStockAdapter.java
│       └── persistence/
│           ├── mapper/
│           └── repository/
├── v-cash-api/                 # 接口层
│   └── src/main/java/com/gongbotao/vcash/
│       ├── VCashApplication.java
│       └── controller/StockController.java
├── docs/
│   ├── PLAN.md
│   ├── SETUP.md
│   ├── TEST_CASES.md
│   └── TEST_REPORT.md
└── vcash-web/                  # 前端 (Vue 3 + Vite + Element Plus)
    ├── src/
    │   ├── api/
    │   │   └── index.js
    │   ├── router/
    │   │   └── index.js
    │   ├── views/
    │   │   ├── StockList.vue
    │   │   └── StockDetail.vue
    │   ├── App.vue
    │   └── main.js
    ├── index.html
    ├── vite.config.js
    └── package.json
```
