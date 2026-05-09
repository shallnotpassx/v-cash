## Purpose

提供上市公司公告数据查询能力，支持按类型筛选、原文链接跳转、手动刷新和定时批量同步。

## Requirements

### Requirement: List announcements by stock
系统 SHALL 支持查询指定股票的公告列表，按发布日期降序排列。

#### Scenario: List announcements
- **WHEN** 用户请求 `GET /api/announcement/600000`
- **THEN** 系统返回该股票的公告列表（title/type/publishDate/sourceUrl）

#### Scenario: Stock not found
- **WHEN** 用户请求 `GET /api/announcement/NONEXIST`
- **THEN** 系统返回 404

#### Scenario: No announcements for existing stock
- **WHEN** 用户请求 `GET /api/announcement/600000` 但该股票无公告记录
- **THEN** 系统返回空列表 `[]`，HTTP 200

### Requirement: Filter announcements by type
系统 SHALL 支持按公告类型筛选（年报/半年报/季报/业绩预告/增减持等）。

#### Scenario: Filter by type
- **WHEN** 用户请求 `GET /api/announcement/600000?type=年报`
- **THEN** 系统仅返回 announcementType 为"年报"的公告

#### Scenario: Type value not found
- **WHEN** 用户请求 `GET /api/announcement/600000?type=不存在类型`
- **THEN** 系统返回空列表 `[]`，HTTP 200

#### Scenario: Dedup on same announcement
- **WHEN** 同一 stockId + title + publishDate 的公告再次同步入库
- **THEN** 系统更新已有记录（upsert），不产生重复行

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

#### Scenario: Stock not found during refresh
- **WHEN** 用户请求 `POST /api/announcement/NONEXIST/refresh`
- **THEN** 系统返回 404

#### Scenario: Adapter returns no announcements
- **WHEN** 适配器对某股票无公告数据
- **THEN** 系统记录 INFO 日志，返回成功但不插入记录

### Requirement: Batch sync announcements
系统 SHALL 支持定时批量同步所有股票的公告数据。

#### Scenario: Scheduled batch sync
- **WHEN** 到达配置的 cron 时间
- **THEN** 系统遍历所有股票逐条同步其公告数据

#### Scenario: Single stock failure does not interrupt batch
- **WHEN** 批量同步中某只股票的公告适配器全部失败
- **THEN** 系统记录 ERROR 日志（含 stockId），继续处理下一条

#### Scenario: Batch sync idempotent
- **WHEN** 连续两次执行批量同步
- **THEN** 第二次不产生重复数据（按 stockId+title+publishDate upsert 保障）
