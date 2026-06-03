package com.gongbotao.vcash.financial.domain;

import com.gongbotao.vcash.shared.domain.StockIdentity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

/** 单条财务历史记录 */
public class FinancialRecord {
    private final StockIdentity stockIdentity;
    private final String period;
    private final LocalDate reportDate;
    private final BigDecimal revenue;
    private final BigDecimal netProfit;
    private final BigDecimal totalAssets;
    private final BigDecimal netAssets;
    private final BigDecimal roe;
    private final BigDecimal eps;

    public FinancialRecord(StockIdentity stockIdentity, String period, LocalDate reportDate,
                           BigDecimal revenue, BigDecimal netProfit, BigDecimal totalAssets,
                           BigDecimal netAssets, BigDecimal roe, BigDecimal eps) {
        this.stockIdentity = Objects.requireNonNull(stockIdentity);
        this.period = Objects.requireNonNull(period);
        this.reportDate = reportDate;
        this.revenue = revenue;
        this.netProfit = netProfit;
        this.totalAssets = totalAssets;
        this.netAssets = netAssets;
        this.roe = roe;
        this.eps = eps;
    }

    public StockIdentity stockIdentity() { return stockIdentity; }
    public String period() { return period; }
    public LocalDate reportDate() { return reportDate; }
    public BigDecimal revenue() { return revenue; }
    public BigDecimal netProfit() { return netProfit; }
    public BigDecimal totalAssets() { return totalAssets; }
    public BigDecimal netAssets() { return netAssets; }
    public BigDecimal roe() { return roe; }
    public BigDecimal eps() { return eps; }
}
