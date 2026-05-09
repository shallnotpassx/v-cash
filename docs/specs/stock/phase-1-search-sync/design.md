# Phase 1: 股票搜索与同步 — 技术设计

## 架构决策

### 搜索方案：服务端 LIKE 查询

- `IStockRepository` 新增 `findByKeyword(keyword)` 方法
- 使用 MyBatis Plus `LambdaQueryWrapper.like()`，匹配 `stockCode` 和 `stockName` 两列
- 前端不维护全量数据做本地过滤，走服务端查询

### 定时任务方案：Spring `@Scheduled`

- 新建 `StockSyncTask` 组件，注入 `StockDomainService`
- 使用 `@Scheduled(cron = "...")` 每日凌晨执行
- cron 表达式放入 `application.yml` 可配置，默认 `0 0 3 * * ?`（凌晨 3 点）
- 同步失败仅 log.error，不影响应用正常运行

## 数据流

搜索：
```
用户输入关键字
  → StockController.list(keyword)
    → StockApplicationService.listFromDb(keyword)  // 新增 keyword 参数
      → StockRepository.findAll(keyword)            // 添加 LIKE 条件
  ← 返回过滤后的 List<StockBasic>
```

定时同步：
```
@Scheduled 触发
  → StockSyncTask.syncStocks()
    → StockDomainService.fetchStockList(null)  // 拉取全部市场
    → StockRepository.saveAll(stocks)
```

## API 设计

| 方法 | 路径 | 变更 | 说明 |
|------|------|------|------|
| GET | `/api/stock/list` | 新增参数 `keyword` | 与原 `market` 参数可同时使用，交集过滤 |

## 数据库变更

无。

## 涉及文件

| 文件 | 变更类型 | 说明 |
|------|----------|------|
| `IStockRepository.java` | 修改 | 新增 `findByKeyword(String keyword)` / 或将关键字参数加入 findAll |
| `StockRepository.java` | 修改 | 实现关键字 LIKE 查询 |
| `StockApplicationService.java` | 修改 | 新增带 keyword 的查询方法 |
| `StockController.java` | 修改 | `list()` 接口新增 keyword 参数 |
| `StockList.vue` | 修改 | 新增搜索输入框，输入后重新请求 |
| `StockSyncTask.java` | 新增 | 定时任务类 |
| `application.yml` | 修改 | 新增 cron 配置项 |
| `VCashApplication.java` | 修改 | 新增 `@EnableScheduling` |

## 风险与对策

| 风险 | 影响 | 应对 |
|------|------|------|
| 定时任务执行时间过长 | 短期内问题不大 | 考虑异步执行 |
| cron 表达式写死不灵活 | 不易调试 | 放入配置文件，支持动态修改 |
| 搜索空字符串全量返回 | 数据量小时无影响 | 仅当 keyword 非空时添加 LIKE 条件 |
