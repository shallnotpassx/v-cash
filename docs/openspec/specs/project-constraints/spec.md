# Project Constraints

跨 capability 通用约束，所有 capability spec 和变更均应遵守。

## Purpose

定义项目级架构、数据、行为约束，作为各 capability 的公共基线。任何 capability spec 不得违反此文档中的规定。

## Constraints

### Constraint: DDD 四层分层依赖

系统 SHALL 遵循 DDD 四层架构，各层依赖方向严格从上到下：

| 层 | 模块 | 可依赖 |
|----|------|--------|
| 接口层 | `v-cash-api` | 应用层 + 基础设施层 |
| 应用层 | `v-cash-application` | 领域层 |
| 领域层 | `v-cash-domain` | 无（不依赖任何其它层） |
| 基础设施层 | `v-cash-infrastructure` | 领域层 + 应用层 |

实体定义在领域层（`StockBasic`/`StockFinancial`/`StockAnnouncement`）。仓储接口定义在领域层，实现在基础设施层。领域服务处理跨聚合逻辑，应用服务编排用例。

#### Scenario: New repository follows DDD
- **WHEN** 新增持久化能力
- **THEN** 接口必须放在 `v-cash-domain` 的 repository 包下，实现放在 `v-cash-infrastructure` 的 repository 包下

#### Scenario: Controller depends only on application service
- **WHEN** Controller 需要调用业务逻辑
- **THEN** 仅注入 ApplicationService，不直接依赖 Repository 或 DomainService

### Constraint: 适配器策略 + 兜底模式

外部数据源接入 SHALL 使用适配器链模式：遍历已注册适配器列表，按配置顺序尝试，返回第一个成功结果。

同一领域概念使用同一适配器接口（如 `StockDataAdapter` 用于股票/财务），不同数据源领域使用独立接口（如 `AnnouncementDataAdapter` 用于公告）。

兜底策略：各数据源按优先级排列（见数据源对照表），当前适配器失败时自动降级到下一个。

#### Scenario: Primary adapter succeeds
- **WHEN** 第一个适配器返回有效数据
- **THEN** 采用该结果，不再调用后续适配器

#### Scenario: Primary adapter fails, fallback succeeds
- **WHEN** 第一个适配器抛出异常或返回空
- **THEN** 记录 WARN 日志，尝试下一个适配器，成功则采用

#### Scenario: All adapters fail
- **WHEN** 所有适配器均失败
- **THEN** 记录 ERROR 日志（含股票代码/市场参数），向上层抛领域异常

### Constraint: Upsert 持久化

保存操作 SHALL 使用 upsert 模式：存在则更新，不存在则插入。

各实体 upsert 唯一键定义：

| 实体 | 唯一键 |
|------|--------|
| `StockBasic` | `stockCode` |
| `StockFinancial` | `stockId + reportPeriod` |
| `StockAnnouncement` | `stockId + title + publishDate` |

#### Scenario: Insert new entity
- **WHEN** 持久化时唯一键在数据库中不存在
- **THEN** 执行 INSERT，自动填充 createTime/updateTime

#### Scenario: Update existing entity
- **WHEN** 持久化时唯一键已存在
- **THEN** 执行 UPDATE，更新变化字段和 updateTime，不改变 createTime

### Constraint: 逻辑删除

删除操作 SHALL 使用逻辑删除（`deleted` 字段），禁止物理删除。

- `deleted = 0`：正常
- `deleted = 1`：已删除
- 所有查询默认过滤 `deleted = 1` 的记录（MyBatis Plus 自动注入）
- `createTime` / `updateTime` 由 MyBatis Plus 自动填充

#### Scenario: Soft delete
- **WHEN** 删除一条股票记录
- **THEN** 执行 `UPDATE SET deleted = 1`，不删除数据库行

#### Scenario: Query excludes deleted
- **WHEN** 执行任意查询操作
- **THEN** 结果集自动排除 `deleted = 1` 的记录

### Constraint: 金融金额类型

所有金融数值（营收、净利润、ROE、PE、PB 等）SHALL 使用 `java.math.BigDecimal`，禁止使用 `float` 或 `double`。

数据库对应使用 `DECIMAL(20,2)`（金额类）或 `DECIMAL(10,4)`（比率类）。

#### Scenario: Financial calculation precision
- **WHEN** 进行营收/净利润等金额计算
- **THEN** 使用 BigDecimal 运算，不产生浮点精度损失

### Constraint: 定时调度

定时任务 SHALL 使用 Spring `@Scheduled` 注解，cron 表达式通过 `application.yml` 配置。

- 调度行为：轻量级，不引入 Quartz 等外部调度框架
- 并发控制：默认禁止并发执行（同一任务上一次未完成时跳过）
- 失败处理：单条失败不中断批量任务，记录日志后继续
- 幂等性：重复执行不产生副作用（upsert 模式天然保障）

#### Scenario: Cron configured in application.yml
- **WHEN** 需要修改同步频率
- **THEN** 修改 `application.yml` 中对应 cron 配置项，重启生效

#### Scenario: Concurrent execution prevention
- **WHEN** 上一次同步未完成且下次触发时间到达
- **THEN** 跳过本次触发，记录 WARN 日志

#### Scenario: Single record failure in batch
- **WHEN** 批量同步中某条记录失败
- **THEN** 记录 ERROR 日志（含标识信息），继续处理下一条，不中断批量任务

### Constraint: REST API 规范

所有 REST 接口 SHALL 遵循以下约定：

- 基础路径：`/api/`
- 路径变量使用 stockCode（非 stockId）
- 成功返回 HTTP 200 + JSON 体
- 资源不存在返回 HTTP 404
- 服务端端口：8089

### Constraint: 测试

测试 SHALL 使用 JUnit 5 + Mockito，测试类放在对应模块的 `src/test/java/` 下。测试用例文档维护在 `docs/TEST_CASES.md`。

## 数据源对照表

| 功能 | 首选 | 备选 |
|------|------|------|
| A 股财务 | Tushare Pro | 东方财富 |
| 港股财务 | 东方财富 | Tushare Pro |
| A 股公告 | 巨潮资讯网 | 东方财富 |
| 港股公告 | 东方财富 | — |
