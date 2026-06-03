package com.gongbotao.vcash.financial.application;

import com.gongbotao.vcash.financial.domain.FinancialRecord;
import com.gongbotao.vcash.financial.domain.FinancialRepository;
import com.gongbotao.vcash.financial.domain.FinancialSnapshot;
import com.gongbotao.vcash.shared.domain.StockIdentity;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class FinancialQueryService {
    private final FinancialRepository repo;

    public FinancialQueryService(FinancialRepository repo) {
        this.repo = repo;
    }

    public List<FinancialRecord> history(String market, String code) {
        return repo.findHistoryByStock(new StockIdentity(market, code));
    }

    public List<FinancialSnapshot> snapshots() {
        return repo.findAllSnapshots();
    }

    public Optional<FinancialSnapshot> snapshotByStock(String market, String code) {
        return repo.findSnapshotByStock(new StockIdentity(market, code));
    }

    public List<FinancialSnapshot> filterSnapshots(BigDecimal minRevenue, BigDecimal minNetProfit,
                                                    BigDecimal minRoe, BigDecimal minEps) {
        return repo.findSnapshotsByFilter(minRevenue, minNetProfit, minRoe, minEps);
    }
}
