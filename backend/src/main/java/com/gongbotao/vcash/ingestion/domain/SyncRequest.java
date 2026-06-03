package com.gongbotao.vcash.ingestion.domain;

import com.gongbotao.vcash.shared.domain.StockIdentity;

import java.time.LocalDateTime;
import java.util.Objects;

/** 同步请求值对象 */
public record SyncRequest(
        StockIdentity targetStock,
        String source,
        LocalDateTime requestedAt
) {
    public SyncRequest {
        Objects.requireNonNull(targetStock);
        Objects.requireNonNull(source);
        Objects.requireNonNull(requestedAt);
    }
}
