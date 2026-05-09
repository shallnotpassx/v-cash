## Why

投资者需要查看上市公司公告（年报、业绩预告、增减持等），这是投资决策的重要依据。Phase 0 已预留 IAnnouncementRepository 和 StockAnnouncement 实体骨架，需完整实现。

## What Changes

- 新增 AnnouncementDataAdapter 独立适配器接口（公告数据源与股票/财务不同）
- 实现 EastMoneyAnnouncementAdapter + CninfoAdapter（巨潮资讯网备选）
- 实现 AnnouncementRepository 仓储（按 stockId + title + publishDate 查重 upsert）
- 新增 AnnouncementDomainService 适配器链编排
- 新增 AnnouncementController REST 端点（list / refresh，支持 type 筛选）
- 新增 AnnouncementSyncTask 定时批量同步
- 前端新增公告列表页（类型筛选、原文链接跳转）

## Capabilities

### New Capabilities
- `announcement-data`: 公告数据查询——列表（按类型筛选）、原文链接跳转、手动刷新

### Modified Capabilities
- `stock-basic`: 股票详情页添加"查看公告"链接

## Impact

- 新增 AnnouncementDataAdapter / CninfoAdapter / EastMoneyAnnouncementAdapter
- 新增 AnnouncementRepository / AnnouncementDomainService / AnnouncementApplicationService / AnnouncementController
- 新增 AnnouncementSyncTask 定时任务
- 前端新增 AnnouncementList.vue 页面
- application.yml 新增 cron 配置
