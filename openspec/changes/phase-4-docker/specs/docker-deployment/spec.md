## ADDED Requirements

### Requirement: One-command startup
系统 SHALL 支持 `docker-compose up -d` 一键启动全部服务。

#### Scenario: Full stack startup
- **WHEN** 执行 `docker-compose up -d`
- **THEN** MySQL / 后端 / Nginx 三容器依次启动成功

### Requirement: Frontend via Nginx
系统 SHALL 使用 Nginx 托管前端并反向代理 API。

#### Scenario: Static file serving
- **WHEN** 浏览器访问 `http://localhost`
- **THEN** Nginx 返回 Vue 前端页面

#### Scenario: API proxy
- **WHEN** 浏览器请求 `http://localhost/api/stock/list`
- **THEN** Nginx 转发到 `http://backend:8089/api/stock/list`

### Requirement: Database auto-init
系统 SHALL 在 MySQL 首次启动时自动执行建表脚本。

#### Scenario: Auto init
- **WHEN** MySQL 容器首次启动
- **THEN** 自动执行 `sql/init.sql` 创建数据表
