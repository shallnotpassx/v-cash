package com.gongbotao.vcash.financial.infrastructure;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.gongbotao.vcash.financial.domain.FinancialSnapshot;
import com.gongbotao.vcash.shared.domain.StockIdentity;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@TableName("financial_snapshot")
public class FinancialSnapshotEntity {
    @TableId private Long id;
    private String market;
    private String stockCode;
    private String latestPeriod;
    private LocalDate updatedAt;
    private BigDecimal revenue;
    private BigDecimal netProfit;
    private BigDecimal roe;
    private BigDecimal eps;

    public FinancialSnapshot toDomain() {
        return new FinancialSnapshot(new StockIdentity(market, stockCode),
                latestPeriod, updatedAt, revenue, netProfit, roe, eps);
    }

    public static FinancialSnapshotEntity fromDomain(FinancialSnapshot s) {
        FinancialSnapshotEntity e = new FinancialSnapshotEntity();
        e.setMarket(s.stockIdentity().market());
        e.setStockCode(s.stockIdentity().stockCode());
        e.setLatestPeriod(s.latestPeriod());
        e.setUpdatedAt(s.updatedAt());
        e.setRevenue(s.revenue());
        e.setNetProfit(s.netProfit());
        e.setRoe(s.roe());
        e.setEps(s.eps());
        return e;
    }
}
