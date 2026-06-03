package com.gongbotao.vcash.financial.domain;

import java.math.BigDecimal;
import java.util.Set;

/**
 * 核心财务指标口径与空值处理规则。
 * 字段命名与 FinancialRecord 一一对应。
 */
public final class FinancialIndicators {

    private FinancialIndicators() {}

    /** 营业收入（单位：元），空值 → 不参与该指标筛选 */
    public static final String REVENUE = "revenue";

    /** 净利润（单位：元），空值 → 不参与该指标筛选 */
    public static final String NET_PROFIT = "netProfit";

    /** 净资产收益率 ROE（%），空值 → 不参与该指标筛选 */
    public static final String ROE = "roe";

    /** 每股收益 EPS（元），空值 → 不参与该指标筛选 */
    public static final String EPS = "eps";

    /** 总资产（单位：元），仅历史表存储，快照不包含 */
    public static final String TOTAL_ASSETS = "totalAssets";

    /** 净资产（单位：元），仅历史表存储，快照不包含 */
    public static final String NET_ASSETS = "netAssets";

    /** 快照表中支持的筛选字段 */
    public static final Set<String> SNAPSHOT_FILTERS = Set.of(REVENUE, NET_PROFIT, ROE, EPS);

    /**
     * 空值处理规则：
     * - 数据库中 NULL 字段在 Java 层映射为 null
     * - 筛选时：若某条记录该字段为 null，该记录不参与该指标的比较筛选
     * - 排序时：null 值排在最后
     */
    public static boolean isPresent(BigDecimal value) {
        return value != null;
    }
}
