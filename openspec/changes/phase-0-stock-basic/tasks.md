## 1. 领域层

- [x] 1.1 StockBasic / StockFinancial / StockAnnouncement 实体（MyBatis Plus 映射）
- [x] 1.2 IStockRepository / IFinancialRepository / IAnnouncementRepository 接口
- [x] 1.3 StockDataAdapter 接口（getStockList / getStockDetail / getFinancialData / getLatestFinancialData）
- [x] 1.4 StockDomainService（适配器链编排，按顺序尝试取第一个成功结果）

## 2. 应用层

- [x] 2.1 StockApplicationService（listFromDb / getByCode / refreshStockList）

## 3. 基础设施层

- [x] 3.1 StockBasicMapper / StockFinancialMapper（MyBatis Plus BaseMapper）
- [x] 3.2 StockRepository 实现 IStockRepository（upsert 模式）
- [x] 3.3 TushareAdapter 实现 StockDataAdapter
- [x] 3.4 EastMoneyStockAdapter 实现 StockDataAdapter
- [x] 3.5 RestTemplateConfig / MyBatisPlusConfig（逻辑删除 + 自动填充）

## 4. 接口层

- [x] 4.1 StockController（list / detail / refresh）
- [x] 4.2 VCashApplication 启动类 + @MapperScan
- [x] 4.3 application.yml（端口 8089，MySQL 配置，Tushare 配置）

## 5. 前端

- [x] 5.1 Vue 3 + Vite + Element Plus 项目初始化
- [x] 5.2 Axios 请求封装（api/index.js）
- [x] 5.3 Vue Router 配置（/stocks, /stocks/:code, redirect）
- [x] 5.4 StockList.vue + StockDetail.vue
- [x] 5.5 App.vue 基础布局 + router-view

## 6. 清理

- [x] 6.1 删除 Legacy src/ 单模块目录
- [x] 6.2 AGENTS.md 更新
- [x] 6.3 PLAN.md 更新
