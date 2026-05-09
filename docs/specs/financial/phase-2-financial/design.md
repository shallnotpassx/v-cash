# Phase 2: 财报模块 — 技术设计

## 架构决策

### 复用适配器接口

`StockDataAdapter` 已在 Phase 0 定义了 `getFinancialData(String stockCode)` 和 `getLatestFinancialData(String stockCode)`，TushareAdapter 和 EastMoneyStockAdapter 均已实现。Phase 2 不新增适配器，仅新增编排层。

### FinancialDomainService 编排

与 `StockDomainService` 相同的策略+兜底模式：

```
FinancialDomainService.fetchFinancialData(stockCode)
  → TushareAdapter.getFinancialData(stockCode)     ← 首选
  → [失败] EastMoneyStockAdapter.getFinancialData(stockCode)  ← 兜底
```

差异：需要将适配器返回的 `List<StockFinancial>` 中的每条记录绑定 `stockId`（通过 `stockCode` 查 `stock_basic` 获取 `id`）。

### FinancialRepository upsert

`StockFinancial` 无业务唯一标识（无 `stockCode`，只有 `stockId`），upsert 键为 `stockId + reportPeriod` 组合。

## 数据流

查询：
```
GET /api/financial/{stockCode}
  → FinancialController.list(stockCode)
    → FinancialApplicationService.listByStockCode(stockCode)
      → IStockRepository.findByCode(stockCode)  ← 获取 stockId
      → IFinancialRepository.findByStockId(stockId)
  ← 返回 List<StockFinancial>
```

刷新：
```
POST /api/financial/{stockCode}/refresh
  → FinancialController.refresh(stockCode)
    → FinancialApplicationService.refreshByStockCode(stockCode)
      → IStockRepository.findByCode(stockCode)  ← 获取 stockId
      → FinancialDomainService.fetchFinancialData(stockCode)
      → 绑定 stockId 到每条记录
      → IFinancialRepository.saveAll(financials)  ← upsert
```

## API 设计

| 方法 | 路径 | 请求参数 | 响应 |
|------|------|----------|------|
| GET | `/api/financial/{stockCode}` | `stockCode` (路径) | `List<StockFinancial>` |
| GET | `/api/financial/{stockCode}/latest` | `stockCode` (路径) | `StockFinancial` 或 404 |
| POST | `/api/financial/{stockCode}/refresh` | `stockCode` (路径) | `"ok"` |

## 数据库变更

无。使用已有 `stock_financial` 表。

## 涉及文件

| 文件 | 变更 | 说明 |
|------|------|------|
| `FinancialRepository.java` | 新增 | 实现 IFinancialRepository，upsert |
| `FinancialDomainService.java` | 新增 | 适配器链编排 + stockId 绑定 |
| `FinancialApplicationService.java` | 新增 | listByStockCode / getLatestByStockCode / refreshByStockCode |
| `FinancialController.java` | 新增 | REST 三个端点 |
| `FinancialSyncTask.java` | 新增 | 定时批量同步 |
| `StockDetail.vue` | 修改 | 嵌入最新财务指标卡片 |
| `FinancialList.vue` | 新增 | 多期财务数据表格页 |
| `api/index.js` | 修改 | 新增 financial API 调用 |
| `router/index.js` | 修改 | 新增 `/stocks/:code/financial` 路由 |
| `application.yml` | 修改 | 新增 financial sync cron 配置 |

## 风险与对策

| 风险 | 影响 | 应对 |
|------|------|------|
| 股票无 stock_basic 记录 | 无法获取 stockId 绑定 | 先刷新股票列表确保存在 |
| 适配器返回无 stockId 的数据 | saveAll 保存不完整 | FinancialDomainService 负责绑定 |
| 定时任务批量同步耗时 | 执行时间过长 | 逐条处理，失败跳过继续 |
