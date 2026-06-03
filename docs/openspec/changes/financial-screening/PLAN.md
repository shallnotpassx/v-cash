# T06 — NAS 环境编译验证、单元测试通过、财务端点冒烟

> **Current Task:** T06
> **Owning Review:** R01
> **Review Status:** open

**Goal:** 在 NAS Docker 环境中验证 financial 全部代码可编译、测试通过、HTTP 端点可访问。

## 验证步骤

- [ ] **Step 1:** `cd backend && ./mvnw test -q` → 确认 FinancialControllerTest 2/2 通过
- [ ] **Step 2:** `curl "http://localhost:8089/api/financial/history?market=SH&code=600000"` → 返回 `[]`
- [ ] **Step 3:** `curl http://localhost:8089/api/financial/snapshot` → 返回 `[]`
- [ ] **Step 4:** `curl "http://localhost:8089/api/financial/snapshot?minRoe=15"` → 返回空或匹配结果
