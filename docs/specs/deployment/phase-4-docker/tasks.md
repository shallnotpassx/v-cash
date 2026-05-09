# Phase 4: 任务清单

## Docker 文件
- [ ] `Dockerfile.backend`
  - [ ] Stage 1：基于 `maven:3.9-eclipse-temurin-17`，复制项目，执行 `mvn package -DskipTests`
  - [ ] Stage 2：基于 `eclipse-temurin:17-jre`，复制 jar，`ENTRYPOINT ["java", "-jar", "app.jar"]`
- [ ] `Dockerfile.frontend`
  - [ ] Stage 1：基于 `node:20-alpine`，`npm ci && npm run build`
  - [ ] Stage 2：基于 `nginx:alpine`，复制构建产物 + nginx.conf

## Nginx 配置
- [ ] `nginx.conf`
  - [ ] `/` → Vue 构建产物根目录
  - [ ] `/api/` → 反向代理到 `http://backend:8089/api/`
  - [ ] 添加 `try_files $uri $uri/ /index.html` 支持 Vue Router history 模式

## docker-compose
- [ ] `docker-compose.yml`
  - [ ] `mysql` 服务：镜像 `mysql:8.0`，环境变量 MYSQL_ROOT_PASSWORD / MYSQL_DATABASE / MYSQL_USER / MYSQL_PASSWORD，挂载 init.sql + 数据卷，healthcheck
  - [ ] `backend` 服务：build Dockerfile.backend，依赖 mysql（condition: service_healthy），环境变量注入数据库连接 + Tushare token，内部端口 8089
  - [ ] `nginx` 服务：build Dockerfile.frontend，依赖 backend，端口映射 80:80
  - [ ] 网络：`vcash-net`（bridge）
  - [ ] 数据卷：`mysql-data`

## 环境变量
- [ ] `.env` 文件：MYSQL_ROOT_PASSWORD、MYSQL_USER、MYSQL_PASSWORD、MYSQL_DATABASE、TUSHARE_TOKEN
- [ ] `.env.example` 模板文件（不含真实密码）

## 忽略文件
- [ ] `.dockerignore`（node_modules、target、.git、dist）

## 验证
- [ ] `docker-compose up -d` 启动所有服务
- [ ] 等待 MySQL healthcheck 通过
- [ ] 浏览器访问 `http://localhost` 可见股票列表页
- [ ] 刷新功能正常工作（API 请求通过 Nginx 转发到后端）
- [ ] `docker-compose down -v` 清理所有资源
