# Phase 3: 公告模块开发

## 概述

实现公告数据模块的完整 DDD 四层链路：新增 `AnnouncementDataAdapter` 接口及适配器实现、仓储实现、领域服务、应用服务、REST 接口、前端页面。

## 动机

投资者需要查看上市公司公告（年报、业绩预告、增减持等），这是投资决策的重要依据。目前的 `IAnnouncementRepository` 和 `StockAnnouncement` 实体只是骨架，需要完整实现。

## 功能范围

| 编号 | 功能 | 描述 | 优先级 |
|------|------|------|--------|
| F7 | 查看公告列表 | `GET /api/announcement/{stockCode}` 返回公告列表 | P0 |
| F8 | 按类型筛选公告 | `GET /api/announcement/{stockCode}?type=年报` 按类型过滤 | P0 |
| F9 | 查看公告原文 | 每条公告携带 `sourceUrl`，前端点击跳转 | P1 |
| F10 | 手动刷新公告 | `POST /api/announcement/{stockCode}/refresh` 从数据源拉取 | P0 |
| F11 | 定时同步公告 | `AnnouncementSyncTask @Scheduled` 批量同步 | P1 |

## 验收标准

- [ ] `GET /api/announcement/{stockCode}` 返回按发布日期降序的公告列表
- [ ] `GET /api/announcement/{stockCode}?type=年报` 仅返回指定类型的公告
- [ ] 公告列表每条可见 title、type、publishDate、sourceUrl
- [ ] 前端点击 sourceUrl 在新标签页打开原文
- [ ] `POST /api/announcement/{stockCode}/refresh` 从适配器拉取并 upsert
- [ ] 定时任务每日自动同步公告数据
