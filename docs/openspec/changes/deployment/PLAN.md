# T01 — 补齐本地优先的 deployment/ 目录结构

> **Current Task:** T01
> **Owning Review:** R01
> **Review Status:** pending

**Goal:** 在 `deployment/` 目录下建立 Docker Compose 骨架：后端、前端、MySQL 三服务，含 Nginx 反向代理配置。

**Architecture:** Docker Compose 编排三个服务：`backend`（Spring Boot，端口 8089）、`frontend`（Vue 3 + Nginx，端口 3000）、`mysql`（端口 3306）。前端 Nginx 反向代理 `/api` 到后端。

**Tech Stack:** Docker, Docker Compose, Nginx, MySQL 8.0

---

## 文件结构

| 文件 | 职责 |
|---|---|
| `deployment/docker-compose.yml` | 三服务编排 |
| `deployment/backend/Dockerfile` | 后端镜像（多阶段构建） |
| `deployment/frontend/Dockerfile` | 前端镜像（Vue 构建 + Nginx） |
| `deployment/frontend/nginx.conf` | Nginx 配置（反向代理 + 静态文件） |
| `deployment/.env.example` | 环境变量模板 |

---

### Task 1.1: 创建 docker-compose.yml

**Files:**
- Create: `deployment/docker-compose.yml`

```yaml
version: "3.9"

services:
  mysql:
    image: mysql:8.0
    container_name: vcash-mysql
    restart: unless-stopped
    environment:
      MYSQL_ROOT_PASSWORD: ${MYSQL_ROOT_PASSWORD:-root}
      MYSQL_DATABASE: ${MYSQL_DATABASE:-vcash}
      MYSQL_USER: ${MYSQL_USER:-vcash}
      MYSQL_PASSWORD: ${MYSQL_PASSWORD:-vcash}
    ports:
      - "${MYSQL_PORT:-3306}:3306"
    volumes:
      - mysql_data:/var/lib/mysql
      - ../sql:/docker-entrypoint-initdb.d:ro
    healthcheck:
      test: ["CMD", "mysqladmin", "ping", "-h", "localhost"]
      interval: 10s
      timeout: 5s
      retries: 5

  backend:
    build:
      context: ../backend
      dockerfile: ../deployment/backend/Dockerfile
    container_name: vcash-backend
    restart: unless-stopped
    depends_on:
      mysql:
        condition: service_healthy
    environment:
      SERVER_PORT: 8089
      SPRING_DATASOURCE_URL: jdbc:mysql://mysql:3306/${MYSQL_DATABASE:-vcash}?useSSL=false&allowPublicKeyRetrieval=true
      SPRING_DATASOURCE_USERNAME: ${MYSQL_USER:-vcash}
      SPRING_DATASOURCE_PASSWORD: ${MYSQL_PASSWORD:-vcash}
    ports:
      - "${BACKEND_PORT:-8089}:8089"

  frontend:
    build:
      context: ../frontend
      dockerfile: ../deployment/frontend/Dockerfile
    container_name: vcash-frontend
    restart: unless-stopped
    depends_on:
      - backend
    ports:
      - "${FRONTEND_PORT:-3000}:80"

volumes:
  mysql_data:
```

---

### Task 1.2: 创建后端 Dockerfile

**Files:**
- Create: `deployment/backend/Dockerfile`

```dockerfile
# ---- build stage ----
FROM maven:3.9-eclipse-temurin-17 AS build
WORKDIR /app
COPY pom.xml .
RUN mvn dependency:go-offline -B
COPY src ./src
RUN mvn package -DskipTests -B

# ---- runtime stage ----
FROM eclipse-temurin:17-jre
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
EXPOSE 8089
ENTRYPOINT ["java", "-jar", "app.jar"]
```

---

### Task 1.3: 创建前端 Dockerfile + Nginx 配置

**Files:**
- Create: `deployment/frontend/Dockerfile`
- Create: `deployment/frontend/nginx.conf`

- [ ] **Step 1: 前端 Dockerfile**

```dockerfile
# ---- build stage ----
FROM node:20-alpine AS build
WORKDIR /app
COPY package*.json ./
RUN npm ci
COPY . .
RUN npm run build

# ---- runtime stage ----
FROM nginx:1.25-alpine
COPY --from=build /app/dist /usr/share/nginx/html
COPY nginx.conf /etc/nginx/conf.d/default.conf
EXPOSE 80
CMD ["nginx", "-g", "daemon off;"]
```

- [ ] **Step 2: nginx.conf**

```nginx
server {
    listen 80;
    server_name localhost;

    root /usr/share/nginx/html;
    index index.html;

    # Vue SPA fallback
    location / {
        try_files $uri $uri/ /index.html;
    }

    # API proxy to backend
    location /api/ {
        proxy_pass http://backend:8089;
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
        proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
        proxy_set_header X-Forwarded-Proto $scheme;
    }
}
```

---

### Task 1.4: 创建环境变量模板

**Files:**
- Create: `deployment/.env.example`

```bash
# MySQL
MYSQL_ROOT_PASSWORD=root
MYSQL_DATABASE=vcash
MYSQL_USER=vcash
MYSQL_PASSWORD=vcash
MYSQL_PORT=3306

# Backend
BACKEND_PORT=8089

# Frontend
FRONTEND_PORT=3000
```

---

### Task 1.5: 验证

```bash
# 检查 docker-compose 语法
docker compose -f deployment/docker-compose.yml config -q
```

预期：无错误输出（仅警告可忽略）。

---

### Task 1.6: 提交

```bash
git add deployment/
git add docs/openspec/changes/deployment/PLAN.md
git commit -m "feat(deployment): T01 Docker Compose skeleton with backend, frontend, and MySQL"
```
