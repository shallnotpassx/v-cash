# v-cash 测试报告

## 测试概览

| 指标 | 数值 |
|------|------|
| 测试用例总数 | 21 |
| 通过 | 21 |
| 失败 | 0 |
| 跳过 | 0 |
| 执行时间 | 0.591s |

## 测试详情

### 1. StockServiceTest (股票服务)

| 序号 | 测试方法 | 状态 | 说明 |
|------|----------|------|------|
| 1 | `listFromDb_withMarketFilter` | ✅ PASS | 测试按市场筛选查询股票列表 |
| 2 | `listFromDb_noMarketFilter` | ✅ PASS | 测试无筛选条件查询股票列表 |
| 3 | `getByCode_found` | ✅ PASS | 测试查询存在的股票 |
| 4 | `getByCode_notFound` | ✅ PASS | 测试查询不存在的股票 |
| 5 | `fetchFromSource_success` | ✅ PASS | 测试从数据源成功获取股票列表 |
| 6 | `fetchFromSource_firstAdapterFail_trySecond` | ✅ PASS | 测试第一适配器失败后切换到第二适配器 |
| 7 | `fetchFromSource_allAdapterFail` | ✅ PASS | 测试所有适配器都失败时返回空列表 |
| 8 | `saveToDb_insertNew` | ✅ PASS | 测试插入新股票 |
| 9 | `saveToDb_updateExist` | ✅ PASS | 测试更新已存在的股票 |
| 10 | `refreshStockList_success` | ✅ PASS | 测试刷新股票列表完整流程 |

### 2. FinancialServiceTest (财务数据服务)

| 序号 | 测试方法 | 状态 | 说明 |
|------|----------|------|------|
| 1 | `getByStockCode_stockNotFound` | ✅ PASS | 测试查询不存在的股票财务数据 |
| 2 | `getByStockCode_success` | ✅ PASS | 测试查询股票财务数据 |
| 3 | `getLatestByStockCode_found` | ✅ PASS | 测试获取最新财务数据（存在） |
| 4 | `getLatestByStockCode_notFound` | ✅ PASS | 测试获取最新财务数据（不存在） |
| 5 | `fetchFromSource_success` | ✅ PASS | 测试从数据源成功获取财务数据 |
| 6 | `fetchFromSource_firstAdapterFail_trySecond` | ✅ PASS | 测试适配器失败切换 |
| 7 | `fetchFromSource_allAdapterFail` | ✅ PASS | 测试所有适配器都失败 |
| 8 | `saveToDb_stockNotFound` | ✅ PASS | 测试股票不存在时不保存财务数据 |
| 9 | `saveToDb_insertNew` | ✅ PASS | 测试插入新财务数据 |
| 10 | `saveToDb_updateExist` | ✅ PASS | 测试更新已存在财务数据 |
| 11 | `refreshFinancialData_success` | ✅ PASS | 测试刷新财务数据完整流程 |

## 测试覆盖率

### 已测试的场景

- **数据查询**: 从数据库查询股票列表、详情、财务数据
- **数据获取**: 从外部数据源获取数据（适配器模式）
- **故障转移**: 主数据源失败时自动切换到备用数据源
- **数据保存**: 新增和更新股票、财务数据
- **异常处理**: 数据源请求异常处理

### 未覆盖的场景

- 公告服务相关功能（Phase 3）
- Controller 层 API 接口
- 定时任务
- 前端页面

## 测试执行

```bash
mvn test
```

## 结论

✅ 所有测试用例通过，项目基础功能测试覆盖良好。
