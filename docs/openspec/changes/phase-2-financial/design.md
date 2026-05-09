## Context

Phase 0 已预留 IFinancialRepository 接口和 StockFinancial 实体，StockDataAdapter 已定义 getFinancialData 方法。需实现完整链路。

## Goals / Non-Goals

**Goals:**
- 实现 FinancialRepository（upsert，键为 stockId + reportPeriod）
- 实现 FinancialDomainService 适配器链编排 + stockId 绑定
- 实现 FinancialController 三个 REST 端点
- 前端嵌入财务指标卡片 + 独立历史页

**Non-Goals:**
- 不新增数据源适配器（复用 Phase 0 的 Tushare/EastMoney 适配器）
- 不做财务指标计算（直接从数据源获取加工后的指标）

## Decisions

**复用 StockDataAdapter 接口**
财务数据与股票数据来自同一数据源，复用同一适配器接口。
替代方案（独立 FinancialDataAdapter）被拒绝：Tushare/东方财富的财务和股票 API 同属一个 SDK。

**stockId 绑定**
适配器返回的 StockFinancial 无 stockId，需通过 stockCode 查 stock_basic 获取 stockId 后绑定。
替代方案（适配器直接返回 stockId）不可行：数据源不感知系统内部 ID。

## Risks / Trade-offs

| 风险 | 应对 |
|------|------|
| 股票无 stock_basic 记录 | 先刷新股票列表确保存在 |
| 适配器返回无 stockId | FinancialDomainService 负责绑定 |
| 定时批量同步耗时 | 逐条处理，失败跳过继续 |
