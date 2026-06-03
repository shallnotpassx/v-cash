package com.gongbotao.vcash.stockregistry.infrastructure;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.gongbotao.vcash.stockregistry.domain.Stock;
import com.gongbotao.vcash.stockregistry.domain.StockRepository;
import com.gongbotao.vcash.shared.domain.StockIdentity;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class StockRepositoryImpl implements StockRepository {

    private final StockMapper stockMapper;

    public StockRepositoryImpl(StockMapper stockMapper) {
        this.stockMapper = stockMapper;
    }

    @Override
    public List<Stock> findAll() {
        return stockMapper.selectList(null).stream()
                .map(StockEntity::toDomain)
                .toList();
    }

    @Override
    public Optional<Stock> findByStockIdentity(StockIdentity identity) {
        StockEntity entity = stockMapper.selectOne(
                new LambdaQueryWrapper<StockEntity>()
                        .eq(StockEntity::getMarket, identity.market())
                        .eq(StockEntity::getStockCode, identity.stockCode())
        );
        return Optional.ofNullable(entity).map(StockEntity::toDomain);
    }

    @Override
    public List<Stock> search(String nameKeyword, Stock.ListedStatus status, int offset, int limit) {
        var qw = new LambdaQueryWrapper<StockEntity>();
        if (nameKeyword != null && !nameKeyword.isBlank()) {
            qw.like(StockEntity::getStockName, nameKeyword);
        }
        if (status != null) {
            qw.eq(StockEntity::getListedStatus, status.name());
        }
        qw.last("LIMIT " + offset + "," + limit);
        return stockMapper.selectList(qw).stream().map(StockEntity::toDomain).toList();
    }
}
