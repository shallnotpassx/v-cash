# T05 — NAS 环境编译验证、单元测试通过、/api/stocks 冒烟

> **Current Task:** T05
> **Owning Review:** R01
> **Review Status:** open

**Goal:** 在 NAS Docker 环境中验证 stock-registry 全部代码可编译、测试通过、HTTP 端点可访问。

## 验证步骤

- [ ] **Step 1:** `cd backend && ./mvnw test -q` → 确认 BUILD SUCCESS，StockControllerTest 2/2 通过
- [ ] **Step 2:** `docker compose up -d` → 确认 backend 容器启动无报错
- [ ] **Step 3:** `curl http://localhost:8089/api/stocks` → 返回 `[]`
- [ ] **Step 4:** `curl "http://localhost:8089/api/stocks?name=浦发"` → 返回空或匹配结果
