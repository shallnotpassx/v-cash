## Why

当前开发和运行依赖本地 JDK/Maven/Node.js/MySQL 环境，不同开发者环境不一致导致问题。Docker 化后可实现环境统一、一键启动、无需本地安装开发工具链。

## What Changes

- 后端 Dockerfile：Maven 多阶段构建 Spring Boot jar 镜像
- 前端 Dockerfile：Nginx 容器托管 Vue 3 构建产物
- MySQL 8.0 容器：挂载 sql/init.sql 自动初始化
- docker-compose.yml 三容器编排（网络互通、健康检查、启动顺序控制）
- .env 环境变量管理敏感配置

## Capabilities

### New Capabilities
- `docker-deployment`: Docker 容器化一键部署——后端 + 前端 + MySQL 三容器编排

### Modified Capabilities
- None

## Impact

- 新增 Dockerfile.backend / Dockerfile.frontend / nginx.conf / docker-compose.yml
- 新增 .env / .env.example / .dockerignore
- Nginx 反向代理 /api/* 到后端 8089 端口
- MySQL 数据持久化 volume
