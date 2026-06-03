package com.gongbotao.vcash.announcement.domain;

import com.gongbotao.vcash.shared.domain.StockIdentity;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface AnnouncementRepository {
    List<Announcement> findByStock(StockIdentity stockIdentity);
    List<Announcement> findAll();
    Optional<Announcement> findById(String id);
    List<Announcement> search(String keyword, String sourceSite,
                              LocalDate dateFrom, LocalDate dateTo);
}
