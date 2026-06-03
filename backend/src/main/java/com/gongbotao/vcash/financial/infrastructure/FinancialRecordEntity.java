package com.gongbotao.vcash.financial.infrastructure;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.gongbotao.vcash.financial.domain.FinancialRecord;
import com.gongbotao.vcash.shared.domain.StockIdentity;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@TableName("financial_record")
public class FinancialRecordEntity {
    @TableId private Long id;
    private String market;
    private String stockCode;
    private String period;
    private LocalDate reportDate;
    private BigDecimal revenue;
    private BigDecimal netProfit;
    private BigDecimal totalAssets;
    private BigDecimal netAssets;
    private BigDecimal roe;
    private BigDecimal eps;

    public FinancialRecord toDomain() {
        return new FinancialRecord(new StockIdentity(market, stockCode),
                period, reportDate, revenue, netProfit, totalAssets, netAssets, roe, eps);
    }

    public static FinancialRecordEntity fromDomain(FinancialRecord r) {
        FinancialRecordEntity e = new FinancialRecordEntity();
        e.setMarket(r.stockIdentity().market());
        e.setStockCode(r.stockIdentity().stockCode());
        e.setPeriod(r.period());
        e.setReportDate(r.reportDate());
        e.setRevenue(r.revenue());
        e.setNetProfit(r.netProfit());
        e.setTotalAssets(r.totalAssets());
        e.setNetAssets(r.netAssets());
        e.setRoe(r.roe());
        e.setEps(r.eps());
        return e;
    }
}
