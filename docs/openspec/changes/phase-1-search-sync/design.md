## Context

Phase 0 已实现股票基础 CRUD，当前仅有全量列表和手动刷新。需增加关键字搜索和定时自动同步。

## Goals / Non-Goals

**Goals:**
- 实现服务端 LIKE 关键字搜索（名称 + 代码）
- 实现 @Scheduled 定时任务每日自动同步
- cron 表达式可配置

**Non-Goals:**
- 不做全文搜索引擎（数据量小，LIKE 足够）
- 不做分布式调度

## Decisions

**服务端搜索而非前端过滤**
使用 MyBatis Plus LambdaQueryWrapper OR LIKE 查询。
替代方案（前端全量过滤）被拒绝：未来数据量大时不适用。

**Spring @Scheduled 而非 Quartz**
当前项目仅需单机定时任务，@Scheduled 足够轻量。
替代方案（Quartz / XXL-Job）被拒绝：过度设计。

## Risks / Trade-offs

| 风险 | 应对 |
|------|------|
| 定时任务执行时间长 | 短期内问题不大，后期可改异步 |
| 搜索空字符串全量返回 | 仅当 keyword 非空时添加 LIKE 条件 |
