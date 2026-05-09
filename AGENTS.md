# v-cash AGENTS.md

## 项目概述

股票数据查询与分析系统，获取A股/港股财务数据及公告信息。

- **后端**: Spring Boot 3.2.5, JDK 17, MyBatis Plus 3.5.5, MySQL 8.0
- **前端**: Vue 3 + Element Plus
- **构建**: Maven 多模块
- **数据源**: Tushare Pro、东方财富 API

## 项目结构

```
v-cash/
├── v-cash-domain/          # 领域层 - 实体、仓储接口、领域服务
├── v-cash-application/     # 应用层 - 用例编排、应用服务
├── v-cash-infrastructure/  # 基础设施层 - 适配器、持久化、配置
├── v-cash-api/             # 接口层 - REST 控制器、启动类
├── vcash-web/              # 前端
├── sql/init.sql            # 数据库初始化脚本
└── docs/
    └── openspec/           # OpenSpec 规范 - spec 定义 + 变更追踪
```

## DDD 分层规则

- **领域层** 不依赖任何其它层
- **应用层** 只依赖领域层
- **基础设施层** 依赖领域层 + 应用层
- **接口层** 依赖应用层 + 基础设施层

关键概念：
- 实体有唯一标识（StockBasic、StockFinancial、StockAnnouncement）
- 仓储接口定义在领域层，实现在基础设施层
- 领域服务处理跨聚合逻辑，应用服务编排用例

## 开发规范

- 代码风格：阿里巴巴 Java 开发规约
- Git 提交：Conventional Commits 格式（feat/fix/docs/chore 等）
- 新代码写入 DDD 多模块，不再修改 `src/` Legacy 代码

## 任务完成检查清单

每完成一个 OpenSpec 任务（tasks.md 中的一项），必须同步更新以下文件：

| 文件 | 何时更新 |
|------|----------|
| `docs/openspec/changes/<name>/tasks.md` | 始终：标记 `- [ ]` → `- [x]` |
| `README.md` | 新增 API / 表结构 / 数据源 / 技术栈变更时 |
| `AGENTS.md` | 新增约定 / 配置 / 开发规范变更时 |
| `docs/PLAN.md` | 完成一个 Phase 时更新进度状态 |
| `docs/openspec/` 相关 artifacts | 需求/设计/任务变更时 |

## 数据库

- 用户：`gongbotao` / `123456`（禁止使用 root）
- 数据库：`vcash`
- 表：`stock_basic`、`stock_financial`、`stock_announcement`
- 逻辑删除：`deleted` 字段（0=正常，1=删除）
- 自动填充：`createTime`、`updateTime`

## 测试

- 框架：JUnit 5 + Mockito
- 测试用例文档：`TEST_CASES.md`
- 现有测试：`src/test/java/` 下 StockServiceTest、FinancialServiceTest
- 新测试应写在对应模块的 `src/test/java/` 目录下

## OpenSpec 规范驱动开发

规范文件位于 `docs/openspec/`，每个变更走 **propose → apply → archive** 三阶段：

| 阶段 | 命令 | 产出 |
|------|------|------|
| Propose | `openspec new change <name>` | proposal.md + specs/ + design.md + tasks.md |
| Apply | 按 tasks.md 逐项实现 | 实现代码 + 标记任务完成 |
| Archive | `openspec archive <name>` | 归档到 `changes/archive/` |

### 工作流

1. **创建变更**：`npx openspec new change <kebab-case-name> --description "..."`（在工作区目录下执行时指定 `docs/openspec` 路径）
2. **编写 artifact**：按模板完成 proposal → specs → design → tasks
3. **实现**：按 tasks.md 逐项实现，每完成一项标记 `- [x]`
4. **归档**：实现完成后执行 archive

### 文件约定

| 文件 | 内容 | 说明 |
|------|------|------|
| `proposal.md` | 动机、变更范围、能力列表 | 需求变更时更新 |
| `specs/<capability>/spec.md` | 详细需求（SHALL/MUST + 场景） | 新能力/需求变更时更新 |
| `design.md` | 技术方案、架构决策、风险 | 方案调整时更新 |
| `tasks.md` | 可执行任务清单（`- [ ]`） | 每完成一项标记 |

## 关键约定

- 适配器采用策略+兜底模式：按顺序尝试，返回第一个成功结果
- 保存采用 upsert 模式：存在则更新，不存在则插入
- 金融金额使用 `BigDecimal`
- 端口：8089
