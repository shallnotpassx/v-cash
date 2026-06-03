package com.gongbotao.vcash.announcement.web;

import com.gongbotao.vcash.announcement.application.AnnouncementQueryService;
import com.gongbotao.vcash.announcement.domain.Announcement;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/announcements")
public class AnnouncementController {
    private final AnnouncementQueryService service;

    public AnnouncementController(AnnouncementQueryService service) {
        this.service = service;
    }

    @GetMapping
    public List<Map<String, Object>> listAll(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String sourceSite,
            @RequestParam(required = false) LocalDate dateFrom,
            @RequestParam(required = false) LocalDate dateTo) {
        boolean hasFilter = keyword != null || sourceSite != null
                || dateFrom != null || dateTo != null;
        var list = hasFilter
                ? service.search(keyword, sourceSite, dateFrom, dateTo)
                : service.listAll();
        return list.stream().map(this::toSummary).toList();
    }

    @GetMapping(params = {"market", "code"})
    public List<Map<String, Object>> listByStock(@RequestParam String market,
                                                  @RequestParam String code) {
        return service.listByStock(market, code).stream().map(this::toSummary).toList();
    }

    private Map<String, Object> toSummary(Announcement a) {
        return Map.of(
                "id", a.id(),
                "market", a.stockIdentity().market(),
                "stockCode", a.stockIdentity().stockCode(),
                "title", a.title(),
                "publishDate", a.publishDate() != null ? a.publishDate().toString() : null,
                "sourceSite", a.sourceSite(),
                "sourceUrl", a.sourceUrl()
        );
    }
}
