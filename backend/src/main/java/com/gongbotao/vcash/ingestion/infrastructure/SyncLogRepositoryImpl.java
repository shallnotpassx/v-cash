package com.gongbotao.vcash.ingestion.infrastructure;

import com.gongbotao.vcash.ingestion.domain.SyncLog;
import com.gongbotao.vcash.ingestion.domain.SyncLogRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class SyncLogRepositoryImpl implements SyncLogRepository {
    private final SyncLogMapper mapper;

    public SyncLogRepositoryImpl(SyncLogMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public void save(SyncLog log) {
        mapper.insert(SyncLogEntity.fromDomain(log));
    }

    @Override
    public List<SyncLog> findAll() {
        return mapper.selectList(null).stream()
                .map(SyncLogEntity::toDomain)
                .toList();
    }
}
