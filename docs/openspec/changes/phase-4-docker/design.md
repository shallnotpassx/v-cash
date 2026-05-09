## Context

当前开发依赖本地 JDK 17 / Maven / Node.js 18+ / MySQL 8.0 环境。新开发者需要 30+ 分钟配置环境。Docker 化后只需 Docker Desktop。

## Goals / Non-Goals

**Goals:**
- 后端多阶段 Docker 构建
- 前端 Nginx Docker 化
- MySQL 8.0 容器自动初始化
- docker-compose 一键编排

**Non-Goals:**
- 不做 Kubernetes 部署（本地开发不需要）
- 不做 CI/CD 流水线（Phase 5）

## Decisions

**Nginx 反向代理而非直连后端端口**
前端 → Nginx(:80) → 后端(:8089)，统一入口。
替代方案（前后端不同端口）被拒绝：需要 CORS 配置，增加复杂度。

**多阶段构建减小镜像体积**
Stage 1 编译（maven:3.9）→ Stage 2 运行（temurin:17-jre），最终镜像仅含 jar。
替代方案（单阶段构建）使镜像体积过大（~500MB vs ~150MB）。

**healthcheck + depends_on 控制启动顺序**
MySQL healthcheck 通过后后端才启动，后端 healthcheck 通过后 Nginx 才启动。
替代方案（固定 sleep 等待）被拒绝：不可靠，不同机器启动时间不同。

## Risks / Trade-offs

| 风险 | 应对 |
|------|------|
| 后端启动时 MySQL 未就绪 | healthcheck + Spring Boot 自动重连 |
| Tushare token 泄露 | 通过 .env 注入，加入 .gitignore |
| Maven 构建耗时 | 多阶段构建利用 Docker 缓存层 |
