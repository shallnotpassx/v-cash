package com.gongbotao.vcash.service.financial;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.gongbotao.vcash.data.source.adapter.StockDataAdapter;
import com.gongbotao.vcash.data.storage.mapper.StockFinancialMapper;
import com.gongbotao.vcash.data.storage.mapper.StockBasicMapper;
import com.gongbotao.vcash.domain.entity.StockFinancial;
import com.gongbotao.vcash.domain.entity.StockBasic;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class FinancialService {

    private final StockFinancialMapper financialMapper;
    private final StockBasicMapper stockBasicMapper;
    private final List<StockDataAdapter> adapters;

    public List<StockFinancial> getByStockCode(String stockCode) {
        StockBasic stock = getStock(stockCode);
        if (stock == null) {
            return List.of();
        }
        
        LambdaQueryWrapper<StockFinancial> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(StockFinancial::getStockId, stock.getId())
               .orderByDesc(StockFinancial::getReportPeriod);
        return financialMapper.selectList(wrapper);
    }

    public StockFinancial getLatestByStockCode(String stockCode) {
        List<StockFinancial> list = getByStockCode(stockCode);
        return list.isEmpty() ? null : list.get(0);
    }

    public List<StockFinancial> fetchFromSource(String stockCode) {
        for (StockDataAdapter adapter : adapters) {
            try {
                log.info("尝试从 {} 获取财务数据", adapter.getSourceName());
                List<StockFinancial> list = adapter.getFinancialData(stockCode);
                if (list != null && !list.isEmpty()) {
                    log.info("从 {} 获取到 {} 条财务数据", adapter.getSourceName(), list.size());
                    return list;
                }
            } catch (Exception e) {
                log.warn("从 {} 获取财务数据失败: {}", adapter.getSourceName(), e.getMessage());
            }
        }
        return List.of();
    }

    public void saveToDb(String stockCode, List<StockFinancial> financials) {
        StockBasic stock = getStock(stockCode);
        if (stock == null) {
            log.warn("股票 {} 不存在", stockCode);
            return;
        }

        for (StockFinancial financial : financials) {
            financial.setStockId(stock.getId());
            
            LambdaQueryWrapper<StockFinancial> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(StockFinancial::getStockId, stock.getId())
                   .eq(StockFinancial::getReportPeriod, financial.getReportPeriod());
            StockFinancial exist = financialMapper.selectOne(wrapper);
            
            if (exist != null) {
                financial.setId(exist.getId());
                financialMapper.updateById(financial);
            } else {
                financialMapper.insert(financial);
            }
        }
    }

    public void refreshFinancialData(String stockCode) {
        List<StockFinancial> financials = fetchFromSource(stockCode);
        if (!financials.isEmpty()) {
            saveToDb(stockCode, financials);
        }
    }

    private StockBasic getStock(String stockCode) {
        LambdaQueryWrapper<StockBasic> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(StockBasic::getStockCode, stockCode);
        return stockBasicMapper.selectOne(wrapper);
    }
}