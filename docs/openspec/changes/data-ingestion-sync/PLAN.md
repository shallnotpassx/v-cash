# T02 — 抽象数据源适配器接口与顺序兜底执行器

> **Current Task:** T02
> **Owning Review:** R01
> **Review Status:** open

**Goal:** 实现 DataSourceCoordinator，按优先级顺序尝试多个 DataSourceAdapter，直到某个返回 SUCCESS/PARTIAL。

同时实现 T03（手动同步端点）和 T04（定时预留接口）。
