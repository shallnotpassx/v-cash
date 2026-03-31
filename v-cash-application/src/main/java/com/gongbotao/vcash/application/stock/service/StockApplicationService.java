package com.gongbotao.vcash.application.stock.service;

import com.gongbotao.vcash.domain.stock.entity.StockBasic;
import com.gongbotao.vcash.domain.stock.repository.IStockRepository;
import com.gongbotao.vcash.domain.stock.service.StockDomainService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StockApplicationService {

    private final IStockRepository stockRepository;
    private final StockDomainService stockDomainService;

    public List<StockBasic> listFromDb(String market) {
        return stockRepository.findAll(market);
    }

    public StockBasic getByCode(String stockCode) {
        return stockRepository.findByCode(stockCode).orElse(null);
    }

    public void refreshStockList(String market) {
        List<StockBasic> stocks = stockDomainService.fetchStockList(market);
        if (!stocks.isEmpty()) {
            stockRepository.saveAll(stocks);
        }
    }
}