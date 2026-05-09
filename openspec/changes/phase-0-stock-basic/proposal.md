## Why

将项目从 Legacy 单模块迁移到 DDD 四层架构，搭建全链路骨架——从 Controller → ApplicationService → DomainService → Adapter → Repository → 数据库，验证新架构可行性。

## What Changes

- 搭建 DDD 四层多模块项目结构（v-cash-domain / v-cash-application / v-cash-infrastructure / v-cash-api）
- 实现股票基础数据 CRUD：列表查看、详情查看、手动刷新
- 实现适配器链（Tushare → 东方财富兜底）获取股票数据
- 前端 Vue 3 + Element Plus 项目初始化，股票列表页和详情页
- 删除 Legacy src/ 单模块目录

## Capabilities

### New Capabilities
- `stock-basic`: 股票基础数据查询——列表（按市场过滤）、详情（按 code 查询）
- `stock-refresh`: 手动触发股票数据从数据源同步到本地数据库

### Modified Capabilities
- None

## Impact

- 新增 4 个 Maven 模块，重构原有单模块代码
- 新增 stock_basic / stock_financial / stock_announcement 实体映射
- 新增 Vue 3 前端项目 vcash-web
- 删除旧 src/ 目录
