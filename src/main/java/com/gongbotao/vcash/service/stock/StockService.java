package com.gongbotao.vcash.service.stock;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.gongbotao.vcash.data.source.adapter.StockDataAdapter;
import com.gongbotao.vcash.data.storage.mapper.StockBasicMapper;
import com.gongbotao.vcash.domain.entity.StockBasic;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class StockService {

    private final StockBasicMapper stockBasicMapper;
    private final List<StockDataAdapter> adapters;

    public List<StockBasic> listFromDb(String market) {
        LambdaQueryWrapper<StockBasic> wrapper = new LambdaQueryWrapper<>();
        if (market != null && !market.isEmpty()) {
            wrapper.eq(StockBasic::getMarket, market);
        }
        return stockBasicMapper.selectList(wrapper);
    }

    public StockBasic getByCode(String stockCode) {
        LambdaQueryWrapper<StockBasic> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(StockBasic::getStockCode, stockCode);
        return stockBasicMapper.selectOne(wrapper);
    }

    public List<StockBasic> fetchFromSource(String market) {
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

    public void saveToDb(List<StockBasic> stocks) {
        for (StockBasic stock : stocks) {
            StockBasic exist = getByCode(stock.getStockCode());
            if (exist != null) {
                stock.setId(exist.getId());
                stockBasicMapper.updateById(stock);
            } else {
                stockBasicMapper.insert(stock);
            }
        }
    }

    public void refreshStockList(String market) {
        List<StockBasic> stocks = fetchFromSource(market);
        if (!stocks.isEmpty()) {
            saveToDb(stocks);
        }
    }
}