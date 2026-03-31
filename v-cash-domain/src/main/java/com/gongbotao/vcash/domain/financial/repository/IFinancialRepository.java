package com.gongbotao.vcash.domain.financial.repository;

import com.gongbotao.vcash.domain.financial.entity.StockFinancial;

import java.util.List;
import java.util.Optional;

public interface IFinancialRepository {
    List<StockFinancial> findByStockId(Long stockId);
    Optional<StockFinancial> findLatestByStockId(Long stockId);
    void save(StockFinancial financial);
    void saveAll(List<StockFinancial> financials);
}