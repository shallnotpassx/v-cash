# T01 — 设计财务历史表与财务筛选快照表的领域模型

> **Current Task:** T01
> **Owning Review:** R01
> **Review Status:** pending

**Goal:** 在 `financial` 上下文中搭建 DDD 四层骨架，定义 FinancialRecord / FinancialSnapshot 领域对象、FinancialRepository 接口、以及 `GET /api/financial/history` 和 `GET /api/financial/snapshot` 入口。

**Architecture:** 依赖 `shared.domain.StockIdentity` 关联股票。财务历史表存原始记录，筛选快照表存预计算指标供多条件筛选。

**Tech Stack:** Java 17, Spring Boot 3.2.5, MyBatis-Plus 3.5.5, Lombok, JUnit 5 + MockMvc

---

## 文件结构

| 层 | 文件 | 职责 |
|---|---|---|
| financial/domain | `FinancialRecord.java` | 单条财务历史记录（period, 各项指标） |
| financial/domain | `FinancialSnapshot.java` | 筛选快照（预计算指标，关联 stock） |
| financial/domain | `FinancialRepository.java` | 仓储接口 |
| financial/infrastructure | `FinancialRecordEntity.java` | 财务历史表实体 |
| financial/infrastructure | `FinancialSnapshotEntity.java` | 筛选快照表实体 |
| financial/infrastructure | `FinancialMapper.java` | MyBatis-Plus BaseMapper（两张表） |
| financial/infrastructure | `FinancialRepositoryImpl.java` | 仓储实现 |
| financial/application | `FinancialQueryService.java` | 查询服务 |
| financial/web | `FinancialController.java` | REST Controller |
| — (test) | `FinancialControllerTest.java` | MockMvc 集成测试 |

---

### Task 1.1: 创建 FinancialRecord + FinancialSnapshot 领域对象

**Files:**
- Create: `backend/src/main/java/com/gongbotao/financial/domain/FinancialRecord.java`
- Create: `backend/src/main/java/com/gongbotao/financial/domain/FinancialSnapshot.java`

- [ ] **Step 1: FinancialRecord（财务历史）**

```java
package com.gongbotao.financial.domain;

import com.gongbotao.shared.domain.StockIdentity;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

/**
 * 单条财务历史记录 —— 原始财报数据，不做预计算。
 * 一期字段从简：营收、净利润、总资产、净资产、ROE、EPS。
 */
public class FinancialRecord {
    private final StockIdentity stockIdentity;
    private final String period;        // e.g. "2024Q4", "2024FY"
    private final LocalDate reportDate;
    private final BigDecimal revenue;
    private final BigDecimal netProfit;
    private final BigDecimal totalAssets;
    private final BigDecimal netAssets;
    private final BigDecimal roe;       // 净资产收益率 %
    private final BigDecimal eps;       // 每股收益

    public FinancialRecord(StockIdentity stockIdentity, String period, LocalDate reportDate,
                           BigDecimal revenue, BigDecimal netProfit, BigDecimal totalAssets,
                           BigDecimal netAssets, BigDecimal roe, BigDecimal eps) {
        this.stockIdentity = Objects.requireNonNull(stockIdentity);
        this.period = Objects.requireNonNull(period);
        this.reportDate = reportDate;
        this.revenue = revenue;
        this.netProfit = netProfit;
        this.totalAssets = totalAssets;
        this.netAssets = netAssets;
        this.roe = roe;
        this.eps = eps;
    }

    public StockIdentity stockIdentity() { return stockIdentity; }
    public String period() { return period; }
    public LocalDate reportDate() { return reportDate; }
    public BigDecimal revenue() { return revenue; }
    public BigDecimal netProfit() { return netProfit; }
    public BigDecimal totalAssets() { return totalAssets; }
    public BigDecimal netAssets() { return netAssets; }
    public BigDecimal roe() { return roe; }
    public BigDecimal eps() { return eps; }
}
```

- [ ] **Step 2: FinancialSnapshot（筛选快照）**

```java
package com.gongbotao.financial.domain;

import com.gongbotao.shared.domain.StockIdentity;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

/**
 * 财务筛选快照 —— 从 FinancialRecord 预计算得出，供多指标组合筛选。
 * 每条快照对应一只股票的最新一期财务摘要。
 */
public class FinancialSnapshot {
    private final StockIdentity stockIdentity;
    private final String latestPeriod;
    private final LocalDate updatedAt;
    private final BigDecimal revenue;
    private final BigDecimal netProfit;
    private final BigDecimal roe;
    private final BigDecimal eps;

    public FinancialSnapshot(StockIdentity stockIdentity, String latestPeriod,
                             LocalDate updatedAt, BigDecimal revenue,
                             BigDecimal netProfit, BigDecimal roe, BigDecimal eps) {
        this.stockIdentity = Objects.requireNonNull(stockIdentity);
        this.latestPeriod = Objects.requireNonNull(latestPeriod);
        this.updatedAt = Objects.requireNonNull(updatedAt);
        this.revenue = revenue;
        this.netProfit = netProfit;
        this.roe = roe;
        this.eps = eps;
    }

    public StockIdentity stockIdentity() { return stockIdentity; }
    public String latestPeriod() { return latestPeriod; }
    public LocalDate updatedAt() { return updatedAt; }
    public BigDecimal revenue() { return revenue; }
    public BigDecimal netProfit() { return netProfit; }
    public BigDecimal roe() { return roe; }
    public BigDecimal eps() { return eps; }
}
```

- [ ] **Step 3: 验证编译**

```bash
cd backend && ./mvnw compile -q
```

---

### Task 1.2: 创建 FinancialRepository 接口

**Files:**
- Create: `backend/src/main/java/com/gongbotao/financial/domain/FinancialRepository.java`

```java
package com.gongbotao.financial.domain;

import com.gongbotao.shared.domain.StockIdentity;
import java.util.List;
import java.util.Optional;

public interface FinancialRepository {
    /** 按股票查历史财务记录 */
    List<FinancialRecord> findHistoryByStock(StockIdentity stockIdentity);
    /** 全部快照（骨架期不做筛选条件，后续 T03 扩展） */
    List<FinancialSnapshot> findAllSnapshots();
    /** 按股票查最新快照 */
    Optional<FinancialSnapshot> findSnapshotByStock(StockIdentity stockIdentity);
}
```

- [ ] **验证编译**

```bash
cd backend && ./mvnw compile -q
```

---

### Task 1.3: 创建 MyBatis-Plus 持久化

**Files:**
- Create: `backend/src/main/java/com/gongbotao/financial/infrastructure/FinancialRecordEntity.java`
- Create: `backend/src/main/java/com/gongbotao/financial/infrastructure/FinancialSnapshotEntity.java`
- Create: `backend/src/main/java/com/gongbotao/financial/infrastructure/FinancialMapper.java`
- Create: `backend/src/main/java/com/gongbotao/financial/infrastructure/FinancialRepositoryImpl.java`

- [ ] **Step 1: FinancialRecordEntity**

```java
package com.gongbotao.financial.infrastructure;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.gongbotao.financial.domain.FinancialRecord;
import com.gongbotao.shared.domain.StockIdentity;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@TableName("financial_record")
public class FinancialRecordEntity {
    @TableId private Long id;
    private String market;
    private String stockCode;
    private String period;
    private LocalDate reportDate;
    private BigDecimal revenue;
    private BigDecimal netProfit;
    private BigDecimal totalAssets;
    private BigDecimal netAssets;
    private BigDecimal roe;
    private BigDecimal eps;

    public FinancialRecord toDomain() {
        return new FinancialRecord(new StockIdentity(market, stockCode),
                period, reportDate, revenue, netProfit, totalAssets, netAssets, roe, eps);
    }
    public static FinancialRecordEntity fromDomain(FinancialRecord r) {
        FinancialRecordEntity e = new FinancialRecordEntity();
        e.setMarket(r.stockIdentity().market());
        e.setStockCode(r.stockIdentity().stockCode());
        e.setPeriod(r.period());
        e.setReportDate(r.reportDate());
        e.setRevenue(r.revenue());
        e.setNetProfit(r.netProfit());
        e.setTotalAssets(r.totalAssets());
        e.setNetAssets(r.netAssets());
        e.setRoe(r.roe());
        e.setEps(r.eps());
        return e;
    }
}
```

- [ ] **Step 2: FinancialSnapshotEntity**

```java
package com.gongbotao.financial.infrastructure;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.gongbotao.financial.domain.FinancialSnapshot;
import com.gongbotao.shared.domain.StockIdentity;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@TableName("financial_snapshot")
public class FinancialSnapshotEntity {
    @TableId private Long id;
    private String market;
    private String stockCode;
    private String latestPeriod;
    private LocalDate updatedAt;
    private BigDecimal revenue;
    private BigDecimal netProfit;
    private BigDecimal roe;
    private BigDecimal eps;

    public FinancialSnapshot toDomain() {
        return new FinancialSnapshot(new StockIdentity(market, stockCode),
                latestPeriod, updatedAt, revenue, netProfit, roe, eps);
    }
    public static FinancialSnapshotEntity fromDomain(FinancialSnapshot s) {
        FinancialSnapshotEntity e = new FinancialSnapshotEntity();
        e.setMarket(s.stockIdentity().market());
        e.setStockCode(s.stockIdentity().stockCode());
        e.setLatestPeriod(s.latestPeriod());
        e.setUpdatedAt(s.updatedAt());
        e.setRevenue(s.revenue());
        e.setNetProfit(s.netProfit());
        e.setRoe(s.roe());
        e.setEps(s.eps());
        return e;
    }
}
```

- [ ] **Step 3: FinancialMapper**

```java
package com.gongbotao.financial.infrastructure;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface FinancialRecordMapper extends BaseMapper<FinancialRecordEntity> {}
```

```java
package com.gongbotao.financial.infrastructure;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface FinancialSnapshotMapper extends BaseMapper<FinancialSnapshotEntity> {}
```

- [ ] **Step 4: FinancialRepositoryImpl**

```java
package com.gongbotao.financial.infrastructure;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.gongbotao.financial.domain.FinancialRecord;
import com.gongbotao.financial.domain.FinancialRepository;
import com.gongbotao.financial.domain.FinancialSnapshot;
import com.gongbotao.shared.domain.StockIdentity;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public class FinancialRepositoryImpl implements FinancialRepository {
    private final FinancialRecordMapper recordMapper;
    private final FinancialSnapshotMapper snapshotMapper;

    public FinancialRepositoryImpl(FinancialRecordMapper recordMapper,
                                   FinancialSnapshotMapper snapshotMapper) {
        this.recordMapper = recordMapper;
        this.snapshotMapper = snapshotMapper;
    }

    @Override
    public List<FinancialRecord> findHistoryByStock(StockIdentity id) {
        return recordMapper.selectList(new LambdaQueryWrapper<FinancialRecordEntity>()
                .eq(FinancialRecordEntity::getMarket, id.market())
                .eq(FinancialRecordEntity::getStockCode, id.stockCode()))
                .stream().map(FinancialRecordEntity::toDomain).toList();
    }

    @Override
    public List<FinancialSnapshot> findAllSnapshots() {
        return snapshotMapper.selectList(null).stream()
                .map(FinancialSnapshotEntity::toDomain).toList();
    }

    @Override
    public Optional<FinancialSnapshot> findSnapshotByStock(StockIdentity id) {
        FinancialSnapshotEntity e = snapshotMapper.selectOne(
                new LambdaQueryWrapper<FinancialSnapshotEntity>()
                        .eq(FinancialSnapshotEntity::getMarket, id.market())
                        .eq(FinancialSnapshotEntity::getStockCode, id.stockCode()));
        return Optional.ofNullable(e).map(FinancialSnapshotEntity::toDomain);
    }
}
```

- [ ] **Step 5: 验证编译**

```bash
cd backend && ./mvnw compile -q
```

---

### Task 1.4: 创建 FinancialQueryService + FinancialController + 测试

**Files:**
- Create: `backend/src/main/java/com/gongbotao/financial/application/FinancialQueryService.java`
- Create: `backend/src/main/java/com/gongbotao/financial/web/FinancialController.java`
- Create: `backend/src/test/java/com/gongbotao/financial/web/FinancialControllerTest.java`

- [ ] **Step 1: FinancialQueryService**

```java
package com.gongbotao.financial.application;

import com.gongbotao.financial.domain.FinancialRecord;
import com.gongbotao.financial.domain.FinancialRepository;
import com.gongbotao.financial.domain.FinancialSnapshot;
import com.gongbotao.shared.domain.StockIdentity;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class FinancialQueryService {
    private final FinancialRepository repo;
    public FinancialQueryService(FinancialRepository repo) { this.repo = repo; }

    public List<FinancialRecord> history(String market, String code) {
        return repo.findHistoryByStock(new StockIdentity(market, code));
    }
    public List<FinancialSnapshot> snapshots() { return repo.findAllSnapshots(); }
    public Optional<FinancialSnapshot> snapshotByStock(String market, String code) {
        return repo.findSnapshotByStock(new StockIdentity(market, code));
    }
}
```

- [ ] **Step 2: FinancialController**

```java
package com.gongbotao.financial.web;

import com.gongbotao.financial.application.FinancialQueryService;
import com.gongbotao.financial.domain.FinancialRecord;
import com.gongbotao.financial.domain.FinancialSnapshot;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/financial")
public class FinancialController {
    private final FinancialQueryService service;
    public FinancialController(FinancialQueryService service) { this.service = service; }

    @GetMapping("/history")
    public List<Map<String, Object>> history(@RequestParam String market,
                                              @RequestParam String code) {
        return service.history(market, code).stream().map(r -> Map.of(
                "market", r.stockIdentity().market(),
                "stockCode", r.stockIdentity().stockCode(),
                "period", r.period(),
                "reportDate", r.reportDate() != null ? r.reportDate().toString() : null,
                "revenue", r.revenue(), "netProfit", r.netProfit(),
                "roe", r.roe(), "eps", r.eps()
        )).toList();
    }

    @GetMapping("/snapshot")
    public List<Map<String, Object>> snapshots() {
        return service.snapshots().stream().map(s -> Map.of(
                "market", s.stockIdentity().market(),
                "stockCode", s.stockIdentity().stockCode(),
                "latestPeriod", s.latestPeriod(),
                "revenue", s.revenue(), "netProfit", s.netProfit(),
                "roe", s.roe(), "eps", s.eps()
        )).toList();
    }
}
```

- [ ] **Step 3: FinancialControllerTest**

```java
package com.gongbotao.financial.web;

import com.gongbotao.financial.application.FinancialQueryService;
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

@WebMvcTest(FinancialController.class)
class FinancialControllerTest {
    @Autowired private MockMvc mockMvc;
    @MockitoBean private FinancialQueryService service;

    @Test
    void history_shouldReturnEmptyList() throws Exception {
        given(service.history("SH", "600000")).willReturn(Collections.emptyList());
        mockMvc.perform(get("/api/financial/history?market=SH&code=600000"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    void snapshots_shouldReturnEmptyList() throws Exception {
        given(service.snapshots()).willReturn(Collections.emptyList());
        mockMvc.perform(get("/api/financial/snapshot"))
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
git add backend/src/main/java/com/gongbotao/financial/
git add backend/src/test/java/com/gongbotao/financial/
git add docs/openspec/changes/financial-screening/PLAN.md
git commit -m "feat(financial-screening): T01 financial domain models, repository, and REST skeleton"
```
