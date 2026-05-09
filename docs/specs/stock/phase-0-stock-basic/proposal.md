# Phase 0: 项目骨架搭通

## 概述

搭建 v-cash 项目全链路骨架：DDD 四层架构的 Spring Boot 后端 + Vue 3 前端，实现股票基础数据的列表查看、详情查看、手动刷新功能。

## 动机

项目从 Legacy 单模块迁移到 DDD 多模块架构，需要验证新架构的完整通路是否可行——从 Controller → ApplicationService → DomainService → Adapter → Repository → 数据库，以及前端 → API → 数据库的全链路。

## 功能范围

| 编号 | 功能 | 描述 | 优先级 |
|------|------|------|--------|
| F0-1 | StockController | `GET /api/stock/list?market=`、`GET /api/stock/{code}`、`POST /api/stock/refresh` | P0 |
| F0-2 | 股票数据拉取 | StockDomainService 通过适配器链（Tushare → 东方财富）获取股票列表 | P0 |
| F0-3 | 股票持久化 | IStockRepository + StockRepository 实现 upsert 模式存取 | P0 |
| F0-4 | 前端项目初始化 | Vue 3 + Vite + Element Plus + Axios + Vue Router | P0 |
| F0-5 | 股票列表页 | StockList.vue：表格展示代码/名称/市场/行业 + 刷新按钮 | P0 |
| F0-6 | 股票详情页 | StockDetail.vue：描述列表展示股票基本信息 | P0 |
| F0-7 | 领域实体定义 | StockFinancial + IFinancialRepository、StockAnnouncement + IAnnouncementRepository（骨架，暂未实现） | P1 |
| F0-8 | Legacy 代码移除 | 删除旧 src/ 单模块目录，全部代码迁移到 DDD 多模块 | P0 |

## 验收标准

- [x] `GET /api/stock/list` 返回数据库中所有股票
- [x] `GET /api/stock/list?market=A` 按市场过滤
- [x] `GET /api/stock/{code}` 返回单只股票详情，不存在返回 404
- [x] `POST /api/stock/refresh` 从数据源拉取并 upsert 到数据库
- [x] 前端可通过 Vite dev server 访问列表页和详情页
- [x] 点击列表行跳转到详情页
- [x] 刷新按钮触发后端同步并重新加载列表
- [ ] 前后端联调验证（待部署后验证）
