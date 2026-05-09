## Purpose

提供上市公司公告数据查询能力，支持按类型筛选、原文链接跳转、手动刷新和定时批量同步。

## Requirements

### Requirement: List announcements by stock
系统 SHALL 支持查询指定股票的公告列表，按发布日期降序排列。

#### Scenario: List announcements
- **WHEN** 用户请求 `GET /api/announcement/600000`
- **THEN** 系统返回该股票的公告列表（title/type/publishDate/sourceUrl）

### Requirement: Filter announcements by type
系统 SHALL 支持按公告类型筛选（年报/半年报/季报/业绩预告/增减持等）。

#### Scenario: Filter by type
- **WHEN** 用户请求 `GET /api/announcement/600000?type=年报`
- **THEN** 系统仅返回类型为"年报"的公告

### Requirement: Announcement source link
系统 SHALL 在每条公告中提供原文链接 sourceUrl。

#### Scenario: Open source link
- **WHEN** 用户点击公告的原文链接
- **THEN** 前端在新标签页打开 sourceUrl

### Requirement: Refresh announcements
系统 SHALL 支持手动触发指定股票的公告同步。

#### Scenario: Refresh announcements
- **WHEN** 用户请求 `POST /api/announcement/600000/refresh`
- **THEN** 系统从适配器拉取公告数据、绑定 stockId、upsert 到数据库

### Requirement: Batch sync announcements
系统 SHALL 支持定时批量同步所有股票的公告数据。

#### Scenario: Scheduled batch sync
- **WHEN** 到达配置的 cron 时间
- **THEN** 系统遍历所有股票逐条同步其公告数据
