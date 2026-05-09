## Context

Phase 0 已实现股票基础 CRUD，当前仅有全量列表和手动刷新。需增加关键字搜索和定时自动同步。

## Goals / Non-Goals

**Goals:**
- 实现服务端 LIKE 关键字搜索（名称 + 代码）
- 实现 @Scheduled 定时任务每日自动同步
- cron 表达式可配置
- 搜索与 market 过滤可组合使用（交集）

**Non-Goals:**
- 不做全文搜索引擎（数据量小，LIKE 足够）
- 不做分布式调度

## Architecture

### 调用链

```
Frontend (StockList.vue)                   Spring Scheduler
  │  keyword + market                        │  cron trigger
  ▼                                          ▼
StockController.list(keyword, market)      StockSyncTask.syncAllStocks()
  │                                          │
  ▼                                          ▼
StockApplicationService.listFromDb()       StockDomainService.refreshAll()
  │                                          │
  ├─ keyword 非空 → IStockRepository        ├─ IStockRepository.findAll()
  │   .findByKeyword(keyword)                ├─ StockDataAdapter.refreshStocks(market)
  ├─ keyword 为空 → IStockRepository         │   (Tushare → EastMoney 降级)
  │   .findAll()                             └─ IStockRepository.upsertAll(stockList)
  └─ 返回 List<StockBasic> → DTO
```

### 接口边界

| 组件 | 方法签名 | 输入约束 | 输出 |
|------|---------|---------|------|
| `IStockRepository` | `findByKeyword(String keyword)` | keyword 可为 null/空字符串，null/空时行为等同于 findAll | `List<StockBasic>` |
| `StockApplicationService` | `listFromDb(String market, String keyword)` | market 可选（A/HK/null），keyword 可选 | `List<StockDTO>` |
| `StockController` | `list(@RequestParam(required=false) String market, @RequestParam(required=false) String keyword)` | 同上，HTTP GET | `ResponseEntity<List<StockDTO>>` |
| `StockSyncTask` | `syncAllStocks()` | 无参，由 @Scheduled 驱动 | void（副作用：数据库更新） |

## Decisions

**服务端搜索而非前端过滤**
使用 MyBatis Plus LambdaQueryWrapper OR LIKE 查询。
替代方案（前端全量过滤）被拒绝：未来数据量大时不适用。

**Spring @Scheduled 而非 Quartz**
当前项目仅需单机定时任务，@Scheduled 足够轻量。
替代方案（Quartz / XXL-Job）被拒绝：过度设计。

## 数据约束

| 约束 | 说明 |
|------|------|
| 关键字匹配 | `stockCode LIKE '%keyword%' OR stockName LIKE '%keyword%'`，大小写由数据库默认（MySQL LIKE 大小写不敏感） |
| 空关键字 | keyword 为 null 或空字符串时不添加 LIKE 条件，等同于全量查询 |
| market + keyword 组合 | AND 关系（交集），两个条件均非空时才组合 |
| 同步唯一键 | stockCode（与 Phase 0 一致，upsert 不产生重复） |
| 定时任务 cron 默认值 | `0 0 3 * * ?`（每日凌晨 3 点），可在 application.yml 中覆盖 |

## 失败策略

| 场景 | 行为 |
|------|------|
| 定时同步适配器全部失败 | 记录 ERROR 日志，不抛异常（避免影响下一次调度） |
| 定时同步单条股票适配器失败 | 记录 ERROR 日志（含 stockCode），继续处理下一条 |
| 上一次同步未完成 + 下次触发到达 | 跳过本次执行，记录 WARN 日志（@Scheduled 默认不并发） |
| 搜索无结果 | 返回空列表 `[]`，HTTP 200 |

## 显式推迟项

以下需求不在 Phase 1 范围内，记录原因：

| 推迟项 | 原因 | 计划 |
|--------|------|------|
| 前端分页 | 当前总数据量约 5000 条，全量返回可接受 | 数据量显著增长时再补 |
| 搜索高亮（highlight） | 前端需求，不改变服务端契约 | 后续 UI 增强阶段 |
| 同步进度反馈（webhook/通知） | 过度设计 | 后续运维阶段考虑 |
| 同步日志持久化到数据库 | 当前日志框架输出到文件已足够 | 运维需求明确后再做 |

## Risks / Trade-offs

| 风险 | 应对 |
|------|------|
| 定时任务执行时间长 | 短期内问题不大，后期可改异步 |
| 搜索空字符串全量返回 | 仅当 keyword 非空时添加 LIKE 条件 |
| 定时任务单点风险（单机 @Scheduled） | Docker 部署时确保单实例，后续需要时可迁移到分布式调度 |
