package com.gongbotao.vcash.financial.web;

import com.gongbotao.vcash.financial.application.FinancialQueryService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/financial")
public class FinancialController {
    private final FinancialQueryService service;

    public FinancialController(FinancialQueryService service) {
        this.service = service;
    }

    @GetMapping("/history")
    public List<Map<String, Object>> history(@RequestParam String market,
                                              @RequestParam String code) {
        return service.history(market, code).stream().map(r -> Map.<String, Object>of(
                "market", r.stockIdentity().market(),
                "stockCode", r.stockIdentity().stockCode(),
                "period", r.period(),
                "reportDate", r.reportDate() != null ? r.reportDate().toString() : null,
                "revenue", r.revenue(), "netProfit", r.netProfit(),
                "roe", r.roe(), "eps", r.eps()
        )).toList();
    }

    @GetMapping("/snapshot")
    public List<Map<String, Object>> snapshots(
            @RequestParam(required = false) BigDecimal minRevenue,
            @RequestParam(required = false) BigDecimal minNetProfit,
            @RequestParam(required = false) BigDecimal minRoe,
            @RequestParam(required = false) BigDecimal minEps) {
        boolean hasFilter = minRevenue != null || minNetProfit != null
                || minRoe != null || minEps != null;
        var list = hasFilter
                ? service.filterSnapshots(minRevenue, minNetProfit, minRoe, minEps)
                : service.snapshots();
        return list.stream().map(s -> Map.<String, Object>of(
                "market", s.stockIdentity().market(),
                "stockCode", s.stockIdentity().stockCode(),
                "latestPeriod", s.latestPeriod(),
                "revenue", s.revenue(), "netProfit", s.netProfit(),
                "roe", s.roe(), "eps", s.eps()
        )).toList();
    }
}
