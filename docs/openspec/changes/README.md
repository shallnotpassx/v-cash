# Changes

这是 v-cash 当前规划中的 change 清单。

## 依赖关系

- `stock-registry`：根能力域，负责统一股票主数据
- `data-ingestion-sync`：依赖 `stock-registry`，负责按代码拉取和同步
- `financial-screening`：依赖 `stock-registry` 和 `data-ingestion-sync`
- `announcement-center`：只依赖 `stock-registry`
- `deployment`：交付能力，不反向影响业务边界

## 状态说明

当前所有 change 都处于 `proposed` 状态。

这意味着：

- 需求边界已经确定
- 技术方向已经定下
- 还没有进入逐 task 的实现与 review 阶段
