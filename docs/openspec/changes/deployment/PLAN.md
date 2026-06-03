# T05 — NAS 环境 docker compose up 验证三服务启动正常

> **Current Task:** T05
> **Owning Review:** R01
> **Review Status:** open

**Goal:** 在 NAS Docker 环境中验证三服务编排可正常启动。

## 验证步骤

- [ ] **Step 1:** `cd deployment && cp .env.example .env && docker compose up -d` → 三容器 Running
- [ ] **Step 2:** `docker compose ps` → mysql / backend / frontend 均为 Up
- [ ] **Step 3:** `curl http://localhost:8089/api/stocks` → 返回 `[]`
- [ ] **Step 4:** `curl http://localhost:3000` → 返回前端 HTML
- [ ] **Step 5:** `docker compose down` → 清理
