# Phase 0: 项目骨架搭通 — 技术设计

## 架构决策

### DDD 四层架构

```
v-cash-api (接口层)
    └── controller/StockController.java
        → v-cash-application (应用层)
            └── application/stock/service/StockApplicationService.java
                → v-cash-domain (领域层)
                    ├── repository/IStockRepository.java
                    ├── service/StockDomainService.java
                    └── adapter/StockDataAdapter.java
                        ← v-cash-infrastructure (基础设施层)
                            ├── tushare/TushareAdapter.java
                            ├── eastmoney/EastMoneyStockAdapter.java
                            └── persistence/repository/StockRepository.java
```

- **接口层** 不直接调用基础设施层，通过应用层编排
- **应用层** 编排领域服务 + 仓储，处理事务边界
- **领域层** 定义适配器接口和仓储接口，不依赖具体实现
- **基础设施层** 实现适配器和仓储的具体逻辑

### 适配器策略

Spring 自动注入所有 `StockDataAdapter` 实现到 `StockDomainService` 的 `List<StockDataAdapter>`，按 Spring 加载顺序尝试，第一个返回非空结果的即为最终结果。失败自动降级到下一个适配器。

## 数据流

```
用户点击刷新
  → StockController.refresh(market)
    → StockApplicationService.refreshStockList(market)
      → StockDomainService.fetchStockList(market)
        → TushareAdapter.getStockList(market)  ← 首选
        → [失败] EastMoneyStockAdapter.getStockList(market)  ← 兜底
      → StockRepository.saveAll(stocks)  ← upsert 逐条保存
```

查询流程：
```
用户访问页面
  → StockController.list(market)
    → StockApplicationService.listFromDb(market)
      → StockRepository.findAll(market)
  ← 返回 List<StockBasic>
```

## API 设计

| 方法 | 路径 | 请求参数 | 响应 |
|------|------|----------|------|
| GET | `/api/stock/list` | `market` (可选, A/HK) | `List<StockBasic>` |
| GET | `/api/stock/{code}` | `code` (路径参数) | `StockBasic` 或 404 |
| POST | `/api/stock/refresh` | `market` (可选) | `"ok"` |

## 数据库变更

无需变更，使用已有表 `stock_basic`、`stock_financial`、`stock_announcement`。

## 前端设计

- **StockList.vue**：`el-table` 展示，`@row-click` 跳转详情，顶部 `el-button` 触发刷新
- **StockDetail.vue**：`el-descriptions` 展示详情，顶部返回按钮
- **路由**：`/stocks` → 列表页，`/stocks/:code` → 详情页，`/` → redirect

## 风险与对策

| 风险 | 影响 | 应对 |
|------|------|------|
| Tushare token 未配置 | 列表拉取失败 | 降级到东方财富适配器 |
| 东方财富 API 变更 | 兜底也失败 | 返回空列表，数据库已有数据仍可用 |
| DDD 层级依赖错误 | 编译失败 | 严格遵守分层规则 |
