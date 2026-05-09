## Why

当前股票列表只能全量查看，无法按名称或代码快速定位目标股票。同时股票数据需要定期从数据源同步以保持最新，不能仅依赖手动刷新。

## What Changes

- StockController.list() 新增 keyword 参数支持关键字搜索（名称 + 代码 LIKE 匹配）
- 新增 StockSyncTask 定时任务，使用 @Scheduled 每日凌晨自动同步
- 前端列表页新增搜索输入框（300ms 防抖）

## Capabilities

### New Capabilities
- `stock-search`: 按股票代码或名称关键字搜索过滤
- `stock-sync-automation`: 定时自动从数据源同步股票列表

### Modified Capabilities
- `stock-basic`: list 接口新增 keyword 查询参数
- `stock-refresh`: 新增定时调度方式，不再仅依赖手动触发

## Impact

- IStockRepository 新增 findByKeyword 方法
- 新增 StockSyncTask 定时任务组件
- StockController.list() 新增 keyword 参数
- application.yml 新增 cron 配置项
- 前端 StockList.vue 新增搜索输入框
