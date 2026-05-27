# OpenSpec

本项目只在 `docs/openspec/changes/` 下维护 change 工件。

这里不保留根级 `specs/` 目录，项目级长期约束统一写在仓库根目录的 `AGENTS.md`。

## 工作方式

每个能力域都按下面的顺序推进：

1. `proposal.md`：先说明为什么要做，以及这次改什么
2. `design.md`：再说明边界、结构和关键决策
3. `specs/<capability>/spec.md`：最后把需求写成可执行的规则
4. `tasks.md`：把后续实现拆成明确任务

只有真正开始执行某个 task 时，才新增：

- `PLAN.md`
- `review/RNN.md`

## 当前 change 列表

当前仓库已经约定的能力域有：

- `stock-registry`
- `data-ingestion-sync`
- `financial-screening`
- `announcement-center`
- `deployment`
