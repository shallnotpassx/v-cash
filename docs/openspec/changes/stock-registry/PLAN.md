# T03 — 提供股票列表、基础搜索与详情查询接口骨架

> **Current Task:** T03
> **Owning Review:** R01
> **Review Status:** open

**Goal:** 增强 StockController，增加按名称模糊搜索、按状态筛选、分页参数支持。

---

### Step 1: 扩展 StockRepository 接口

**Modify:** `backend/src/main/java/com/gongbotao/vcash/stockregistry/domain/StockRepository.java`

添加 `search` 方法签名：

```java
List<Stock> search(String nameKeyword, Stock.ListedStatus status, int offset, int limit);
```

### Step 2: 扩展 MyBatis-Plus 实现

**Modify:** `backend/src/main/java/com/gongbotao/vcash/stockregistry/infrastructure/StockRepositoryImpl.java`

```java
@Override
public List<Stock> search(String nameKeyword, Stock.ListedStatus status, int offset, int limit) {
    var qw = new LambdaQueryWrapper<StockEntity>();
    if (nameKeyword != null && !nameKeyword.isBlank()) {
        qw.like(StockEntity::getStockName, nameKeyword);
    }
    if (status != null) {
        qw.eq(StockEntity::getListedStatus, status.name());
    }
    qw.last("LIMIT " + offset + "," + limit);
    return stockMapper.selectList(qw).stream().map(StockEntity::toDomain).toList();
}
```

### Step 3: 扩展 StockQueryService

**Modify:** `backend/src/main/java/com/gongbotao/vcash/stockregistry/application/StockQueryService.java`

```java
public List<Stock> search(String nameKeyword, String statusStr, int page, int size) {
    Stock.ListedStatus status = null;
    if (statusStr != null && !statusStr.isBlank()) {
        status = Stock.ListedStatus.valueOf(statusStr.toUpperCase());
    }
    int offset = Math.max(0, page - 1) * size;
    return stockRepository.search(nameKeyword, status, offset, size);
}
```

### Step 4: 增强 StockController

**Modify:** `backend/src/main/java/com/gongbotao/vcash/stockregistry/web/StockController.java`

在现有 `/api/stocks` 端点增加可选参数 `name`, `status`, `page`, `size`：

```java
@GetMapping
public List<Map<String, Object>> listAll(
        @RequestParam(required = false) String name,
        @RequestParam(required = false) String status,
        @RequestParam(defaultValue = "1") int page,
        @RequestParam(defaultValue = "20") int size) {
    return stockQueryService.search(name, status, page, size).stream()
            .map(this::toSummary)
            .toList();
}
```

保留原有 `findByMarketAndCode` 不变。

### 验证

```bash
cd backend && ./mvnw test -q
```
