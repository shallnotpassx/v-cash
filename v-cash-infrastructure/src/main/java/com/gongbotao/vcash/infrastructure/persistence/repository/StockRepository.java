package com.gongbotao.vcash.infrastructure.persistence.repository;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.gongbotao.vcash.domain.stock.entity.StockBasic;
import com.gongbotao.vcash.domain.stock.repository.IStockRepository;
import com.gongbotao.vcash.infrastructure.persistence.mapper.StockBasicMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class StockRepository implements IStockRepository {

    private final StockBasicMapper stockBasicMapper;

    @Override
    public List<StockBasic> findAll(String market) {
        LambdaQueryWrapper<StockBasic> wrapper = new LambdaQueryWrapper<>();
        if (market != null && !market.isEmpty()) {
            wrapper.eq(StockBasic::getMarket, market);
        }
        return stockBasicMapper.selectList(wrapper);
    }

    @Override
    public Optional<StockBasic> findByCode(String stockCode) {
        LambdaQueryWrapper<StockBasic> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(StockBasic::getStockCode, stockCode);
        return Optional.ofNullable(stockBasicMapper.selectOne(wrapper));
    }

    @Override
    public void save(StockBasic stock) {
        findByCode(stock.getStockCode()).ifPresent(exist -> stock.setId(exist.getId()));
        if (stock.getId() != null) {
            stockBasicMapper.updateById(stock);
        } else {
            stockBasicMapper.insert(stock);
        }
    }

    @Override
    public void saveAll(List<StockBasic> stocks) {
        for (StockBasic stock : stocks) {
            save(stock);
        }
    }
}