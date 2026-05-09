## Context

Phase 0 已预留 IAnnouncementRepository 接口和 StockAnnouncement 实体。公告数据源（巨潮资讯网/东方财富）与股票/财务不同，需独立适配器。

## Goals / Non-Goals

**Goals:**
- 新建 AnnouncementDataAdapter 独立适配器接口
- 实现 EastMoneyAnnouncementAdapter + CninfoAdapter（备选）
- 实现公告仓储、领域服务、应用服务、控制器
- 前端公告列表页（类型筛选、原文链接）

**Non-Goals:**
- 不做公告全文搜索
- 不存储公告 HTML 内容，仅存储标题和原文链接

## Decisions

**独立适配器接口**
公告数据源与股票不同，使用独立 AnnouncementDataAdapter 接口。
替代方案（复用 StockDataAdapter）被拒绝：接口语义不匹配。

**upsert 键为 stockId + title + publishDate**
公告无业务 ID，用内容标题+发布日期作为唯一性判断。
替代方案（仅用 title）被拒绝：同一公司可能有同名公告。

## Risks / Trade-offs

| 风险 | 应对 |
|------|------|
| 巨潮资讯网 API 无公开文档 | 优先东方财富，巨潮作为备选 |
| 公告数据量大 | 仅获取最近 100 条 |
| 港股公告编码问题 | 确认 API 编码为 UTF-8 |
