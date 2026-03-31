package com.gongbotao.vcash.domain.announcement.repository;

import com.gongbotao.vcash.domain.announcement.entity.StockAnnouncement;

import java.util.List;

public interface IAnnouncementRepository {
    List<StockAnnouncement> findByStockId(Long stockId);
    void save(StockAnnouncement announcement);
    void saveAll(List<StockAnnouncement> announcements);
}