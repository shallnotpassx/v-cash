package com.gongbotao.vcash.ingestion.web;

import com.gongbotao.vcash.ingestion.application.DataSourceCoordinator;
import com.gongbotao.vcash.ingestion.application.SyncLogQueryService;
import com.gongbotao.vcash.ingestion.domain.SyncLog;
import com.gongbotao.vcash.shared.domain.StockIdentity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/sync")
public class SyncLogController {
    private final SyncLogQueryService queryService;
    private final DataSourceCoordinator coordinator;

    public SyncLogController(SyncLogQueryService queryService, DataSourceCoordinator coordinator) {
        this.queryService = queryService;
        this.coordinator = coordinator;
    }

    @GetMapping("/logs")
    public List<Map<String, Object>> listAll() {
        return queryService.listAll().stream().map(this::toLogMap).toList();
    }

    @PostMapping("/trigger")
    public Map<String, Object> triggerSync(@RequestParam String market,
                                            @RequestParam String code) {
        SyncLog log = coordinator.sync(new StockIdentity(market, code));
        return toLogMap(log);
    }

    private Map<String, Object> toLogMap(SyncLog log) {
        return Map.of(
                "id", log.id(),
                "market", log.stockIdentity().market(),
                "stockCode", log.stockIdentity().stockCode(),
                "source", log.source(),
                "status", log.status().name(),
                "recordCount", log.recordCount(),
                "message", log.message() != null ? log.message() : ""
        );
    }
}
