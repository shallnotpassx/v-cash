## Why

股票列表只能看基本信息，投资决策需要营收、净利润、ROE、PE、PB 等财务指标。Phase 0 已预留 IFinancialRepository 和 StockFinancial 实体骨架，需实现完整链路。

## What Changes

- 实现 FinancialRepository 仓储（upsert 模式，按 stockId + reportPeriod 查重）
- 新增 FinancialDomainService 适配器链编排（绑定 stockId）
- 新增 FinancialApplicationService 用例编排
- 新增 FinancialController REST 端点（list / latest / refresh）
- 新增 FinancialSyncTask 定时批量同步
- 前端新增财务指标卡片（嵌入详情页）和独立财报历史页

## Capabilities

### New Capabilities
- `financial-data`: 财务数据查询——历史列表、最新指标、手动刷新

### Modified Capabilities
- `stock-basic`: 股票详情页内嵌最新财务指标卡片

## Impact

- 新增 FinancialRepository / FinancialDomainService / FinancialApplicationService / FinancialController
- 新增 FinancialSyncTask 定时任务
- 前端新增 FinancialList.vue 页面，修改 StockDetail.vue
- 复用 Phase 0 的 StockDataAdapter 接口（TushareAdapter / EastMoneyStockAdapter 已有实现）
