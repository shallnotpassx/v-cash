package com.gongbotao.vcash.announcement.domain;

import com.gongbotao.vcash.shared.domain.StockIdentity;

import java.time.LocalDate;
import java.util.Objects;

/** 公告领域实体 —— 只保存元数据和原文链接 */
public class Announcement {
    private final String id;
    private final StockIdentity stockIdentity;
    private final String title;
    private final LocalDate publishDate;
    private final String sourceSite;
    private final String sourceUrl;
    private final String fileType;
    private final String summary;

    public Announcement(String id, StockIdentity stockIdentity, String title,
                        LocalDate publishDate, String sourceSite, String sourceUrl,
                        String fileType, String summary) {
        this.id = Objects.requireNonNull(id);
        this.stockIdentity = Objects.requireNonNull(stockIdentity);
        this.title = Objects.requireNonNull(title);
        this.publishDate = publishDate;
        this.sourceSite = Objects.requireNonNull(sourceSite);
        this.sourceUrl = Objects.requireNonNull(sourceUrl);
        this.fileType = fileType != null ? fileType : "pdf";
        this.summary = summary;
    }

    public String id() { return id; }
    public StockIdentity stockIdentity() { return stockIdentity; }
    public String title() { return title; }
    public LocalDate publishDate() { return publishDate; }
    public String sourceSite() { return sourceSite; }
    public String sourceUrl() { return sourceUrl; }
    public String fileType() { return fileType; }
    public String summary() { return summary; }
}
