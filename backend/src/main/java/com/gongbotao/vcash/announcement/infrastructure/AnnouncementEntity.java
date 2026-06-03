package com.gongbotao.vcash.announcement.infrastructure;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.gongbotao.vcash.announcement.domain.Announcement;
import com.gongbotao.vcash.shared.domain.StockIdentity;
import lombok.Data;

import java.time.LocalDate;

@Data
@TableName("announcement")
public class AnnouncementEntity {
    @TableId
    private String id;
    private String market;
    private String stockCode;
    private String title;
    private LocalDate publishDate;
    private String sourceSite;
    private String sourceUrl;
    private String fileType;
    private String summary;

    public Announcement toDomain() {
        return new Announcement(id, new StockIdentity(market, stockCode),
                title, publishDate, sourceSite, sourceUrl, fileType, summary);
    }

    public static AnnouncementEntity fromDomain(Announcement a) {
        AnnouncementEntity e = new AnnouncementEntity();
        e.setId(a.id());
        e.setMarket(a.stockIdentity().market());
        e.setStockCode(a.stockIdentity().stockCode());
        e.setTitle(a.title());
        e.setPublishDate(a.publishDate());
        e.setSourceSite(a.sourceSite());
        e.setSourceUrl(a.sourceUrl());
        e.setFileType(a.fileType());
        e.setSummary(a.summary());
        return e;
    }
}
