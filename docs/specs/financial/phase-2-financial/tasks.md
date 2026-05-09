# Phase 2: 任务清单

## 领域层
- [ ] FinancialDomainService：注入 `List<StockDataAdapter>`，实现 `fetchFinancialData(stockCode)` 和 `fetchLatestFinancialData(stockCode)`，适配器链 + stockId 绑定

## 基础设施层
- [ ] FinancialRepository：实现 IFinancialRepository
  - [ ] `findByStockId`：按 stockId 查询，按 reportPeriod 降序
  - [ ] `findLatestByStockId`：limit 1，按 reportPeriod 降序
  - [ ] `save`：按 stockId + reportPeriod 查重，upsert
  - [ ] `saveAll`：批量 upsert
- [ ] FinancialSyncTask：注入 `IStockRepository` + `FinancialDomainService` + `IFinancialRepository`，遍历所有股票逐条同步财务数据

## 应用层
- [ ] FinancialApplicationService
  - [ ] `listByStockCode(stockCode)`：查 stockId → 查财务列表
  - [ ] `getLatestByStockCode(stockCode)`：查 stockId → 查最新一条
  - [ ] `refreshByStockCode(stockCode)`：查 stockId → 拉取 → 绑定 → 保存

## 接口层
- [ ] FinancialController
  - [ ] `GET /api/financial/{stockCode}` → list
  - [ ] `GET /api/financial/{stockCode}/latest` → latest
  - [ ] `POST /api/financial/{stockCode}/refresh` → refresh

## 配置
- [ ] application.yml 新增 `financial.sync.cron` 配置项

## 前端
- [ ] api/index.js 新增 `getFinancialData(code)`、`getLatestFinancial(code)`、`refreshFinancialData(code)`
- [ ] router/index.js 新增 `/stocks/:code/financial` 路由
- [ ] StockDetail.vue 嵌入最新财务指标卡片（el-card 展示 revenue/netProfit/roe/pe/pb）
- [ ] FinancialList.vue：el-table 展示多期数据，含刷新按钮，返回链接
- [ ] StockDetail.vue 添加"查看财报"链接跳转到 FinancialList

## 测试
- [ ] FinancialRepository 单元测试
- [ ] FinancialDomainService 单元测试（Mock 适配器）
- [ ] FinancialController 集成测试
