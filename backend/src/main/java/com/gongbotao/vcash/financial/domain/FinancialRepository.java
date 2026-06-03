package com.gongbotao.vcash.financial.domain;

import com.gongbotao.vcash.shared.domain.StockIdentity;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface FinancialRepository {
    List<FinancialRecord> findHistoryByStock(StockIdentity stockIdentity);
    List<FinancialSnapshot> findAllSnapshots();
    Optional<FinancialSnapshot> findSnapshotByStock(StockIdentity stockIdentity);
    List<FinancialSnapshot> findSnapshotsByFilter(BigDecimal minRevenue, BigDecimal minNetProfit,
                                                   BigDecimal minRoe, BigDecimal minEps);
}
