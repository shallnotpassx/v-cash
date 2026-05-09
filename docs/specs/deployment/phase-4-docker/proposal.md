# Phase 4: 本地 Docker 部署

## 概述

将整个 v-cash 系统 Docker 容器化，使用 docker-compose 实现后端、前端、MySQL 三容器的一键启动。

## 动机

当前开发和运行依赖本地 JDK/Maven/Node.js/MySQL 环境，不同开发者环境不一致导致问题。Docker 化后：
- 环境统一，一键启动
- 无需安装 JDK、Maven、Node.js、MySQL
- 便于演示和本地验证

## 功能范围

| 编号 | 功能 | 描述 | 优先级 |
|------|------|------|--------|
| F12 | 后端 Docker 化 | 编写 Dockerfile，Maven 多阶段构建 Spring Boot jar 镜像 | P0 |
| F13 | 前端 Docker 化 | Nginx 容器托管 Vue 3 构建产物，反向代理 API 请求 | P0 |
| F14 | MySQL 容器 | docker-compose 编排 MySQL 8.0，挂载 `sql/init.sql` 自动初始化 | P0 |
| F15 | docker-compose 编排 | 三容器网络互通、端口映射、健康检查、启动顺序控制 | P0 |
| F16 | 本地全链路验证 | `docker-compose up -d` 后浏览器访问确认前后端 MySQL 全通 | P0 |

## 验收标准

- [ ] `docker-compose up -d` 一键启动全部服务
- [ ] 浏览器访问 `http://localhost:8089` 显示股票列表页
- [ ] 前端 Nginx 反向代理 `/api/*` 到后端 8089 端口
- [ ] MySQL 容器启动后自动执行 `init.sql` 建表
- [ ] 后端可连接 MySQL 容器读写数据
- [ ] `docker-compose down` 停止并清理所有容器
