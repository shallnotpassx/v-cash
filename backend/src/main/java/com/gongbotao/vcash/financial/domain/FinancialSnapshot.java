package com.gongbotao.vcash.financial.domain;

import com.gongbotao.vcash.shared.domain.StockIdentity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

/** 财务筛选快照 */
public class FinancialSnapshot {
    private final StockIdentity stockIdentity;
    private final String latestPeriod;
    private final LocalDate updatedAt;
    private final BigDecimal revenue;
    private final BigDecimal netProfit;
    private final BigDecimal roe;
    private final BigDecimal eps;

    public FinancialSnapshot(StockIdentity stockIdentity, String latestPeriod,
                             LocalDate updatedAt, BigDecimal revenue,
                             BigDecimal netProfit, BigDecimal roe, BigDecimal eps) {
        this.stockIdentity = Objects.requireNonNull(stockIdentity);
        this.latestPeriod = Objects.requireNonNull(latestPeriod);
        this.updatedAt = Objects.requireNonNull(updatedAt);
        this.revenue = revenue;
        this.netProfit = netProfit;
        this.roe = roe;
        this.eps = eps;
    }

    public StockIdentity stockIdentity() { return stockIdentity; }
    public String latestPeriod() { return latestPeriod; }
    public LocalDate updatedAt() { return updatedAt; }
    public BigDecimal revenue() { return revenue; }
    public BigDecimal netProfit() { return netProfit; }
    public BigDecimal roe() { return roe; }
    public BigDecimal eps() { return eps; }
}
