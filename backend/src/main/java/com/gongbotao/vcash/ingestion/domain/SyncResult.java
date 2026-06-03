package com.gongbotao.vcash.ingestion.domain;

import java.time.LocalDateTime;

/** 同步结果值对象 */
public record SyncResult(
        SyncStatus status,
        int recordCount,
        String message,
        LocalDateTime completedAt
) {
    public enum SyncStatus { SUCCESS, PARTIAL, FAILED }
}
