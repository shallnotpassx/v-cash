package com.gongbotao.vcash.ingestion.infrastructure;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.gongbotao.vcash.ingestion.domain.SyncLog;
import com.gongbotao.vcash.ingestion.domain.SyncResult;
import com.gongbotao.vcash.shared.domain.StockIdentity;
import lombok.Data;

@Data
@TableName("sync_log")
public class SyncLogEntity {
    @TableId
    private String id;
    private String market;
    private String stockCode;
    private String source;
    private String status;
    private int recordCount;
    private String message;

    public SyncLog toDomain() {
        return new SyncLog(new StockIdentity(market, stockCode), source,
                SyncResult.SyncStatus.valueOf(status), recordCount, message);
    }

    public static SyncLogEntity fromDomain(SyncLog log) {
        SyncLogEntity e = new SyncLogEntity();
        e.setId(log.id());
        e.setMarket(log.stockIdentity().market());
        e.setStockCode(log.stockIdentity().stockCode());
        e.setSource(log.source());
        e.setStatus(log.status().name());
        e.setRecordCount(log.recordCount());
        e.setMessage(log.message());
        return e;
    }
}
