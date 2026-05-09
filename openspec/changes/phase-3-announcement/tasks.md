## 1. 领域层

- [ ] 1.1 AnnouncementDataAdapter 接口（getSourceName / getAnnouncements）
- [ ] 1.2 AnnouncementDomainService：适配器链编排 + stockId 绑定

## 2. 基础设施层

- [ ] 2.1 AnnouncementMapper（MyBatis Plus BaseMapper）
- [ ] 2.2 AnnouncementRepository（findByStockId / findByStockId+type / save / saveAll）
- [ ] 2.3 EastMoneyAnnouncementAdapter
- [ ] 2.4 CninfoAdapter（备选，可后续实现）
- [ ] 2.5 AnnouncementSyncTask（遍历股票逐条同步）

## 3. 应用层

- [ ] 3.1 AnnouncementApplicationService（listByStockCode / refreshByStockCode）

## 4. 接口层

- [ ] 4.1 AnnouncementController（GET list + POST refresh）

## 5. 配置

- [ ] 5.1 application.yml 新增 announcement.sync.cron

## 6. 前端

- [ ] 6.1 api/index.js 新增 announcement API
- [ ] 6.2 router/index.js 新增 /stocks/:code/announcements 路由
- [ ] 6.3 AnnouncementList.vue（el-table + 类型筛选 + sourceUrl 跳转 + 刷新按钮）
- [ ] 6.4 StockDetail.vue 添加"查看公告"链接

## 7. 测试

- [ ] 7.1 AnnouncementRepository 单元测试
- [ ] 7.2 AnnouncementDomainService 单元测试
- [ ] 7.3 AnnouncementController 集成测试
