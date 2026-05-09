## 1. 领域层

- [ ] 1.1 IStockRepository 新增 findByKeyword(String keyword) 方法
  - **验收**: 接口签名接受 `keyword: String`，返回 `List<StockBasic>`

## 2. 基础设施层

- [ ] 2.1 StockRepository 实现 findByKeyword：LambdaQueryWrapper OR LIKE（stockCode + stockName）
  - **验收**: `keyword="600000"` → 返回 stockCode 含 "600000" 的记录
  - **验收**: `keyword="银行"` → 返回 stockName 含 "银行" 的记录
  - **验收**: `keyword=""` → 返回全部记录（空关键字不添加 LIKE 条件）
  - **验收**: `keyword=null` → 返回全部记录
  - **验收**: 匹配无结果时返回空列表 `[]`

- [ ] 2.2 StockSyncTask 新增：注入 StockDomainService + IStockRepository，@Scheduled 全量同步
  - **验收**: cron 到达时自动触发全量 A+H 股票同步
  - **验收**: 上一次同步未完成时，本次跳过并记录 WARN 日志
  - **验收**: 单条失败时记录 ERROR 日志（含股票代码），继续下一条，不中断批量任务

## 3. 应用层

- [ ] 3.1 StockApplicationService.listFromDb 支持 keyword 参数
  - **验收**: `keyword` 非空时调用 `findByKeyword(keyword)`，空/null 时调用原 `findAll()`
  - **验收**: `keyword + market` 同时传递时，返回交集结果

## 4. 接口层

- [ ] 4.1 StockController.list() 新增 keyword 查询参数
  - **验收**: `GET /api/stock/list?keyword=600000` → HTTP 200，返回匹配结果
  - **验收**: `GET /api/stock/list`（不传 keyword）→ 行为不变，向后兼容
  - **验收**: `GET /api/stock/list?keyword=不存在` → HTTP 200，返回空列表 `[]`

- [ ] 4.2 VCashApplication 添加 @EnableScheduling
  - **验收**: 应用启动后 Spring 定时任务调度器处于激活状态

## 5. 配置

- [ ] 5.1 application.yml 新增 stock.sync.cron（默认 `0 0 3 * * ?`）
  - **验收**: 修改 cron 配置后重启应用，定时任务按新 cron 执行

## 6. 前端

- [ ] 6.1 StockList.vue 新增搜索输入框（el-input，300ms 防抖）
  - **验收**: 输入关键字后 300ms 内无新输入则发送请求
  - **验收**: 快速连续输入时仅发送最后一次请求
  - **验收**: 清空输入框时重新加载全部股票列表

- [ ] 6.2 搜索触发 getStockList({ keyword }) 重新请求
  - **验收**: 搜索请求携带 keyword 参数，返回结果更新列表
  - **验收**: 无匹配结果时列表显示空状态提示

## 7. 测试

- [ ] 7.1 StockRepository 关键字搜索单元测试
  - **验收**: 覆盖按代码搜索、按名称搜索、空关键字、无结果、组合 market 过滤共 5 个场景

- [ ] 7.2 StockController list 带 keyword 参数集成测试
  - **验收**: MockMvc 测试覆盖 keyword 有效匹配、keyword 无匹配、不传 keyword 兼容性共 3 个场景
