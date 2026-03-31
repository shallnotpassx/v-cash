package com.gongbotao.vcash.data.source.adapter;

import com.gongbotao.vcash.domain.entity.StockAnnouncement;

import java.util.List;

public interface AnnouncementAdapter {

    String getSourceName();

    List<StockAnnouncement> getAnnouncements(String stockCode);

    List<StockAnnouncement> getLatestAnnouncements(String stockCode, int limit);
}