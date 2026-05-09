## MODIFIED Requirements

### Requirement: Stock list supports keyword search
系统 SHALL 在 `GET /api/stock/list` 接口新增可选 `keyword` 查询参数，支持按股票代码和名称进行 LIKE 模糊匹配。

#### Scenario: Search by stock code
- **WHEN** 用户请求 `GET /api/stock/list?keyword=600000`
- **THEN** 系统返回 stockCode LIKE '%600000%' 的股票

#### Scenario: Search by stock name
- **WHEN** 用户请求 `GET /api/stock/list?keyword=浦发银行`
- **THEN** 系统返回 stockName LIKE '%浦发银行%' 的股票

#### Scenario: Combined keyword and market filter
- **WHEN** 用户请求 `GET /api/stock/list?keyword=银行&market=A`
- **THEN** 系统返回同时匹配 keyword 和 market 条件的股票（交集）

#### Scenario: Omitted keyword (backward compatible)
- **WHEN** 用户请求 `GET /api/stock/list`（不传 keyword）
- **THEN** 系统返回全部股票，行为与修改前一致

#### Scenario: Empty keyword
- **WHEN** 用户请求 `GET /api/stock/list?keyword=`
- **THEN** 系统返回全部股票，等同于不传 keyword

#### Scenario: Keyword with no match
- **WHEN** 用户请求 `GET /api/stock/list?keyword=不存在`
- **THEN** 系统返回空列表，HTTP 200
