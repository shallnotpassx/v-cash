## Context

项目从 Legacy 单模块迁移到 DDD 多模块架构。已有数据库表（stock_basic / stock_financial / stock_announcement），需验证 DDD 四层完整通路。

## Goals / Non-Goals

**Goals:**
- 搭建 DDD 四层 Maven 多模块项目结构
- 实现股票列表/详情/刷新完整链路
- 适配器链策略+兜底模式获取数据
- Vue 3 前端基础框架

**Non-Goals:**
- 不实现搜索功能（Phase 1）
- 不实现财报/公告模块（Phase 2/3）

## Decisions

**DDD 四层架构**
领域层定义接口，基础设施层实现，应用层编排，接口层暴露 REST。
替代方案（传统三层架构）被拒绝，因为模块耦合度高、不利于扩展。

**适配器策略模式**
Spring 自动注入 List<StockDataAdapter>，按顺序尝试直到第一个成功。
替代方案（配置中心指定数据源）被拒绝：不够灵活，无法自动降级。

**Upsert 持久化**
存在则更新，不存在则插入，避免重复数据。
替代方案（先删后插）被拒绝：可能导致并发问题。

## Risks / Trade-offs

| 风险 | 应对 |
|------|------|
| Tushare token 未配置 | 降级到东方财富适配器 |
| 东方财富 API 变更 | 返回空列表，数据库已有数据仍可用 |
| DDD 层级依赖错误 | 严格 lint 检查分层规则 |
