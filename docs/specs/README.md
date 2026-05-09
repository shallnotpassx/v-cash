# v-cash Specs 索引

## 状态总览

| 模块 | Phase | 名称 | 状态 | 文件 |
|------|-------|------|------|------|
| stock | Phase 0 | 项目骨架搭通 | ✅ done | [proposal](stock/phase-0-stock-basic/proposal.md) / [design](stock/phase-0-stock-basic/design.md) / [tasks](stock/phase-0-stock-basic/tasks.md) |
| stock | Phase 1 | 搜索 + 定时同步 | 📋 proposed | [proposal](stock/phase-1-search-sync/proposal.md) / [design](stock/phase-1-search-sync/design.md) / [tasks](stock/phase-1-search-sync/tasks.md) |
| financial | Phase 2 | 财报模块 | 📋 proposed | [proposal](financial/phase-2-financial/proposal.md) / [design](financial/phase-2-financial/design.md) / [tasks](financial/phase-2-financial/tasks.md) |
| announcement | Phase 3 | 公告模块 | 📋 proposed | [proposal](announcement/phase-3-announcement/proposal.md) / [design](announcement/phase-3-announcement/design.md) / [tasks](announcement/phase-3-announcement/tasks.md) |
| deployment | Phase 4 | Docker 部署 | 📋 proposed | [proposal](deployment/phase-4-docker/proposal.md) / [design](deployment/phase-4-docker/design.md) / [tasks](deployment/phase-4-docker/tasks.md) |

## 工作流

每个 Phase 走 **propose → apply → archive** 三阶段：

1. **proposed** — spec 文档已编写，待开始实施
2. **in-progress** — 正在实施 tasks.md 中的任务
3. **done** — 所有任务完成，待归档
4. **archived** — 已归档到历史记录

## 新建 Spec

1. 在对应模块下创建 `phase-N-<name>/` 目录
2. 编写 `proposal.md`（what & why）、`design.md`（how）、`tasks.md`（步骤）
3. 更新本索引文件

## 文件约定

| 文件 | 内容 | 何时改 |
|------|------|--------|
| `proposal.md` | 功能范围、动机、验收标准 | 需求变更时 |
| `design.md` | 技术方案、API 设计、架构决策 | 方案调整时 |
| `tasks.md` | 可执行任务清单（`- [ ]` / `- [x]`） | 每完成一步 |
