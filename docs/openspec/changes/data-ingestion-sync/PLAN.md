# T05 — NAS 环境编译验证、单元测试通过、/api/sync/logs 冒烟

> **Current Task:** T05
> **Owning Review:** R01
> **Review Status:** open

**Goal:** 在 NAS Docker 环境中验证 ingestion 全部代码可编译、测试通过、HTTP 端点可访问。

## 验证步骤

- [ ] **Step 1:** `cd backend && ./mvnw test -q` → 确认 SyncLogControllerTest 通过
- [ ] **Step 2:** `curl http://localhost:8089/api/sync/logs` → 返回 `[]`
- [ ] **Step 3:** `curl -X POST "http://localhost:8089/api/sync/trigger?market=SH&code=600000"` → 返回同步日志 JSON（预期 status=FAILED，因无真实 data source adapter 注入）
