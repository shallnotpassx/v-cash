package com.gongbotao.vcash.stockregistry.domain;

import com.gongbotao.vcash.shared.domain.StockIdentity;

import java.util.List;
import java.util.Optional;

/**
 * 股票主数据仓储接口 —— 只定义领域层需要的查询语义。
 */
public interface StockRepository {

    List<Stock> findAll();

    Optional<Stock> findByStockIdentity(StockIdentity identity);

    List<Stock> search(String nameKeyword, Stock.ListedStatus status, int offset, int limit);
}
