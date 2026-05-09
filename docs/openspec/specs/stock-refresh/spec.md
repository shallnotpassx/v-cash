## Purpose

提供股票数据同步能力，支持手动触发刷新和适配器链自动降级。

## Requirements

### Requirement: Manual stock refresh
系统 SHALL 支持手动触发股票数据从数据源同步到数据库。

#### Scenario: Refresh all markets
- **WHEN** 用户请求 `POST /api/stock/refresh` 不带 market 参数
- **THEN** 系统从适配器链拉取全市场股票数据并 upsert 到数据库

#### Scenario: Refresh specific market
- **WHEN** 用户请求 `POST /api/stock/refresh?market=A`
- **THEN** 系统仅拉取 A 股数据并 upsert

#### Scenario: Unknown market param
- **WHEN** 用户请求 `POST /api/stock/refresh?market=XX`
- **THEN** 系统返回 400 Bad Request

### Requirement: Adapter chain with fallback
系统 SHALL 按 Tushare → 东方财富的顺序尝试数据源，第一个返回成功结果即采用。

#### Scenario: Primary adapter succeeds
- **WHEN** TushareAdapter 返回股票数据
- **THEN** 系统使用 Tushare 数据，不调用兜底适配器

#### Scenario: Primary adapter fails
- **WHEN** TushareAdapter 抛出异常或返回空
- **THEN** 系统自动降级到 EastMoneyStockAdapter

#### Scenario: All adapters fail
- **WHEN** 所有适配器均失败
- **THEN** 系统记录 ERROR 日志，向上层抛领域异常

#### Scenario: Refresh is idempotent
- **WHEN** 连续两次执行 refresh
- **THEN** 第二次不产生重复数据（按 stockCode upsert）
