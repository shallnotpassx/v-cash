## Purpose

支持按关键字搜索股票，同时匹配股票代码和股票名称，可与市场过滤条件组合使用。

## Requirements

### Requirement: Search stocks by keyword
系统 SHALL 支持按关键字搜索股票，同时匹配股票代码和股票名称。

#### Scenario: Search by code
- **WHEN** 用户请求 `GET /api/stock/list?keyword=600000`
- **THEN** 系统返回 stockCode LIKE '%600000%' 的股票

#### Scenario: Search by name
- **WHEN** 用户请求 `GET /api/stock/list?keyword=浦发银行`
- **THEN** 系统返回 stockName LIKE '%浦发银行%' 的股票

#### Scenario: Search with market filter
- **WHEN** 用户请求 `GET /api/stock/list?keyword=银行&market=A`
- **THEN** 系统返回同时匹配关键字和市场条件的股票（交集）
