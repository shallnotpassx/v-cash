## Purpose

提供 Docker 容器化一键部署能力，实现后端 + 前端 + MySQL 三容器编排，环境统一、一键启动。

## Requirements

### Requirement: One-command startup
系统 SHALL 支持通过 `docker-compose up -d` 一键启动全部服务。

#### Scenario: Full stack startup
- **WHEN** 用户在项目根目录执行 `docker-compose up -d`
- **THEN** MySQL / 后端 / Nginx 三容器依次启动成功

### Requirement: Frontend serves via Nginx
系统 SHALL 使用 Nginx 容器托管 Vue 3 构建产物，并反向代理 API 请求。

#### Scenario: Static file serving
- **WHEN** 浏览器访问 `http://localhost`
- **THEN** Nginx 返回 Vue 前端页面

#### Scenario: API proxy
- **WHEN** 浏览器请求 `http://localhost/api/stock/list`
- **THEN** Nginx 将请求转发到后端容器 `http://backend:8089/api/stock/list`

### Requirement: Database auto-initialization
系统 SHALL 在 MySQL 容器首次启动时自动执行建表脚本。

#### Scenario: Auto init
- **WHEN** MySQL 容器首次启动
- **THEN** 自动执行 `sql/init.sql` 创建 stock_basic / stock_financial / stock_announcement 表
