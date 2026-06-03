package com.gongbotao.vcash.ingestion.domain;

import java.util.List;

public interface SyncLogRepository {
    void save(SyncLog log);
    List<SyncLog> findAll();
}
