## Purpose

支持定时自动从数据源同步股票列表，cron 表达式可配置，同步失败不影响应用正常运行。

## Requirements

### Requirement: Scheduled stock sync
系统 SHALL 支持定时自动同步股票数据，cron 表达式可配置。

#### Scenario: Scheduled task execution
- **WHEN** 到达配置的 cron 时间
- **THEN** 系统自动执行全量股票数据同步

#### Scenario: Sync failure does not affect application
- **WHEN** 定时同步执行失败（适配器链全部失败）
- **THEN** 系统记录 ERROR 日志，不影响应用正常运行

#### Scenario: Concurrent execution prevention
- **WHEN** 上一次定时同步尚未完成且下次触发时间到达
- **THEN** 系统跳过本次触发，记录 WARN 日志

#### Scenario: Per-record error handling in batch
- **WHEN** 批量同步中某条记录失败（如某只股票适配器抛异常）
- **THEN** 系统记录 ERROR 日志（含股票代码和异常信息），继续处理下一条，不中断整个批量任务
