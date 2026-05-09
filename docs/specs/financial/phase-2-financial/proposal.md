# Phase 2: 财报模块开发

## 概述

实现财报数据模块的完整 DDD 四层链路：仓储实现、领域服务（适配器编排）、应用服务、REST 接口、前端展示页。复用已有的 `IFinancialRepository` 接口、`StockFinancial` 实体和 `StockDataAdapter.getFinancialData()` 方法。

## 动机

股票列表只能看基本信息，投资决策需要营收、净利润、ROE、PE、PB 等财务指标。Phase 0 已经预留了接口和实体骨架，现在需要实现完整链路让用户能看到财务数据。

## 功能范围

| 编号 | 功能 | 描述 | 优先级 |
|------|------|------|--------|
| F3 | 查看历史财务数据 | `GET /api/financial/{stockCode}` 返回多期财报数据，前端表格展示 | P0 |
| F4 | 查看最新财务指标 | `GET /api/financial/{stockCode}/latest` 返回最新一期，嵌入详情页 | P0 |
| F5 | 手动刷新财务数据 | `POST /api/financial/{stockCode}/refresh` 从数据源拉取并 upsert | P0 |
| F6 | 定时同步财务数据 | `FinancialSyncTask @Scheduled` 对所有股票批量同步财务数据 | P1 |

## 验收标准

- [ ] `GET /api/financial/{stockCode}` 返回按报告期降序排列的财务数据列表
- [ ] `GET /api/financial/{stockCode}/latest` 返回最新一期财务指标
- [ ] `POST /api/financial/{stockCode}/refresh` 从适配器拉取数据并 upsert，返回成功
- [ ] 前端股票详情页内嵌最新财务指标卡片
- [ ] 前端独立财报页展示多期历史数据表格
- [ ] 定时任务每日自动同步财务数据
