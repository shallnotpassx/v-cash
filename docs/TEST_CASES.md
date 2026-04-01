# v-cash 测试用例文档

## 1. 概述

### 1.1 测试目标
覆盖项目已实现的所有功能模块，确保代码质量和业务逻辑正确性。

### 1.2 测试框架
- JUnit 5
- Mockito (Mock 外部依赖)
- Spring Boot Test (集成测试)

### 1.3 代码架构说明
项目存在两套并行代码：
- **Legacy 代码** (`src/`)：单模块架构，包含 StockService、FinancialService
- **DDD 代码** (`v-cash-*/`)：多模块架构，包含 ApplicationService、DomainService、Repository、Adapter

两套代码功能等价，测试需覆盖两套实现。

---

## 2. 数据库表结构

### 2.1 stock_basic（股票基本信息表）
| 字段 | 类型 | 说明 |
|------|------|------|
| id | BIGINT | 主键，自增 |
| stock_code | VARCHAR(20) | 股票代码 |
| stock_name | VARCHAR(100) | 股票名称 |
| market | VARCHAR(20) | 市场（A/HK） |
| industry | VARCHAR(100) | 所属行业 |
| list_date | DATE | 上市日期 |

### 2.2 stock_financial（财务数据表）
| 字段 | 类型 | 说明 |
|------|------|------|
| id | BIGINT | 主键，自增 |
| stock_id | BIGINT | 关联 stock_basic.id |
| report_period | DATE | 报告期 |
| revenue | DECIMAL(20,2) | 营业收入 |
| net_profit | DECIMAL(20,2) | 净利润 |
| roe | DECIMAL(10,4) | 净资产收益率 |
| pe | DECIMAL(10,4) | 市盈率 |
| pb | DECIMAL(10,4) | 市净率 |

### 2.3 stock_announcement（公告表）
| 字段 | 类型 | 说明 |
|------|------|------|
| id | BIGINT | 主键，自增 |
| stock_id | BIGINT | 关联 stock_basic.id |
| title | VARCHAR(500) | 公告标题 |
| announcement_type | VARCHAR(50) | 公告类型 |
| publish_date | DATE | 发布日期 |
| source_url | VARCHAR(500) | 原文链接 |

---

## 3. 测试用例详细设计

### 3.1 股票管理模块 (Stock)

#### 3.1.1 StockService.listFromDb(market) - 从数据库查询股票列表

| 用例ID | 测试场景 | 输入 | 预期结果 | 优先级 |
|--------|----------|------|----------|--------|
| TC-STK-001 | 查询A股列表 | market="A" | 返回 market="A" 的所有股票 | P0 |
| TC-STK-002 | 查询港股列表 | market="HK" | 返回 market="HK" 的所有股票 | P0 |
| TC-STK-003 | 查询全部股票（不传market） | market=null | 返回所有市场的股票 | P0 |
| TC-STK-004 | 查询空市场 | market="invalid" | 返回空列表 | P1 |
| TC-STK-005 | 数据库无数据 | 任意market | 返回空列表 | P1 |
| TC-STK-006 | 市场参数大小写不敏感 | market="a" / market="hk" | 正确匹配对应市场 | P2 |

#### 3.1.2 StockService.getByCode(stockCode) - 按代码查询股票

| 用例ID | 测试场景 | 输入 | 预期结果 | 优先级 |
|--------|----------|------|----------|--------|
| TC-STK-007 | 查询存在的A股 | stockCode="600519" | 返回对应的 StockBasic 对象 | P0 |
| TC-STK-008 | 查询存在的港股 | stockCode="00700" | 返回对应的 StockBasic 对象 | P0 |
| TC-STK-009 | 查询不存在的股票 | stockCode="999999" | 返回 null | P0 |
| TC-STK-010 | 查询空代码 | stockCode="" / null | 返回 null 或抛异常 | P1 |
| TC-STK-011 | 查询已逻辑删除的股票 | stockCode="已删除代码" | 返回 null | P1 |

#### 3.1.3 StockService.fetchFromSource(market) - 从外部数据源获取股票

| 用例ID | 测试场景 | 输入 | 预期结果 | 优先级 |
|--------|----------|------|----------|--------|
| TC-STK-012 | Tushare 成功获取A股 | market="A"，Tushare正常 | 返回股票列表 | P0 |
| TC-STK-013 | Tushare 成功获取港股 | market="HK"，Tushare正常 | 返回股票列表 | P0 |
| TC-STK-014 | Tushare失败，EastMoney兜底 | Tushare抛异常，EastMoney正常 | 返回EastMoney的数据 | P0 |
| TC-STK-015 | 所有数据源都失败 | 所有Adapter抛异常 | 返回空列表 | P0 |
| TC-STK-016 | 单个Adapter返回空列表 | Adapter返回[] | 尝试下一个Adapter | P1 |
| TC-STK-017 | 数据源返回的股票字段完整 | 正常响应 | StockBasic包含code、name、market、industry | P1 |

#### 3.1.4 StockService.saveToDb(stocks) - 保存股票到数据库

| 用例ID | 测试场景 | 输入 | 预期结果 | 优先级 |
|--------|----------|------|----------|--------|
| TC-STK-018 | 插入新股票 | 数据库中不存在的股票 | 执行 INSERT，返回保存后的对象 | P0 |
| TC-STK-019 | 更新已存在股票 | 数据库中已存在的stockCode | 执行 UPDATE，更新name/industry等字段 | P0 |
| TC-STK-020 | 批量保存（混合新增和更新） | 部分存在、部分不存在的股票列表 | 新增的执行INSERT，已存在的执行UPDATE | P0 |
| TC-STK-021 | 保存空列表 | stocks=[] | 无操作，不抛异常 | P1 |
| TC-STK-022 | 保存null | stocks=null | 抛异常或返回 | P1 |

#### 3.1.5 StockService.refreshStockList(market) - 刷新股票列表

| 用例ID | 测试场景 | 输入 | 预期结果 | 优先级 |
|--------|----------|------|----------|--------|
| TC-STK-023 | 完整刷新A股列表 | market="A" | 从数据源获取并保存到数据库 | P0 |
| TC-STK-024 | 完整刷新港股列表 | market="HK" | 从数据源获取并保存到数据库 | P0 |
| TC-STK-025 | 数据源获取失败 | 数据源全部不可用 | 不更新数据库，返回空或异常 | P0 |

---

### 3.2 财务数据模块 (Financial)

#### 3.2.1 FinancialService.getByStockCode(stockCode) - 查询股票全部财务数据

| 用例ID | 测试场景 | 输入 | 预期结果 | 优先级 |
|--------|----------|------|----------|--------|
| TC-FIN-001 | 查询存在的股票财务数据 | stockCode="600519" | 返回按报告期降序的财务数据列表 | P0 |
| TC-FIN-002 | 查询不存在的股票 | stockCode="999999" | 返回空列表 | P0 |
| TC-FIN-003 | 股票存在但无财务数据 | stockCode存在，financial表无记录 | 返回空列表 | P1 |
| TC-FIN-004 | 财务数据按报告期降序排列 | 有多期数据 | 列表按 report_period DESC 排序 | P0 |
| TC-FIN-005 | 财务数据字段完整性 | 正常数据 | 包含revenue、netProfit、roe、pe、pb | P1 |

#### 3.2.2 FinancialService.getLatestByStockCode(stockCode) - 查询最新财务数据

| 用例ID | 测试场景 | 输入 | 预期结果 | 优先级 |
|--------|----------|------|----------|--------|
| TC-FIN-006 | 查询最新一期财务数据 | stockCode="600519" | 返回最近一期的 StockFinancial | P0 |
| TC-FIN-007 | 无财务数据时查询 | stockCode存在但无财务记录 | 返回 null | P0 |
| TC-FIN-008 | 股票不存在时查询 | stockCode="999999" | 返回 null | P0 |

#### 3.2.3 FinancialService.fetchFromSource(stockCode) - 从外部数据源获取财务数据

| 用例ID | 测试场景 | 输入 | 预期结果 | 优先级 |
|--------|----------|------|----------|--------|
| TC-FIN-009 | Tushare 成功获取财务数据 | stockCode="600519"，Tushare正常 | 返回财务数据列表 | P0 |
| TC-FIN-010 | Tushare失败，EastMoney兜底 | Tushare抛异常 | 返回EastMoney的数据 | P0 |
| TC-FIN-011 | 所有数据源都失败 | 所有Adapter抛异常 | 返回空列表 | P0 |
| TC-FIN-012 | A股财务数据获取 | stockCode="600519" | 调用A股接口 | P1 |
| TC-FIN-013 | 港股财务数据获取 | stockCode="00700" | 调用港股接口 | P1 |

#### 3.2.4 FinancialService.saveToDb(stockCode, financials) - 保存财务数据

| 用例ID | 测试场景 | 输入 | 预期结果 | 优先级 |
|--------|----------|------|----------|--------|
| TC-FIN-014 | 插入新财务数据 | 不存在的报告期数据 | 执行 INSERT | P0 |
| TC-FIN-015 | 更新已存在财务数据 | 已存在的stockId+reportPeriod | 执行 UPDATE | P0 |
| TC-FIN-016 | 股票不存在时保存 | stockCode在stock_basic中不存在 | 不保存，返回或抛异常 | P0 |
| TC-FIN-017 | 批量保存（混合新增和更新） | 多期数据，部分已存在 | 分别执行INSERT/UPDATE | P0 |
| TC-FIN-018 | 保存空列表 | financials=[] | 无操作 | P1 |
| TC-FIN-019 | BigDecimal 精度验证 | 含小数的财务数据 | 精度不丢失 | P1 |

#### 3.2.5 FinancialService.refreshFinancialData(stockCode) - 刷新财务数据

| 用例ID | 测试场景 | 输入 | 预期结果 | 优先级 |
|--------|----------|------|----------|--------|
| TC-FIN-020 | 完整刷新财务数据 | stockCode="600519" | 从数据源获取并保存到数据库 | P0 |
| TC-FIN-021 | 数据源获取失败 | 数据源全部不可用 | 不更新数据库 | P0 |
| TC-FIN-022 | 股票不存在时刷新 | stockCode="999999" | 不执行保存 | P0 |

---

### 3.3 数据源适配器模块 (Adapter)

#### 3.3.1 TushareAdapter - Tushare Pro API 适配器

| 用例ID | 测试场景 | 输入 | 预期结果 | 优先级 |
|--------|----------|------|----------|--------|
| TC-TSH-001 | getSourceName() 返回值 | - | 返回 "Tushare" | P1 |
| TC-TSH-002 | getStockList 获取A股 | market="" | 调用 stock_basic API，exchange="SSE" | P0 |
| TC-TSH-003 | getStockList 获取港股 | market="5" | 调用 stock_basic API，exchange="HKEX" | P0 |
| TC-TSH-004 | getStockDetail 获取A股详情 | stockCode="600519" | 调用 stock_basic API，ts_code="600519.SH" | P0 |
| TC-TSH-005 | getStockDetail 获取港股详情 | stockCode="00700" | 调用 stock_basic API，ts_code="00700.HK" | P0 |
| TC-TSH-006 | getFinancialData 获取财务数据 | stockCode="600519" | 调用 fina_indicator API | P0 |
| TC-TSH-007 | getLatestFinancialData 获取最新财务 | stockCode="600519" | 返回最近一期财务数据 | P0 |
| TC-TSH-008 | 股票代码转换 - 上海A股 | code="600519" | 转换为 "600519.SH" | P1 |
| TC-TSH-009 | 股票代码转换 - 深圳A股 | code="000001" | 转换为 "000001.SZ" | P1 |
| TC-TSH-010 | 股票代码转换 - 创业板 | code="300750" | 转换为 "300750.SZ" | P1 |
| TC-TSH-011 | 股票代码转换 - 北交所 | code="830799" | 转换为 "830799.BJ" | P1 |
| TC-TSH-012 | 股票代码转换 - 港股 | code="00700" | 转换为 "00700.HK" | P1 |
| TC-TSH-013 | API 调用超时 | Tushare API 超时 | 抛异常或返回空 | P1 |
| TC-TSH-014 | API 返回错误 | Tushare 返回错误码 | 抛异常或返回空 | P1 |
| TC-TSH-015 | 日期格式解析 | Tushare返回YYYYMMDD格式 | 正确解析为 LocalDate/Date | P1 |

#### 3.3.2 EastMoneyStockAdapter - 东方财富 API 适配器

| 用例ID | 测试场景 | 输入 | 预期结果 | 优先级 |
|--------|----------|------|----------|--------|
| TC-EM-001 | getSourceName() 返回值 | - | 返回 "EastMoney" | P1 |
| TC-EM-002 | getStockList 获取A股 | market="" | 调用 RPT_BASIC_STOCKINFO 接口 | P0 |
| TC-EM-003 | getStockList 获取港股 | market="5" | 调用 RPT_BASIC_STOCKINFO_HK 接口 | P0 |
| TC-EM-004 | getStockDetail 获取A股详情 | stockCode="600519" | 调用 RPT_BASIC_STOCKINFO 并过滤 | P0 |
| TC-EM-005 | getStockDetail 获取港股详情 | stockCode="00700" | 调用 RPT_BASIC_STOCKINFO_HK 并过滤 | P0 |
| TC-EM-006 | getFinancialData 获取A股财务 | stockCode="600519" | 调用 RPT_FINMAIN_INDICATOR 接口 | P0 |
| TC-EM-007 | getFinancialData 获取港股财务 | stockCode="00700" | 调用 RPT_FINMAIN_INDICATOR_HK 接口 | P0 |
| TC-EM-008 | getLatestFinancialData 获取最新财务 | stockCode="600519" | 返回最近一期财务数据 | P0 |
| TC-EM-009 | 港股代码识别 | code="00700"（5位数字） | 识别为港股 | P1 |
| TC-EM-010 | A股代码识别 | code="600519"（6位数字） | 识别为A股 | P1 |
| TC-EM-011 | API 调用超时 | 东方财富 API 超时 | 抛异常或返回空 | P1 |
| TC-EM-012 | API 返回空数据 | 接口返回空结果 | 返回空列表 | P1 |
| TC-EM-013 | 响应数据解析 | 正常JSON响应 | 正确解析为 StockBasic/StockFinancial | P0 |

#### 3.3.3 StockDomainService - 领域服务（适配器编排）

| 用例ID | 测试场景 | 输入 | 预期结果 | 优先级 |
|--------|----------|------|----------|--------|
| TC-DOM-001 | 第一个Adapter成功 | adapter1返回数据 | 直接返回adapter1的数据 | P0 |
| TC-DOM-002 | 第一个Adapter失败，第二个成功 | adapter1抛异常，adapter2正常 | 返回adapter2的数据 | P0 |
| TC-DOM-003 | 所有Adapter都失败 | 所有Adapter抛异常 | 返回空列表 | P0 |
| TC-DOM-004 | 单个Adapter | 只有一个Adapter且成功 | 返回该Adapter的数据 | P1 |
| TC-DOM-005 | 无Adapter | Adapter列表为空 | 返回空列表 | P1 |
| TC-DOM-006 | Adapter返回空列表 | adapter返回[] | 尝试下一个Adapter | P1 |

---

### 3.4 仓储模块 (Repository)

#### 3.4.1 StockRepository - 股票仓储实现

| 用例ID | 测试场景 | 输入 | 预期结果 | 优先级 |
|--------|----------|------|----------|--------|
| TC-REP-001 | findAll 查询全部 | market=null | 返回所有股票 | P0 |
| TC-REP-002 | findAll 按市场过滤 | market="A" | 返回A股列表 | P0 |
| TC-REP-003 | findByCode 找到股票 | stockCode="600519" | 返回 Optional<StockBasic>.present | P0 |
| TC-REP-004 | findByCode 未找到股票 | stockCode="999999" | 返回 Optional.empty() | P0 |
| TC-REP-005 | save 新增股票 | 不存在的stockCode | 执行 INSERT | P0 |
| TC-REP-006 | save 更新股票 | 已存在的stockCode | 执行 UPDATE | P0 |
| TC-REP-007 | saveAll 批量保存 | 多个股票对象 | 逐个保存 | P0 |

#### 3.4.2 StockBasicMapper - MyBatis Plus 映射

| 用例ID | 测试场景 | 输入 | 预期结果 | 优先级 |
|--------|----------|------|----------|--------|
| TC-MAP-001 | insert 插入数据 | 有效的 StockBasic | 插入成功，id回填 | P0 |
| TC-MAP-002 | selectById 查询 | 存在的id | 返回 StockBasic | P0 |
| TC-MAP-003 | selectList 条件查询 | LambdaQueryWrapper | 返回匹配的记录 | P0 |
| TC-MAP-004 | updateById 更新 | 修改后的 StockBasic | 更新成功 | P0 |
| TC-MAP-005 | 逻辑删除 | 调用 deleteById | deleted=1，记录仍存在 | P1 |

---

### 3.5 值对象与枚举模块

#### 3.5.1 Market 枚举

| 用例ID | 测试场景 | 输入 | 预期结果 | 优先级 |
|--------|----------|------|----------|--------|
| TC-ENUM-001 | Market.A 的 prefix | - | 返回 "" | P1 |
| TC-ENUM-002 | Market.HK 的 prefix | - | 返回 "5" | P1 |
| TC-ENUM-003 | of("A") 查找 | value="A" | 返回 Market.A | P1 |
| TC-ENUM-004 | of("HK") 查找 | value="HK" | 返回 Market.HK | P1 |
| TC-ENUM-005 | of("a") 大小写不敏感 | value="a" | 返回 Market.A | P2 |
| TC-ENUM-006 | of("hk") 大小写不敏感 | value="hk" | 返回 Market.HK | P2 |
| TC-ENUM-007 | of("invalid") 无效值 | value="invalid" | 返回 null 或抛异常 | P1 |

#### 3.5.2 StockCode 值对象

| 用例ID | 测试场景 | 输入 | 预期结果 | 优先级 |
|--------|----------|------|----------|--------|
| TC-VO-001 | A股 getFullCode | code="600519", market=A | 返回 "600519" | P1 |
| TC-VO-002 | 港股 getFullCode | code="00700", market=HK | 返回 "500700" | P1 |
| TC-VO-003 | equals 相同代码 | 两个相同的StockCode | 返回 true | P1 |
| TC-VO-004 | equals 不同代码 | 两个不同的StockCode | 返回 false | P1 |
| TC-VO-005 | hashCode 一致性 | 相同的StockCode | hashCode 相同 | P1 |

---

### 3.6 配置模块 (Config)

#### 3.6.1 RestTemplateConfig

| 用例ID | 测试场景 | 输入 | 预期结果 | 优先级 |
|--------|----------|------|----------|--------|
| TC-CFG-001 | RestTemplate Bean 创建 | Spring 上下文启动 | Bean 可用 | P1 |
| TC-CFG-002 | 连接超时配置 | - | connectTimeout=10000ms | P2 |
| TC-CFG-003 | 读取超时配置 | - | readTimeout=30000ms | P2 |

#### 3.6.2 MyBatisPlusConfig

| 用例ID | 测试场景 | 输入 | 预期结果 | 优先级 |
|--------|----------|------|----------|--------|
| TC-CFG-004 | 插入时自动填充 createTime | insert 操作 | createTime 自动设置 | P1 |
| TC-CFG-005 | 插入时自动填充 updateTime | insert 操作 | updateTime 自动设置 | P1 |
| TC-CFG-006 | 更新时自动填充 updateTime | update 操作 | updateTime 自动更新 | P1 |
| TC-CFG-007 | 分页插件生效 | 分页查询 | 返回分页结果 | P1 |
| TC-CFG-008 | 乐观锁插件生效 | 并发更新 | 版本号检查生效 | P2 |
| TC-CFG-009 | 逻辑删除生效 | delete 操作 | deleted=1，查询时过滤 | P1 |

---

### 3.7 DDD 应用层模块

#### 3.7.1 StockApplicationService

| 用例ID | 测试场景 | 输入 | 预期结果 | 优先级 |
|--------|----------|------|----------|--------|
| TC-APP-001 | listFromDb 查询全部 | market=null | 调用 IStockRepository.findAll(null) | P0 |
| TC-APP-002 | listFromDb 按市场过滤 | market="A" | 调用 IStockRepository.findAll("A") | P0 |
| TC-APP-003 | getByCode 查询股票 | stockCode="600519" | 调用 IStockRepository.findByCode() | P0 |
| TC-APP-004 | refreshStockList 刷新 | market="A" | 调用 StockDomainService 获取数据并保存 | P0 |

---

### 3.8 集成测试

| 用例ID | 测试场景 | 输入 | 预期结果 | 优先级 |
|--------|----------|------|----------|--------|
| TC-INT-001 | Spring 上下文启动 | - | 应用正常启动，所有Bean注入成功 | P0 |
| TC-INT-002 | 数据库连接测试 | - | 能正常执行SQL | P0 |
| TC-INT-003 | 端到端：刷新股票列表 | market="A" | 从API获取→保存到DB→查询验证 | P0 |
| TC-INT-004 | 端到端：刷新财务数据 | stockCode="600519" | 从API获取→保存到DB→查询验证 | P0 |
| TC-INT-005 | 事务回滚测试 | 保存过程中抛异常 | 数据不入库 | P0 |
| TC-INT-006 | 多Adapter优先级 | 配置多个Adapter | 按声明顺序尝试 | P1 |

---

## 4. 测试覆盖矩阵

| 模块 | 类/接口 | 已有测试 | 需新增测试 | 覆盖度 |
|------|---------|----------|------------|--------|
| **Stock Service (Legacy)** | StockService | 10个用例 | 5个边界用例 | 70% |
| **Financial Service (Legacy)** | FinancialService | 11个用例 | 8个边界用例 | 65% |
| **Stock Application (DDD)** | StockApplicationService | 0 | 4个用例 | 0% |
| **Stock Domain Service** | StockDomainService | 0 | 6个用例 | 0% |
| **Tushare Adapter** | TushareAdapter | 0 | 15个用例 | 0% |
| **EastMoney Adapter** | EastMoneyStockAdapter | 0 | 13个用例 | 0% |
| **Stock Repository** | StockRepository | 0 | 7个用例 | 0% |
| **Mapper 层** | StockBasicMapper | 0 | 5个用例 | 0% |
| **值对象/枚举** | Market, StockCode | 0 | 12个用例 | 0% |
| **配置类** | RestTemplateConfig, MyBatisPlusConfig | 0 | 9个用例 | 0% |
| **集成测试** | 端到端流程 | 0 | 6个用例 | 0% |

---

## 5. 测试数据准备

### 5.1 股票测试数据
```sql
-- A股测试数据
INSERT INTO stock_basic (stock_code, stock_name, market, industry, list_date) VALUES
('600519', '贵州茅台', 'A', '白酒', '2001-08-27'),
('000001', '平安银行', 'A', '银行', '1991-04-03'),
('300750', '宁德时代', 'A', '电池', '2018-06-11');

-- 港股测试数据
INSERT INTO stock_basic (stock_code, stock_name, market, industry, list_date) VALUES
('00700', '腾讯控股', 'HK', '互联网', '2004-06-16'),
('09988', '阿里巴巴', 'HK', '电商', '2019-11-26');
```

### 5.2 财务测试数据
```sql
INSERT INTO stock_financial (stock_id, report_period, revenue, net_profit, roe, pe, pb) VALUES
(1, '2024-12-31', 150560000000, 74734000000, 0.3156, 30.5, 10.2),
(1, '2023-12-31', 147690000000, 72280000000, 0.3089, 35.2, 11.5);
```

---

## 6. 测试分层策略

| 层级 | 测试类型 | 占比 | 说明 |
|------|----------|------|------|
| 单元测试 | Mockito Mock 外部依赖 | 60% | 测试单个类/方法的逻辑 |
| 集成测试 | @SpringBootTest + H2/测试库 | 30% | 测试组件间协作 |
| 端到端测试 | 真实API调用（可选） | 10% | 测试完整业务流程 |

---

## 7. 优先级说明

| 优先级 | 说明 | 执行时机 |
|--------|------|----------|
| P0 | 核心业务逻辑，必须覆盖 | 每次提交前 |
| P1 | 重要功能，边界条件 | 每次发版前 |
| P2 | 辅助功能，锦上添花 | 有时间时补充 |
