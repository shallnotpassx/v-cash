package com.gongbotao.vcash.ingestion.application;

import com.gongbotao.vcash.ingestion.domain.SyncLog;
import com.gongbotao.vcash.ingestion.domain.SyncLogRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SyncLogQueryService {
    private final SyncLogRepository repo;

    public SyncLogQueryService(SyncLogRepository repo) {
        this.repo = repo;
    }

    public List<SyncLog> listAll() {
        return repo.findAll();
    }
}
