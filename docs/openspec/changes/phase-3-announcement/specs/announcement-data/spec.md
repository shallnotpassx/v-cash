## ADDED Requirements

### Requirement: List announcements by stock
系统 SHALL 支持查询指定股票的公告列表。

#### Scenario: List announcements
- **WHEN** 用户请求 `GET /api/announcement/600000`
- **THEN** 系统返回公告列表（title/type/publishDate/sourceUrl）

### Requirement: Filter by type
系统 SHALL 支持按公告类型筛选。

#### Scenario: Filter by type
- **WHEN** 用户请求 `GET /api/announcement/600000?type=年报`
- **THEN** 系统仅返回类型为"年报"的公告

### Requirement: Source link
系统 SHALL 在每条公告中提供原文链接。

#### Scenario: Open source link
- **WHEN** 用户点击公告原文链接
- **THEN** 前端在新标签页打开 sourceUrl

### Requirement: Refresh announcements
系统 SHALL 支持手动触发公告同步。

#### Scenario: Refresh
- **WHEN** 用户请求 `POST /api/announcement/600000/refresh`
- **THEN** 系统从适配器拉取公告数据并 upsert

### Requirement: Batch sync
系统 SHALL 支持定时批量同步所有股票的公告数据。

#### Scenario: Scheduled batch sync
- **WHEN** 到达配置的 cron 时间
- **THEN** 系统遍历所有股票逐条同步公告数据
