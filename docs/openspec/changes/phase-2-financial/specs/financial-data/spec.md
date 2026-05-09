## ADDED Requirements

### Requirement: List financial data by stock
系统 SHALL 支持查询指定股票的多期历史财务数据。

#### Scenario: List financial history
- **WHEN** 用户请求 `GET /api/financial/600000`
- **THEN** 系统返回按报告期降序排列的财务数据列表

### Requirement: Get latest financial data
系统 SHALL 支持查询最新一期财务指标。

#### Scenario: Latest financial data
- **WHEN** 用户请求 `GET /api/financial/600000/latest`
- **THEN** 系统返回最新一期财务数据

#### Scenario: No financial data
- **WHEN** 用户请求 `GET /api/financial/NONEXIST/latest`
- **THEN** 系统返回 404

### Requirement: Refresh financial data
系统 SHALL 支持手动触发财务数据同步。

#### Scenario: Refresh financials
- **WHEN** 用户请求 `POST /api/financial/600000/refresh`
- **THEN** 系统从适配器拉取财务数据并 upsert

### Requirement: Batch sync
系统 SHALL 支持定时批量同步所有股票的财务数据。

#### Scenario: Scheduled batch sync
- **WHEN** 到达配置的 cron 时间
- **THEN** 系统遍历所有股票逐条同步财务数据
