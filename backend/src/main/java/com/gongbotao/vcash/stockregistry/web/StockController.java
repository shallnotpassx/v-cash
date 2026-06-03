package com.gongbotao.vcash.stockregistry.web;

import com.gongbotao.vcash.stockregistry.application.StockQueryService;
import com.gongbotao.vcash.stockregistry.domain.Stock;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/stocks")
public class StockController {

    private final StockQueryService stockQueryService;

    public StockController(StockQueryService stockQueryService) {
        this.stockQueryService = stockQueryService;
    }

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

    @GetMapping(params = {"market", "code"})
    public Map<String, Object> findByMarketAndCode(@RequestParam String market,
                                                    @RequestParam("code") String stockCode) {
        return stockQueryService.findByMarketAndCode(market, stockCode)
                .map(this::toDetail)
                .orElseThrow(() -> new RuntimeException("Stock not found: " + market + ":" + stockCode));
    }

    private Map<String, Object> toSummary(Stock s) {
        return Map.of(
                "market", s.market(),
                "stockCode", s.stockCode(),
                "stockName", s.stockName()
        );
    }

    private Map<String, Object> toDetail(Stock s) {
        return Map.of(
                "market", s.market(),
                "stockCode", s.stockCode(),
                "stockName", s.stockName(),
                "listedStatus", s.listedStatus().name(),
                "listingDate", s.listingDate() != null ? s.listingDate().toString() : null,
                "exchange", s.exchange()
        );
    }
}
