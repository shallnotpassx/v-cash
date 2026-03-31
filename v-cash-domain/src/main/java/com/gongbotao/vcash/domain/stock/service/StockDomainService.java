package com.gongbotao.vcash.domain.stock.service;

import com.gongbotao.vcash.domain.stock.entity.StockBasic;
import com.gongbotao.vcash.domain.stock.adapter.StockDataAdapter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class StockDomainService {

    private final List<StockDataAdapter> adapters;

    public StockDomainService(List<StockDataAdapter> adapters) {
        this.adapters = adapters;
    }

    public List<StockBasic> fetchStockList(String market) {
        for (StockDataAdapter adapter : adapters) {
            try {
                log.info("尝试从 {} 获取股票列表", adapter.getSourceName());
                List<StockBasic> list = adapter.getStockList(market);
                if (list != null && !list.isEmpty()) {
                    log.info("从 {} 获取到 {} 条股票数据", adapter.getSourceName(), list.size());
                    return list;
                }
            } catch (Exception e) {
                log.warn("从 {} 获取股票列表失败: {}", adapter.getSourceName(), e.getMessage());
            }
        }
        return List.of();
    }
}