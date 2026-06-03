package com.gongbotao.vcash.financial.domain;

/**
 * PDF 人工辅助解析结果挂载点 —— 骨架期只定义数据结构契约。
 *
 * 当结构化数据拉取失败时，可通过公告 PDF 解析关键指标，
 * 解析结果存入此结构供人工审核，不直接写入 FinancialSnapshot。
 */
public record ManualParseResult(
        String sourceAnnouncementId,
        String market,
        String stockCode,
        String period,
        String rawRevenue,
        String rawNetProfit,
        String rawRoe,
        String rawEps,
        String notes
) {}
