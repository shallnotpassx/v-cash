# T01 — 设计公告元数据领域模型与仓储接口

> **Current Task:** T01
> **Owning Review:** R01
> **Review Status:** pending

**Goal:** 在 `announcement` 上下文中搭建 DDD 四层骨架，定义 Announcement 领域对象、AnnouncementRepository 接口、以及 `GET /api/announcements` 入口。

**Architecture:** 依赖 `shared.domain.StockIdentity` 关联股票。公告只存元数据和原文链接，不做本地 PDF 缓存。

**Tech Stack:** Java 17, Spring Boot 3.2.5, MyBatis-Plus 3.5.5, Lombok, JUnit 5 + MockMvc

---

## 文件结构

| 层 | 文件 | 职责 |
|---|---|---|
| announcement/domain | `Announcement.java` | 公告领域实体 |
| announcement/domain | `AnnouncementRepository.java` | 仓储接口 |
| announcement/infrastructure | `AnnouncementEntity.java` | MyBatis-Plus 实体 |
| announcement/infrastructure | `AnnouncementMapper.java` | MyBatis-Plus BaseMapper |
| announcement/infrastructure | `AnnouncementRepositoryImpl.java` | 仓储实现 |
| announcement/application | `AnnouncementQueryService.java` | 查询服务 |
| announcement/web | `AnnouncementController.java` | REST Controller |
| — (test) | `AnnouncementControllerTest.java` | MockMvc 测试 |

---

### Task 1.1: 创建 Announcement 领域实体

**Files:**
- Create: `backend/src/main/java/com/gongbotao/announcement/domain/Announcement.java`

```java
package com.gongbotao.announcement.domain;

import com.gongbotao.shared.domain.StockIdentity;
import java.time.LocalDate;
import java.util.Objects;

/**
 * 公告领域实体 —— 只保存元数据和原文链接。
 * 一期不缓存 PDF 原文，仅记录 URL。
 */
public class Announcement {
    private final String id;            // 内部 ID
    private final StockIdentity stockIdentity;
    private final String title;
    private final LocalDate publishDate;
    private final String sourceSite;    // "cninfo", "hkexnews"
    private final String sourceUrl;     // 原文链接
    private final String fileType;      // "pdf", "html"
    private final String summary;       // 摘要（可为空）

    public Announcement(String id, StockIdentity stockIdentity, String title,
                        LocalDate publishDate, String sourceSite, String sourceUrl,
                        String fileType, String summary) {
        this.id = Objects.requireNonNull(id);
        this.stockIdentity = Objects.requireNonNull(stockIdentity);
        this.title = Objects.requireNonNull(title);
        this.publishDate = publishDate;
        this.sourceSite = Objects.requireNonNull(sourceSite);
        this.sourceUrl = Objects.requireNonNull(sourceUrl);
        this.fileType = fileType != null ? fileType : "pdf";
        this.summary = summary;
    }

    public String id() { return id; }
    public StockIdentity stockIdentity() { return stockIdentity; }
    public String title() { return title; }
    public LocalDate publishDate() { return publishDate; }
    public String sourceSite() { return sourceSite; }
    public String sourceUrl() { return sourceUrl; }
    public String fileType() { return fileType; }
    public String summary() { return summary; }
}
```

- [ ] **验证编译**

```bash
cd backend && ./mvnw compile -q
```

---

### Task 1.2: 创建 AnnouncementRepository 接口

**Files:**
- Create: `backend/src/main/java/com/gongbotao/announcement/domain/AnnouncementRepository.java`

```java
package com.gongbotao.announcement.domain;

import com.gongbotao.shared.domain.StockIdentity;
import java.util.List;
import java.util.Optional;

public interface AnnouncementRepository {
    List<Announcement> findByStock(StockIdentity stockIdentity);
    List<Announcement> findAll();
    Optional<Announcement> findById(String id);
}
```

- [ ] **验证编译**

```bash
cd backend && ./mvnw compile -q
```

---

### Task 1.3: 创建 MyBatis-Plus 持久化

**Files:**
- Create: `backend/src/main/java/com/gongbotao/announcement/infrastructure/AnnouncementEntity.java`
- Create: `backend/src/main/java/com/gongbotao/announcement/infrastructure/AnnouncementMapper.java`
- Create: `backend/src/main/java/com/gongbotao/announcement/infrastructure/AnnouncementRepositoryImpl.java`

- [ ] **Step 1: AnnouncementEntity**

```java
package com.gongbotao.announcement.infrastructure;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.gongbotao.announcement.domain.Announcement;
import com.gongbotao.shared.domain.StockIdentity;
import lombok.Data;
import java.time.LocalDate;

@Data
@TableName("announcement")
public class AnnouncementEntity {
    @TableId
    private String id;
    private String market;
    private String stockCode;
    private String title;
    private LocalDate publishDate;
    private String sourceSite;
    private String sourceUrl;
    private String fileType;
    private String summary;

    public Announcement toDomain() {
        return new Announcement(id, new StockIdentity(market, stockCode),
                title, publishDate, sourceSite, sourceUrl, fileType, summary);
    }
    public static AnnouncementEntity fromDomain(Announcement a) {
        AnnouncementEntity e = new AnnouncementEntity();
        e.setId(a.id());
        e.setMarket(a.stockIdentity().market());
        e.setStockCode(a.stockIdentity().stockCode());
        e.setTitle(a.title());
        e.setPublishDate(a.publishDate());
        e.setSourceSite(a.sourceSite());
        e.setSourceUrl(a.sourceUrl());
        e.setFileType(a.fileType());
        e.setSummary(a.summary());
        return e;
    }
}
```

- [ ] **Step 2: AnnouncementMapper**

```java
package com.gongbotao.announcement.infrastructure;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface AnnouncementMapper extends BaseMapper<AnnouncementEntity> {}
```

- [ ] **Step 3: AnnouncementRepositoryImpl**

```java
package com.gongbotao.announcement.infrastructure;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.gongbotao.announcement.domain.Announcement;
import com.gongbotao.announcement.domain.AnnouncementRepository;
import com.gongbotao.shared.domain.StockIdentity;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public class AnnouncementRepositoryImpl implements AnnouncementRepository {
    private final AnnouncementMapper mapper;
    public AnnouncementRepositoryImpl(AnnouncementMapper mapper) { this.mapper = mapper; }

    @Override
    public List<Announcement> findByStock(StockIdentity id) {
        return mapper.selectList(new LambdaQueryWrapper<AnnouncementEntity>()
                .eq(AnnouncementEntity::getMarket, id.market())
                .eq(AnnouncementEntity::getStockCode, id.stockCode()))
                .stream().map(AnnouncementEntity::toDomain).toList();
    }

    @Override
    public List<Announcement> findAll() {
        return mapper.selectList(null).stream()
                .map(AnnouncementEntity::toDomain).toList();
    }

    @Override
    public Optional<Announcement> findById(String id) {
        return Optional.ofNullable(mapper.selectById(id))
                .map(AnnouncementEntity::toDomain);
    }
}
```

- [ ] **Step 4: 验证编译**

```bash
cd backend && ./mvnw compile -q
```

---

### Task 1.4: 创建 AnnouncementQueryService + AnnouncementController + 测试

**Files:**
- Create: `backend/src/main/java/com/gongbotao/announcement/application/AnnouncementQueryService.java`
- Create: `backend/src/main/java/com/gongbotao/announcement/web/AnnouncementController.java`
- Create: `backend/src/test/java/com/gongbotao/announcement/web/AnnouncementControllerTest.java`

- [ ] **Step 1: AnnouncementQueryService**

```java
package com.gongbotao.announcement.application;

import com.gongbotao.announcement.domain.Announcement;
import com.gongbotao.announcement.domain.AnnouncementRepository;
import com.gongbotao.shared.domain.StockIdentity;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class AnnouncementQueryService {
    private final AnnouncementRepository repo;
    public AnnouncementQueryService(AnnouncementRepository repo) { this.repo = repo; }

    public List<Announcement> listAll() { return repo.findAll(); }
    public List<Announcement> listByStock(String market, String code) {
        return repo.findByStock(new StockIdentity(market, code));
    }
}
```

- [ ] **Step 2: AnnouncementController**

```java
package com.gongbotao.announcement.web;

import com.gongbotao.announcement.application.AnnouncementQueryService;
import com.gongbotao.announcement.domain.Announcement;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/announcements")
public class AnnouncementController {
    private final AnnouncementQueryService service;
    public AnnouncementController(AnnouncementQueryService service) { this.service = service; }

    @GetMapping
    public List<Map<String, Object>> listAll() {
        return service.listAll().stream().map(this::toSummary).toList();
    }

    @GetMapping(params = {"market", "code"})
    public List<Map<String, Object>> listByStock(@RequestParam String market,
                                                  @RequestParam String code) {
        return service.listByStock(market, code).stream().map(this::toSummary).toList();
    }

    private Map<String, Object> toSummary(Announcement a) {
        return Map.of(
                "id", a.id(),
                "market", a.stockIdentity().market(),
                "stockCode", a.stockIdentity().stockCode(),
                "title", a.title(),
                "publishDate", a.publishDate() != null ? a.publishDate().toString() : null,
                "sourceSite", a.sourceSite(),
                "sourceUrl", a.sourceUrl()
        );
    }
}
```

- [ ] **Step 3: AnnouncementControllerTest**

```java
package com.gongbotao.announcement.web;

import com.gongbotao.announcement.application.AnnouncementQueryService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.bean.annotation.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import java.util.Collections;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.hasSize;

@WebMvcTest(AnnouncementController.class)
class AnnouncementControllerTest {
    @Autowired private MockMvc mockMvc;
    @MockitoBean private AnnouncementQueryService service;

    @Test
    void listAll_shouldReturnEmptyList() throws Exception {
        given(service.listAll()).willReturn(Collections.emptyList());
        mockMvc.perform(get("/api/announcements"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    void listByStock_shouldReturnEmptyList() throws Exception {
        given(service.listByStock("SH", "600000")).willReturn(Collections.emptyList());
        mockMvc.perform(get("/api/announcements?market=SH&code=600000"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }
}
```

- [ ] **Step 4: 运行测试**

```bash
cd backend && ./mvnw test -q
```

预期：BUILD SUCCESS

---

### Task 1.5: 提交

```bash
git add backend/src/main/java/com/gongbotao/announcement/
git add backend/src/test/java/com/gongbotao/announcement/
git add docs/openspec/changes/announcement-center/PLAN.md
git commit -m "feat(announcement-center): T01 announcement domain models, repository, and REST skeleton"
```
