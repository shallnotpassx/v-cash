## ADDED Requirements

### Requirement: Scheduled stock sync
系统 SHALL 支持定时自动同步股票数据。

#### Scenario: Scheduled task execution
- **WHEN** 到达配置的 cron 时间
- **THEN** 系统自动执行全量股票数据同步

#### Scenario: Sync failure handling
- **WHEN** 定时同步执行失败
- **THEN** 系统记录错误日志，不影响应用正常运行
