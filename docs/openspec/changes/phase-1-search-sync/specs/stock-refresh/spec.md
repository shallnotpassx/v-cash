## MODIFIED Requirements

### Requirement: Scheduled automatic sync
系统 SHALL 在现有手动刷新基础上新增 `@Scheduled` 定时自动同步能力。

#### Scenario: Scheduled trigger at configured cron
- **WHEN** 到达 `application.yml` 中配置的 cron 时间
- **THEN** 系统自动执行全量股票数据同步（仅同步 A+H 市场）

#### Scenario: Manual refresh still works
- **WHEN** 用户请求 `POST /api/stock/refresh`
- **THEN** 系统从适配器链同步数据，行为与修改前一致

#### Scenario: Sync failure does not affect application
- **WHEN** 定时同步执行中任意适配器失败
- **THEN** 系统记录错误日志（含股票代码、错误信息），继续处理下一条，不影响应用健康

#### Scenario: Concurrent execution prevention
- **WHEN** 上一次定时同步尚未完成且下次触发时间到达
- **THEN** 系统跳过本次触发，记录 WARN 日志

#### Scenario: Cron expression configurable
- **WHEN** 运维人员修改 `application.yml` 中的 `stock.sync.cron` 配置
- **THEN** 系统在重启后按新 cron 表达式执行
