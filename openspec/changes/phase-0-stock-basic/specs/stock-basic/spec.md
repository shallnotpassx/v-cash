## ADDED Requirements

### Requirement: List stocks by market
系统 SHALL 支持按市场（A/HK）查询股票列表。

#### Scenario: List A-share stocks
- **WHEN** 用户请求 `GET /api/stock/list?market=A`
- **THEN** 系统返回 market=A 的所有股票

#### Scenario: List all stocks
- **WHEN** 用户请求 `GET /api/stock/list` 不带 market 参数
- **THEN** 系统返回数据库中所有股票

### Requirement: Get stock detail by code
系统 SHALL 支持按股票代码查询单只股票详情。

#### Scenario: Existing stock
- **WHEN** 用户请求 `GET /api/stock/600000`
- **THEN** 系统返回该股票的完整基本信息

#### Scenario: Non-existing stock
- **WHEN** 用户请求 `GET /api/stock/NONEXIST`
- **THEN** 系统返回 404
