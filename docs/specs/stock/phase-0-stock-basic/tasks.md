# Phase 0: 任务清单

## 领域层
- [x] StockBasic 实体（MyBatis Plus 映射 stock_basic 表）
- [x] StockFinancial 实体（MyBatis Plus 映射 stock_financial 表）
- [x] StockAnnouncement 实体（MyBatis Plus 映射 stock_announcement 表）
- [x] IStockRepository 接口定义
- [x] IFinancialRepository 接口定义
- [x] IAnnouncementRepository 接口定义
- [x] StockDataAdapter 接口定义（getStockList / getStockDetail / getFinancialData / getLatestFinancialData）
- [x] StockDomainService（适配器链编排，按顺序尝试，取第一个成功结果）

## 应用层
- [x] StockApplicationService（listFromDb / getByCode / refreshStockList）

## 基础设施层
- [x] StockBasicMapper（MyBatis Plus BaseMapper）
- [x] StockFinancialMapper（MyBatis Plus BaseMapper）
- [x] StockRepository 实现 IStockRepository（upsert 模式）
- [x] TushareAdapter 实现 StockDataAdapter
- [x] EastMoneyStockAdapter 实现 StockDataAdapter
- [x] RestTemplateConfig
- [x] MyBatisPlusConfig（逻辑删除 + 自动填充）

## 接口层
- [x] StockController（list / detail / refresh）
- [x] VCashApplication 启动类 + @MapperScan
- [x] application.yml（端口 8089，MySQL 配置，Tushare 配置）

## 前端
- [x] Vue 3 + Vite + Element Plus 项目初始化（package.json / vite.config.js）
- [x] Axios 请求封装（api/index.js）
- [x] Vue Router 配置（/stocks, /stocks/:code, redirect）
- [x] StockList.vue（表格展示 + 刷新按钮 + 行点击跳转）
- [x] StockDetail.vue（描述列表展示详情 + 返回按钮）
- [x] App.vue 基础布局 + router-view

## 清理
- [x] 删除 Legacy src/ 单模块目录
- [x] AGENTS.md 更新
- [x] PLAN.md 更新
