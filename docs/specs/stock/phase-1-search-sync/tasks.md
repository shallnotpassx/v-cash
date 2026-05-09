# Phase 1: 任务清单

## 领域层
- [ ] IStockRepository 新增 `List<StockBasic> findByKeyword(String keyword)` 方法

## 基础设施层
- [ ] StockRepository 实现 `findByKeyword`：`LambdaQueryWrapper` 对 `stockCode` 和 `stockName` 做 OR LIKE 查询
- [ ] StockSyncTask 新增：注入 `StockDomainService` + `IStockRepository`，`@Scheduled` 执行全量同步

## 应用层
- [ ] StockApplicationService.listFromDb 支持 keyword 参数，委托给 `stockRepository.findByKeyword`

## 接口层
- [ ] StockController.list() 新增 `keyword` 查询参数（与 market 可组合使用）
- [ ] VCashApplication 添加 `@EnableScheduling`

## 配置
- [ ] application.yml 新增 `stock.sync.cron` 配置项（默认 `0 0 3 * * ?`）
- [ ] StockSyncTask 读取 cron 配置

## 前端
- [ ] StockList.vue 新增搜索输入框（`el-input`，带防抖 300ms）
- [ ] 搜索触发 `getStockList({ keyword })` 重新请求

## 测试
- [ ] StockRepository 按关键字搜索的单元测试
- [ ] StockController list 带 keyword 参数的集成测试
