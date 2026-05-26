# v-cash AGENTS.md

## 项目定位

这是一个面向个人使用场景的 A 股 / 港股数据查询与筛选项目。

当前项目仍处于规划和骨架阶段，默认以本地优先的方式推进：

- 默认只查本地库
- 本地没有数据时，不自动回源
- 通过“按指定代码拉取”显式触发同步

## 仓库结构

```text
v-cash/
├── backend/               # 单 Spring Boot 工程，按领域上下文做 DDD 分层
├── frontend/              # Vue 3 + Element Plus 前端
├── deployment/            # Docker Compose、Dockerfile、Nginx 配置
├── docs/openspec/changes/ # OpenSpec change 工件
├── sql/                   # 数据库初始化脚本
├── README.md              # 给人看的项目介绍
└── AGENTS.md              # 给开发和 agent 看的规则
```

## 后端架构规则

后端必须遵循 DDD，但不再拆成多个 Maven 模块。

- 后端是一个 Spring Boot 应用
- 先按领域上下文拆分，再在上下文内部做 DDD 分层
- 当前上下文固定为：
  - `shared`
  - `stockregistry`
  - `ingestion`
  - `financial`
  - `announcement`

每个上下文内部使用以下分层：

- `domain/`
- `application/`
- `infrastructure/`
- `web/`

### 依赖方向

- `domain` 不依赖其它层
- `application` 只依赖 `domain`
- `infrastructure` 实现外部适配和持久化，依赖 `domain` / `application`
- `web` 负责 HTTP 接口，调用 `application`

## 项目级业务约束

### 股票标识

- 股票主标识采用 `market + stock_code`
- 不直接把用户原始输入当作主键
- 需要支持 A 股和港股

### 数据源策略

- 免费优先
- A 股 / 港股基础资料与财务数据：优先考虑东方财富
- A 股公告：优先巨潮资讯
- 港股公告：优先 HKEXnews
- Tushare 作为后续增强源，不是一期主依赖

### 数据拉取策略

- 查询默认只查本地库
- 本地缺失时不自动回源
- 由用户手动触发“按指定代码拉取”
- 手动同步是主链路，定时同步只是辅助能力

### 存储策略

- 主存储使用 MySQL
- 财务数据至少拆成：
  - 股票主表
  - 财务历史表
  - 财务筛选快照表
- 多指标筛选优先走快照表
- 详情页历史财务走历史表

### 文档解析兜底

- 当结构化财务数据拉取失败时，可以用公告或年报 PDF 解析关键指标
- 解析结果只作为人工辅助
- 不直接自动写入正式财务快照
- 默认只保存文档 URL 和元信息，不做本地缓存

## OpenSpec 规则

OpenSpec 只维护 `docs/openspec/changes/` 下的 change 工件。

不再保留根级 `docs/openspec/specs/`。

每个 change 目录至少包含：

- `.openspec.yaml`
- `README.md`
- `proposal.md`
- `design.md`
- `tasks.md`
- `specs/`

只有真正进入执行阶段时，才新增：

- `PLAN.md`
- `review/RNN.md`

## 当前能力域

- `stock-registry`
- `data-ingestion-sync`
- `financial-screening`
- `announcement-center`
- `deployment`

依赖关系：

- `stock-registry` 是根能力域
- `data-ingestion-sync` 依赖 `stock-registry`
- `financial-screening` 依赖 `stock-registry` 和 `data-ingestion-sync`
- `announcement-center` 只依赖 `stock-registry`
- `deployment` 负责交付，不反向影响业务边界

## 文档更新规则

变更项目规则时更新 `AGENTS.md`。

变更产品定位、能力说明或路线图时更新 `README.md`。

变更某个能力域的需求、方案或任务时，只更新对应 `docs/openspec/changes/<name>/`。
