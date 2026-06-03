# T05 — NAS 环境编译验证、单元测试通过、/api/announcements 冒烟

> **Current Task:** T05
> **Owning Review:** R01
> **Review Status:** open

**Goal:** 在 NAS Docker 环境中验证 announcement 全部代码可编译、测试通过、HTTP 端点可访问。

## 验证步骤

- [ ] **Step 1:** `cd backend && ./mvnw test -q` → 确认 AnnouncementControllerTest 2/2 通过
- [ ] **Step 2:** `curl http://localhost:8089/api/announcements` → 返回 `[]`
- [ ] **Step 3:** `curl "http://localhost:8089/api/announcements?sourceSite=cninfo"` → 返回空或匹配结果
