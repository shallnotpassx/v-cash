# Phase 3: 任务清单

## 领域层
- [ ] AnnouncementDataAdapter 接口：`getSourceName()`、`getAnnouncements(String stockCode)`
- [ ] AnnouncementDomainService：注入 `List<AnnouncementDataAdapter>`，适配器链编排，stockId 绑定

## 基础设施层
- [ ] AnnouncementMapper：MyBatis Plus `BaseMapper<StockAnnouncement>`
- [ ] AnnouncementRepository：实现 IAnnouncementRepository
  - [ ] `findByStockId(Long stockId)`：按 stockId + publishDate 降序
  - [ ] `findByStockId(Long stockId, String type)`：额外按 type 过滤
  - [ ] `save`：按 stockId + title + publishDate 查重 upsert
  - [ ] `saveAll`：批量 upsert
- [ ] EastMoneyAnnouncementAdapter：东方财富公告 API 适配
- [ ] CninfoAdapter：巨潮资讯网适配器（备选，可后续实现）
- [ ] AnnouncementSyncTask：遍历股票逐条同步公告

## 应用层
- [ ] AnnouncementApplicationService
  - [ ] `listByStockCode(stockCode, type)`：查 stockId → 查公告列表
  - [ ] `refreshByStockCode(stockCode)`：查 stockId → 拉取 → 绑定 → 保存

## 接口层
- [ ] AnnouncementController
  - [ ] `GET /api/announcement/{stockCode}` → 公告列表（支持 type 参数）
  - [ ] `POST /api/announcement/{stockCode}/refresh` → 刷新

## 配置
- [ ] application.yml 新增 `announcement.sync.cron` 配置项

## 前端
- [ ] api/index.js 新增 `getAnnouncements(code, type)`、`refreshAnnouncements(code)`
- [ ] router/index.js 新增 `/stocks/:code/announcements` 路由
- [ ] AnnouncementList.vue：
  - [ ] el-table 展示 title/type/publishDate/sourceUrl
  - [ ] 类型筛选下拉框（el-select）
  - [ ] sourceUrl 点击新标签页打开
  - [ ] 刷新按钮
  - [ ] 返回股票详情链接
- [ ] StockDetail.vue 添加"查看公告"链接

## 测试
- [ ] AnnouncementRepository 单元测试
- [ ] AnnouncementDomainService 单元测试
- [ ] AnnouncementController 集成测试
