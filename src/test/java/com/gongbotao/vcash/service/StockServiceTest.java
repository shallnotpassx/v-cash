package com.gongbotao.vcash.service.stock;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.gongbotao.vcash.data.source.adapter.StockDataAdapter;
import com.gongbotao.vcash.data.storage.mapper.StockBasicMapper;
import com.gongbotao.vcash.domain.entity.StockBasic;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StockServiceTest {

    @Mock
    private StockBasicMapper stockBasicMapper;

    @Mock
    private StockDataAdapter adapter1;

    @Mock
    private StockDataAdapter adapter2;

    private StockService stockService;

    @BeforeEach
    void setUp() {
        stockService = new StockService(stockBasicMapper, Arrays.asList(adapter1, adapter2));
    }

    @Test
    void listFromDb_withMarketFilter() {
        List<StockBasic> mockList = List.of(createStock("600000", "浦发银行", "A"));
        when(stockBasicMapper.selectList(any(LambdaQueryWrapper.class))).thenReturn(mockList);

        List<StockBasic> result = stockService.listFromDb("A");

        assertEquals(1, result.size());
        assertEquals("600000", result.get(0).getStockCode());
    }

    @Test
    void listFromDb_noMarketFilter() {
        when(stockBasicMapper.selectList(any(LambdaQueryWrapper.class))).thenReturn(Collections.emptyList());

        List<StockBasic> result = stockService.listFromDb(null);

        verify(stockBasicMapper).selectList(any(LambdaQueryWrapper.class));
    }

    @Test
    void getByCode_found() {
        StockBasic mockStock = createStock("600000", "浦发银行", "A");
        when(stockBasicMapper.selectOne(any(LambdaQueryWrapper.class))).thenReturn(mockStock);

        StockBasic result = stockService.getByCode("600000");

        assertNotNull(result);
        assertEquals("600000", result.getStockCode());
    }

    @Test
    void getByCode_notFound() {
        when(stockBasicMapper.selectOne(any(LambdaQueryWrapper.class))).thenReturn(null);

        StockBasic result = stockService.getByCode("999999");

        assertNull(result);
    }

    @Test
    void fetchFromSource_success() {
        List<StockBasic> mockList = List.of(
            createStock("600000", "浦发银行", "A"),
            createStock("600019", "宝钢股份", "A")
        );
        when(adapter1.getSourceName()).thenReturn("Tushare");
        when(adapter1.getStockList("A")).thenReturn(mockList);

        List<StockBasic> result = stockService.fetchFromSource("A");

        assertEquals(2, result.size());
        verify(adapter1).getStockList("A");
        verify(adapter2, never()).getStockList(any());
    }

    @Test
    void fetchFromSource_firstAdapterFail_trySecond() {
        List<StockBasic> mockList = List.of(createStock("600000", "浦发银行", "A"));
        when(adapter1.getSourceName()).thenReturn("Tushare");
        when(adapter1.getStockList("A")).thenThrow(new RuntimeException("API Error"));
        when(adapter2.getSourceName()).thenReturn("EastMoney");
        when(adapter2.getStockList("A")).thenReturn(mockList);

        List<StockBasic> result = stockService.fetchFromSource("A");

        assertEquals(1, result.size());
        verify(adapter1).getStockList("A");
        verify(adapter2).getStockList("A");
    }

    @Test
    void fetchFromSource_allAdapterFail() {
        when(adapter1.getStockList("A")).thenThrow(new RuntimeException("Error 1"));
        when(adapter2.getStockList("A")).thenThrow(new RuntimeException("Error 2"));

        List<StockBasic> result = stockService.fetchFromSource("A");

        assertTrue(result.isEmpty());
    }

    @Test
    void saveToDb_insertNew() {
        StockBasic newStock = createStock("600000", "浦发银行", "A");
        when(stockBasicMapper.selectOne(any(LambdaQueryWrapper.class))).thenReturn(null);
        when(stockBasicMapper.insert(any(StockBasic.class))).thenReturn(1);

        stockService.saveToDb(List.of(newStock));

        verify(stockBasicMapper).insert(any(StockBasic.class));
    }

    @Test
    void saveToDb_updateExist() {
        StockBasic stock = createStock("600000", "浦发银行", "A");
        StockBasic existStock = createStock("600000", "浦发银行", "A");
        existStock.setId(1L);
        when(stockBasicMapper.selectOne(any(LambdaQueryWrapper.class))).thenReturn(existStock);
        when(stockBasicMapper.updateById(any(StockBasic.class))).thenReturn(1);

        stockService.saveToDb(List.of(stock));

        verify(stockBasicMapper).updateById(any(StockBasic.class));
        verify(stockBasicMapper, never()).insert(any(StockBasic.class));
    }

    @Test
    void refreshStockList_success() {
        List<StockBasic> mockList = List.of(createStock("600000", "浦发银行", "A"));
        when(adapter1.getSourceName()).thenReturn("Tushare");
        when(adapter1.getStockList("A")).thenReturn(mockList);
        when(stockBasicMapper.selectOne(any(LambdaQueryWrapper.class))).thenReturn(null);
        when(stockBasicMapper.insert(any(StockBasic.class))).thenReturn(1);

        stockService.refreshStockList("A");

        verify(stockBasicMapper).insert(any(StockBasic.class));
    }

    private StockBasic createStock(String code, String name, String market) {
        StockBasic stock = new StockBasic();
        stock.setStockCode(code);
        stock.setStockName(name);
        stock.setMarket(market);
        stock.setIndustry("银行");
        stock.setListDate(LocalDate.of(1999, 11, 10));
        return stock;
    }
}