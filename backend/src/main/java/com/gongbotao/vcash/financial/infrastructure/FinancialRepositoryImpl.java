package com.gongbotao.vcash.financial.infrastructure;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.gongbotao.vcash.financial.domain.FinancialRecord;
import com.gongbotao.vcash.financial.domain.FinancialRepository;
import com.gongbotao.vcash.financial.domain.FinancialSnapshot;
import com.gongbotao.vcash.shared.domain.StockIdentity;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public class FinancialRepositoryImpl implements FinancialRepository {
    private final FinancialRecordMapper recordMapper;
    private final FinancialSnapshotMapper snapshotMapper;

    public FinancialRepositoryImpl(FinancialRecordMapper recordMapper,
                                   FinancialSnapshotMapper snapshotMapper) {
        this.recordMapper = recordMapper;
        this.snapshotMapper = snapshotMapper;
    }

    @Override
    public List<FinancialRecord> findHistoryByStock(StockIdentity id) {
        return recordMapper.selectList(new LambdaQueryWrapper<FinancialRecordEntity>()
                .eq(FinancialRecordEntity::getMarket, id.market())
                .eq(FinancialRecordEntity::getStockCode, id.stockCode()))
                .stream().map(FinancialRecordEntity::toDomain).toList();
    }

    @Override
    public List<FinancialSnapshot> findAllSnapshots() {
        return snapshotMapper.selectList(null).stream()
                .map(FinancialSnapshotEntity::toDomain).toList();
    }

    @Override
    public Optional<FinancialSnapshot> findSnapshotByStock(StockIdentity id) {
        FinancialSnapshotEntity e = snapshotMapper.selectOne(
                new LambdaQueryWrapper<FinancialSnapshotEntity>()
                        .eq(FinancialSnapshotEntity::getMarket, id.market())
                        .eq(FinancialSnapshotEntity::getStockCode, id.stockCode()));
        return Optional.ofNullable(e).map(FinancialSnapshotEntity::toDomain);
    }

    @Override
    public List<FinancialSnapshot> findSnapshotsByFilter(BigDecimal minRevenue, BigDecimal minNetProfit,
                                                          BigDecimal minRoe, BigDecimal minEps) {
        var qw = new LambdaQueryWrapper<FinancialSnapshotEntity>();
        if (minRevenue != null) qw.ge(FinancialSnapshotEntity::getRevenue, minRevenue);
        if (minNetProfit != null) qw.ge(FinancialSnapshotEntity::getNetProfit, minNetProfit);
        if (minRoe != null) qw.ge(FinancialSnapshotEntity::getRoe, minRoe);
        if (minEps != null) qw.ge(FinancialSnapshotEntity::getEps, minEps);
        return snapshotMapper.selectList(qw).stream()
                .map(FinancialSnapshotEntity::toDomain).toList();
    }
}
