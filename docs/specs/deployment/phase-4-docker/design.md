# Phase 4: Docker 部署 — 技术设计

## 架构决策

### 三容器拓扑

```
┌─────────────────────────────────────────────┐
│ docker-compose network: vcash-net           │
│                                             │
│  ┌──────────┐  ┌──────────┐  ┌──────────┐  │
│  │  nginx   │  │  backend │  │  mysql   │  │
│  │  :80     │→ │  :8089   │→ │  :3306   │  │
│  │  (前端)  │  │  (API)   │  │  (DB)    │  │
│  └──────────┘  └──────────┘  └──────────┘  │
│       │                             │       │
│    localhost:80              localhost:3306  │
└─────────────────────────────────────────────┘
```

- **nginx**：对外暴露 80 端口，静态文件 + `/api/*` 反向代理
- **backend**：仅对内暴露 8089，不直接对外
- **mysql**：暴露 3306 供本地调试，数据持久化到 volume

### 后端 Dockerfile 多阶段构建

```
Stage 1: maven:3.9-eclipse-temurin-17 → mvn package
Stage 2: eclipse-temurin:17-jre → 复制 jar → ENTRYPOINT java -jar
```

### 前端 Nginx 反向代理

```
location /api/ {
    proxy_pass http://backend:8089;
}
location / {
    root /usr/share/nginx/html;
}
```

## 数据流

```
浏览器 → localhost:80
  ├── /api/* → nginx 反向代理 → backend:8089 → mysql:3306
  └── /*     → nginx 静态文件服务（Vue 构建产物）
```

## 涉及文件

| 文件 | 说明 |
|------|------|
| `Dockerfile.backend` | 后端多阶段构建 |
| `Dockerfile.frontend` | 前端构建 + Nginx |
| `nginx.conf` | Nginx 反向代理配置 |
| `docker-compose.yml` | 三容器编排 |
| `.env` | 环境变量（MySQL 密码、Tushare token 等） |
| `.dockerignore` | Docker 构建忽略文件 |

## 资源配置

| 容器 | 镜像 | 端口映射 | 数据卷 |
|------|------|----------|--------|
| mysql | mysql:8.0 | 3306:3306 | `mysql-data:/var/lib/mysql`, `./sql/init.sql:/docker-entrypoint-initdb.d/init.sql` |
| backend | 自定义 | 8089 (内部) | 无 |
| nginx | 自定义 | 80:80 | 无 |

## 风险与对策

| 风险 | 影响 | 应对 |
|------|------|------|
| 后端启动时 MySQL 未就绪 | 连接失败 | docker-compose `depends_on` + healthcheck + Spring Boot 重连 |
| Tushare token 泄露 | 安全风险 | 通过 `.env` 注入，加入 `.gitignore` |
| Maven 构建耗时 | 首次 build 慢 | 多阶段构建利用 Docker 缓存 |
| 前端构建时后端不可用 | 不影响 | 前端构建是纯静态，不依赖后端运行时 |
