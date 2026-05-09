## 1. Docker 文件

- [ ] 1.1 Dockerfile.backend：Maven 多阶段构建（maven:3.9 → temurin:17-jre）
- [ ] 1.2 Dockerfile.frontend：Node 构建 + Nginx 运行

## 2. Nginx 配置

- [ ] 2.1 nginx.conf：静态文件 + /api/ 反向代理 + Vue Router history 模式

## 3. docker-compose 编排

- [ ] 3.1 docker-compose.yml：mysql / backend / nginx 三服务 + 健康检查 + 网络 + 数据卷

## 4. 环境变量

- [ ] 4.1 .env 文件（MySQL 密码、Tushare token 等）
- [ ] 4.2 .env.example 模板文件
- [ ] 4.3 .dockerignore

## 5. 验证

- [ ] 5.1 docker-compose up -d 启动所有服务
- [ ] 5.2 浏览器访问确认前后端 MySQL 全通
- [ ] 5.3 docker-compose down -v 清理
