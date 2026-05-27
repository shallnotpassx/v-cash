# Deployment

当前阶段采用本地优先的交付方式。

## 服务组成

- `mysql`：本地数据存储
- `backend`：Spring Boot 后端，默认端口 `8089`
- `frontend`：Vue 3 前端，默认端口 `8080`

## 本地启动

```bash
cd deployment
cp .env.example .env
docker compose up --build
```

## 说明

- 这是骨架级交付目录，目的是先把运行结构固定下来
- 后续切到 NAS 或 VPS 时，优先复用这套 Compose 结构
