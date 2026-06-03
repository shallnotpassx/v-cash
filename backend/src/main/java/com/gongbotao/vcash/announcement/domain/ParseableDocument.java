package com.gongbotao.vcash.announcement.domain;

/**
 * PDF 人工解析文档入口 —— 骨架期只定义数据结构。
 * 不缓存 PDF 原文到本地，仅保留 URL 引用。
 */
public record ParseableDocument(
        String announcementId,
        String sourceUrl,
        String fileType,
        String notes
) {}
