package com.gongbotao.vcash.announcement.domain;

/**
 * 公告来源站点常量。
 * A 股优先巨潮资讯，港股优先 HKEXnews。
 */
public final class AnnouncementSources {

    private AnnouncementSources() {}

    /** 巨潮资讯（A 股） */
    public static final String CNINFO = "cninfo";

    /** 香港交易所披露易（港股） */
    public static final String HKEXNEWS = "hkexnews";

    /** 东方财富（备用） */
    public static final String EASTMONEY = "eastmoney";
}
