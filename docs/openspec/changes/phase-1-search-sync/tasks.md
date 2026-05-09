## 1. 领域层

- [ ] 1.1 IStockRepository 新增 findByKeyword(String keyword) 方法

## 2. 基础设施层

- [ ] 2.1 StockRepository 实现 findByKeyword：LambdaQueryWrapper OR LIKE（stockCode + stockName）
- [ ] 2.2 StockSyncTask 新增：注入 StockDomainService + IStockRepository，@Scheduled 全量同步

## 3. 应用层

- [ ] 3.1 StockApplicationService.listFromDb 支持 keyword 参数

## 4. 接口层

- [ ] 4.1 StockController.list() 新增 keyword 查询参数
- [ ] 4.2 VCashApplication 添加 @EnableScheduling

## 5. 配置

- [ ] 5.1 application.yml 新增 stock.sync.cron（默认 0 0 3 * * ?）

## 6. 前端

- [ ] 6.1 StockList.vue 新增搜索输入框（el-input，300ms 防抖）
- [ ] 6.2 搜索触发 getStockList({ keyword }) 重新请求

## 7. 测试

- [ ] 7.1 StockRepository 关键字搜索单元测试
- [ ] 7.2 StockController list 带 keyword 参数集成测试
