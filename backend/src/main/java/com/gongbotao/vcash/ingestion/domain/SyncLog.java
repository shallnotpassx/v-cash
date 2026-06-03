package com.gongbotao.vcash.ingestion.domain;

import com.gongbotao.vcash.shared.domain.StockIdentity;

import java.util.Objects;
import java.util.UUID;

/** 同步日志实体 */
public class SyncLog {
    private final String id;
    private final StockIdentity stockIdentity;
    private final String source;
    private final SyncResult.SyncStatus status;
    private final int recordCount;
    private final String message;

    public SyncLog(StockIdentity stockIdentity, String source,
                   SyncResult.SyncStatus status, int recordCount, String message) {
        this.id = UUID.randomUUID().toString();
        this.stockIdentity = Objects.requireNonNull(stockIdentity);
        this.source = Objects.requireNonNull(source);
        this.status = Objects.requireNonNull(status);
        this.recordCount = recordCount;
        this.message = message;
    }

    public String id() { return id; }
    public StockIdentity stockIdentity() { return stockIdentity; }
    public String source() { return source; }
    public SyncResult.SyncStatus status() { return status; }
    public int recordCount() { return recordCount; }
    public String message() { return message; }
}
