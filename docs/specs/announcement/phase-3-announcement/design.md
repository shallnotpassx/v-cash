# Phase 3: 公告模块 — 技术设计

## 架构决策

### 独立适配器接口

公告数据源与股票/财务数据源不同（巨潮资讯网 vs Tushare/东方财富），使用独立的 `AnnouncementDataAdapter` 接口，而非复用 `StockDataAdapter`。

```java
public interface AnnouncementDataAdapter {
    String getSourceName();
    List<StockAnnouncement> getAnnouncements(String stockCode);
}
```

### 适配器实现

| 适配器 | 目标市场 | 数据源 |
|--------|----------|--------|
| CninfoAdapter | A股 | 巨潮资讯网 API |
| EastMoneyAnnouncementAdapter | A股/港股 | 东方财富公告 API |

### 类型筛选

- 服务端过滤：`announcementType` 字段在数据库层做 `eq` 查询
- 前端下拉框提供常见类型选项（年报、半年报、季报、业绩预告、增减持、其他）

## 数据流

```
GET /api/announcement/{stockCode}?type=年报
  → AnnouncementController.list(stockCode, type)
    → AnnouncementApplicationService.listByStockCode(stockCode, type)
      → IStockRepository.findByCode(stockCode)  ← 获取 stockId
      → IAnnouncementRepository.findByStockId(stockId, type)
  ← 返回 List<StockAnnouncement>
```

刷新：
```
POST /api/announcement/{stockCode}/refresh
  → AnnouncementController.refresh(stockCode)
    → AnnouncementApplicationService.refreshByStockCode(stockCode)
      → IStockRepository.findByCode(stockCode)  ← 获取 stockId
      → AnnouncementDomainService.fetchAnnouncements(stockCode)
      → 绑定 stockId
      → IAnnouncementRepository.saveAll(announcements)
```

## API 设计

| 方法 | 路径 | 请求参数 | 响应 |
|------|------|----------|------|
| GET | `/api/announcement/{stockCode}` | `stockCode` (路径), `type` (可选) | `List<StockAnnouncement>` |
| POST | `/api/announcement/{stockCode}/refresh` | `stockCode` (路径) | `"ok"` |

## 数据库变更

`stock_announcement` 表中 `title` 字段当前为 `VARCHAR(500)`，中文公告标题较长，需确认是否足够。

## 涉及文件

| 文件 | 变更 | 说明 |
|------|------|------|
| `AnnouncementDataAdapter.java` | 新增 | 公告适配器接口（domain 层） |
| `CninfoAdapter.java` | 新增 | 巨潮资讯网适配器 |
| `EastMoneyAnnouncementAdapter.java` | 新增 | 东方财富公告适配器 |
| `AnnouncementMapper.java` | 新增 | MyBatis Plus BaseMapper |
| `AnnouncementRepository.java` | 新增 | 实现 IAnnouncementRepository |
| `AnnouncementDomainService.java` | 新增 | 适配器链编排 + stockId 绑定 |
| `AnnouncementApplicationService.java` | 新增 | 用例编排 |
| `AnnouncementController.java` | 新增 | REST 端点 |
| `AnnouncementSyncTask.java` | 新增 | 定时批量同步 |
| `AnnouncementList.vue` | 新增 | 公告列表页 |
| `api/index.js` | 修改 | 新增 announcement API |
| `router/index.js` | 修改 | 新增路由 |
| `application.yml` | 修改 | 新增 cron 配置 |

## 风险与对策

| 风险 | 影响 | 应对 |
|------|------|------|
| 巨潮资讯网 API 无公开文档 | 适配器实现困难 | 优先东方财富，巨潮作为备选 |
| 公告数据量大 | 刷新耗时长 | 仅获取最近 N 条（如 100 条） |
| 港股公告中文编码 | 乱码 | 确认东方财富 API 编码为 UTF-8 |
