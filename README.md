# v-cash

v-cash 是一个面向个人使用场景的股票数据查询与筛选项目，目标是把 A 股和港股的基础资料、财务数据、公告信息统一到本地，支持后续按财务指标筛选股票并查看个股历史明细。

## 当前状态

当前仓库处于**规划和骨架阶段**。

现在已经明确的是项目方向、能力边界、OpenSpec change 结构和基础目录骨架；业务功能还没有进入正式实现阶段。

## 这个项目要解决什么问题

这个项目不是量化交易系统，也不是全市场高频分析平台。

它主要解决下面几件事：

- 把你关心的 A 股 / 港股公司数据拉到本地
- 统一保存股票、财务和公告数据
- 支持按多个财务指标组合筛选股票
- 支持点进个股查看历史财务和公告
- 当结构化财务数据拿不到时，允许用公告或年报 PDF 做人工辅助提取

## 项目怎么工作

项目默认采用“本地优先”的方式：

- 查询默认只查本地库
- 本地没有时，不自动回源
- 由用户手动触发“按指定代码拉取”
- 手动同步是主链路
- 定时同步只是辅助能力

## 计划中的核心能力

### 1. 股票主数据

- 支持 A 股 / 港股股票身份统一管理
- 使用 `market + stock_code` 作为内部主标识
- 支持本地股票列表、详情和基础搜索

### 2. 数据拉取与同步

- 接入免费优先的数据源
- 按指定代码手动拉取数据
- 支持批量刷新和辅助性定时同步

### 3. 财务筛选

- 保存历史财务数据
- 保存最新财务筛选快照
- 支持多指标组合筛选
- 支持从个股详情查看历史财务

### 4. 公告中心

- 支持 A 股 / 港股公告查询
- 支持公告列表、类型筛选、原文链接
- 给年报和公告文档解析提供来源

### 5. 部署

- 前期以本地 Windows 机器为主
- 使用 Docker Compose 启动前端、后端和 MySQL
- 后续可以迁移到 NAS 或 VPS

## 数据源策略

当前规划的优先级如下：

- A 股 / 港股基础资料与财务数据：优先东方财富
- A 股公告：优先巨潮资讯
- 港股公告：优先 HKEXnews
- Tushare 作为后续增强源，不是一期开局必须依赖的主源

## 存储策略

主存储计划使用 MySQL。

财务数据会按用途拆开：

- 股票主表
- 财务历史表
- 财务筛选快照表

这样可以保证：

- 多指标筛选直接查快照表
- 个股详情再查历史表

## 文档解析兜底

当结构化财务数据拿不到时，项目允许使用公告或年报 PDF 做财务指标提取。

这条链路只作为**人工辅助**，不会直接自动写入正式财务结果。

## 当前 OpenSpec 结构

项目的可执行 spec 只保留在 `docs/openspec/changes/` 下。

当前规划的能力域有：

- `stock-registry`
- `data-ingestion-sync`
- `financial-screening`
- `announcement-center`
- `deployment`

每个 change 目录当前都已经补齐：

- `proposal.md`
- `design.md`
- `tasks.md`
- `specs/<capability>/spec.md`

只有真正开始实现某个 task 时，才会继续补 `PLAN.md` 和 `review/`。

## 预期部署方式

项目最终仍然是一个前后端分离的单体系统：

- 一个 Spring Boot 后端应用
- 一个 Vue 3 前端应用
- 一个 MySQL 实例

Docker Compose 负责把它们组织起来。

## 当前目录骨架

```text
v-cash/
├── .agents/
├── backend/
├── deployment/
├── docs/openspec/changes/
├── frontend/
├── sql/
├── AGENTS.md
├── LICENSE
└── README.md
```

## 本地体验方式

### 前端骨架

```bash
cd frontend
npm install
npm run dev
```

默认地址：`http://localhost:5173`

### 后端骨架

```bash
mvn -pl backend spring-boot:run
```

默认地址：`http://localhost:8089/api/system/overview`

如果你在 IDEA 里直接打开仓库根目录，现在也可以把根目录识别为 Maven 项目；仓库根目录新增了聚合 `pom.xml`，统一把 `backend` 纳入 Maven 管理。

### Docker Compose

```bash
cd deployment
cp .env.example .env
docker compose up --build
```
