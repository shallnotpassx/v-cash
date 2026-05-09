## 1. 领域层

- [ ] 1.1 FinancialDomainService：注入 List<StockDataAdapter>，实现 fetchFinancialData/fetchLatestFinancialData，适配器链 + stockId 绑定

## 2. 基础设施层

- [ ] 2.1 FinancialRepository：实现 IFinancialRepository（findByStockId / findLatestByStockId / save / saveAll，按 stockId+reportPeriod upsert）
- [ ] 2.2 FinancialSyncTask：遍历所有股票逐条同步财务数据

## 3. 应用层

- [ ] 3.1 FinancialApplicationService（listByStockCode / getLatestByStockCode / refreshByStockCode）

## 4. 接口层

- [ ] 4.1 FinancialController（GET /api/financial/{stockCode} / GET latest / POST refresh）

## 5. 配置

- [ ] 5.1 application.yml 新增 financial.sync.cron 配置项

## 6. 前端

- [ ] 6.1 api/index.js 新增 financial API 调用
- [ ] 6.2 router/index.js 新增 /stocks/:code/financial 路由
- [ ] 6.3 StockDetail.vue 嵌入最新财务指标卡片
- [ ] 6.4 FinancialList.vue 多期财务数据表格页

## 7. 测试

- [ ] 7.1 FinancialRepository 单元测试
- [ ] 7.2 FinancialDomainService 单元测试（Mock 适配器）
- [ ] 7.3 FinancialController 集成测试
