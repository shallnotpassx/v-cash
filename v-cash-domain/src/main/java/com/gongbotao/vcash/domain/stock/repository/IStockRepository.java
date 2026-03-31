package com.gongbotao.vcash.domain.stock.repository;

import com.gongbotao.vcash.domain.stock.entity.StockBasic;

import java.util.List;
import java.util.Optional;

public interface IStockRepository {
    List<StockBasic> findAll(String market);
    Optional<StockBasic> findByCode(String stockCode);
    void save(StockBasic stock);
    void saveAll(List<StockBasic> stocks);
}