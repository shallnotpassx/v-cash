## ADDED Requirements

### Requirement: Manual stock refresh
系统 SHALL 支持手动触发股票数据同步。

#### Scenario: Refresh all markets
- **WHEN** 用户请求 `POST /api/stock/refresh`
- **THEN** 系统从适配器链拉取全市场股票数据并 upsert

#### Scenario: Refresh specific market
- **WHEN** 用户请求 `POST /api/stock/refresh?market=A`
- **THEN** 系统仅拉取 A 股数据并 upsert

### Requirement: Adapter chain with fallback
系统 SHALL 按 Tushare → 东方财富的顺序尝试数据源。

#### Scenario: Primary adapter succeeds
- **WHEN** TushareAdapter 返回股票数据
- **THEN** 系统使用 Tushare 数据，不调用兜底适配器

#### Scenario: Primary adapter fails
- **WHEN** TushareAdapter 抛出异常或返回空
- **THEN** 系统自动降级到 EastMoneyStockAdapter
