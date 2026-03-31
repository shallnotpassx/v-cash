# v-cash 智能体说明

## 项目概述

v-cash 是一个股票数据查询与分析系统，用于获取A股和港股的财务数据及公司公告信息。

## 智能体角色

### 1. 项目架构师 (Project Architect)
- **职责**: 整体技术方案设计、技术选型决策
- **主要任务**:
  - 制定项目架构和技术栈
  - 设计数据库模型
  - 规划API接口
  - 制定开发规范

### 2. 后端开发 (Backend Developer)
- **职责**: Spring Boot 后端开发
- **主要任务**:
  - 领域模型设计
  - 数据源适配器实现
  - 应用服务开发
  - REST API接口开发
  - 定时任务开发

### 3. 前端开发 (Frontend Developer)
- **职责**: Vue 3 前端开发
- **主要任务**:
  - 前端项目初始化
  - 页面组件开发
  - API调用封装
  - 前后端联调

### 4. 数据库管理员 (DBA)
- **职责**: 数据库设计与管理
- **主要任务**:
  - 数据库表结构设计
  - SQL脚本编写
  - 数据库性能优化

## 架构设计 (DDD领域驱动设计)

### 分层架构
```
com.gongbotao.vcash
├── domain/               # 领域层 - 核心业务逻辑
│   ├── stock/            # 股票领域
│   │   ├── entity/       # 实体（聚合根）
│   │   ├── valueobject/  # 值对象
│   │   ├── repository/   # 仓储接口
│   │   └── service/      # 领域服务
│   ├── financial/        # 财务领域
│   │   ├── entity/
│   │   ├── valueobject/
│   │   ├── repository/
│   │   └── service/
│   ├── announcement/     # 公告领域
│   │   ├── entity/
│   │   ├── valueobject/
│   │   ├── repository/
│   │   └── service/
│   └── enums/            # 共享枚举
├── application/          # 应用层 - 用例编排
│   ├── stock/
│   │   ├── command/      # 命令对象
│   │   ├── query/        # 查询对象
│   │   └── service/      # 应用服务
│   ├── financial/
│   └── announcement/
├── infrastructure/        # 基础设施层
│   ├── adapter/          # 外部数据源适配器
│   │   ├── tushare/
│   │   ├── eastmoney/
│   │   └── cninfo/
│   ├── persistence/      # 持久化实现
│   │   ├── mapper/       # MyBatis映射
│   │   └── repository/   # 仓储实现
│   └── config/           # 配置类
├── api/                  # 接口层
│   └── controller/       # REST控制器
└── task/                 # 定时任务
```

### DDD核心概念

#### 1. 实体 (Entity)
- 有唯一标识的对象
- 如：StockBasic、StockFinancial、StockAnnouncement

#### 2. 值对象 (Value Object)
- 无唯一标识，属性不可变
- 如：StockCode、Market、Money

#### 3. 聚合根 (Aggregate Root)
- 聚合的根节点，负责维护聚合内部一致性
- 外部对象只能通过聚合根访问内部实体

#### 4. 仓储接口 (Repository)
- 定义在领域层，声明持久化方法
- 基础设施层实现具体的持久化逻辑

#### 5. 领域服务 (Domain Service)
- 承载跨聚合的业务逻辑
- 负责协调多个实体/聚合之间的交互

#### 6. 应用服务 (Application Service)
- 编排领域服务，实现用例
- 处理事务、参数校验、DTO转换

### 领域划分

#### 股票领域 (Stock)
- 聚合：Stock
- 实体：StockBasic
- 仓储接口：IStockRepository
- 领域服务：StockDomainService

#### 财务领域 (Financial)
- 聚合：Financial
- 实体：StockFinancial
- 仓储接口：IFinancialRepository

#### 公告领域 (Announcement)
- 聚合：Announcement
- 实体：StockAnnouncement
- 仓储接口：IAnnouncementRepository

## 模块划分 (Legacy)

### 后端模块
```
com.gongbotao.vcash
├── domain/           # 领域模型
│   ├── entity/       # 实体类
│   ├── dto/          # 数据传输对象
│   └── enums/        # 枚举类
├── data/             # 数据层
│   ├── source/       # 数据源适配器
│   │   ├── adapter/  # 适配器接口
│   │   ├── tushare/  # Tushare实现
│   │   ├── eastmoney/ # 东方财富实现
│   │   └── cninfo/   # 巨潮资讯实现
│   └── storage/      # 数据库操作
│       ├── mapper/   # MyBatis映射
│       └── repository/ # 仓储实现
├── service/          # 业务逻辑
│   ├── stock/        # 股票服务
│   ├── financial/    # 财务数据服务
│   └── announcement/ # 公告服务
├── task/             # 定时任务
├── config/           # 配置类
└── controller/       # 接口层
```

### 前端模块
```
vcash-web/src/
├── api/              # API调用
├── components/       # 公共组件
├── views/            # 页面视图
├── router/           # 路由配置
└── store/            # 状态管理
```

## 协作流程

1. **架构师** 完成技术方案设计
2. **DBA** 创建数据库表结构
3. **后端开发** 实现领域模型和仓储
4. **后端开发** 实现应用服务和API接口
5. **前端开发** 开发前端页面
6. **前后端联调** 完成系统集成

## 开发规范

- 代码风格遵循阿里巴巴Java开发规约
- 使用Git进行版本管理
- 提交信息采用 Conventional Commits 格式
- 代码审查采用Pull Request流程
- 每次完成新的功能开发后，都更新PLAN.md文件
- **DDD规范**：
  - 领域层不能依赖Infrastructure层
  - 应用层依赖领域层，不依赖基础设施
  - 领域服务处理跨聚合逻辑
  - 应用服务编排用例，处理事务

## 数据库规范

- **禁止使用 root 用户连接数据库**，必须创建专用用户
- 应用使用最小权限原则，只授予必要的库权限
- 用户名统一使用项目名或开发者名，密码使用强密码
- 数据库配置文件禁止提交到版本控制系统
