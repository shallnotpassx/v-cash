package com.gongbotao.vcash.announcement.application;

import com.gongbotao.vcash.announcement.domain.Announcement;
import com.gongbotao.vcash.announcement.domain.AnnouncementRepository;
import com.gongbotao.vcash.shared.domain.StockIdentity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class AnnouncementQueryService {
    private final AnnouncementRepository repo;

    public AnnouncementQueryService(AnnouncementRepository repo) {
        this.repo = repo;
    }

    public List<Announcement> listAll() {
        return repo.findAll();
    }

    public List<Announcement> listByStock(String market, String code) {
        return repo.findByStock(new StockIdentity(market, code));
    }

    public List<Announcement> search(String keyword, String sourceSite,
                                      LocalDate dateFrom, LocalDate dateTo) {
        return repo.search(keyword, sourceSite, dateFrom, dateTo);
    }
}
