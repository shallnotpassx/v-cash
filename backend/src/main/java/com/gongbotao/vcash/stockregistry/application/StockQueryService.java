package com.gongbotao.vcash.stockregistry.application;

import com.gongbotao.vcash.stockregistry.domain.Stock;
import com.gongbotao.vcash.stockregistry.domain.StockRepository;
import com.gongbotao.vcash.shared.domain.StockIdentity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class StockQueryService {

    private final StockRepository stockRepository;

    public StockQueryService(StockRepository stockRepository) {
        this.stockRepository = stockRepository;
    }

    public List<Stock> listAll() {
        return stockRepository.findAll();
    }

    public Optional<Stock> findByMarketAndCode(String market, String stockCode) {
        return stockRepository.findByStockIdentity(new StockIdentity(market, stockCode));
    }

    public List<Stock> search(String nameKeyword, String statusStr, int page, int size) {
        Stock.ListedStatus status = null;
        if (statusStr != null && !statusStr.isBlank()) {
            status = Stock.ListedStatus.valueOf(statusStr.toUpperCase());
        }
        int offset = Math.max(0, page - 1) * size;
        return stockRepository.search(nameKeyword, status, offset, size);
    }
}
