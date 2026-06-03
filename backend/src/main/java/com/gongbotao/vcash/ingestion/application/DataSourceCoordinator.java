package com.gongbotao.vcash.ingestion.application;

import com.gongbotao.vcash.ingestion.domain.DataSourceAdapter;
import com.gongbotao.vcash.ingestion.domain.SyncLog;
import com.gongbotao.vcash.ingestion.domain.SyncLogRepository;
import com.gongbotao.vcash.ingestion.domain.SyncResult;
import com.gongbotao.vcash.shared.domain.StockIdentity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 数据源兜底执行器 —— 按优先级顺序尝试多个 DataSourceAdapter，
 * 直到某个返回 SUCCESS 或 PARTIAL，或全部尝试完毕。
 */
@Service
public class DataSourceCoordinator {

    private final List<DataSourceAdapter> adapters;
    private final SyncLogRepository syncLogRepository;

    public DataSourceCoordinator(List<DataSourceAdapter> adapters, SyncLogRepository syncLogRepository) {
        this.adapters = adapters;
        this.syncLogRepository = syncLogRepository;
    }

    /** 手动触发同步：依次尝试 adapter，记录日志。 */
    public SyncLog sync(StockIdentity stock) {
        for (DataSourceAdapter adapter : adapters) {
            SyncResult result = adapter.sync(stock);
            if (result.status() == SyncResult.SyncStatus.SUCCESS
                    || result.status() == SyncResult.SyncStatus.PARTIAL) {
                SyncLog log = new SyncLog(stock, adapter.sourceName(),
                        result.status(), result.recordCount(), result.message());
                syncLogRepository.save(log);
                return log;
            }
        }
        SyncLog log = new SyncLog(stock, "all",
                SyncResult.SyncStatus.FAILED, 0,
                "All adapters failed for " + stock);
        syncLogRepository.save(log);
        return log;
    }
}
