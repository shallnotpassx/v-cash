package com.gongbotao.vcash.service.financial;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.gongbotao.vcash.data.source.adapter.StockDataAdapter;
import com.gongbotao.vcash.data.storage.mapper.StockFinancialMapper;
import com.gongbotao.vcash.data.storage.mapper.StockBasicMapper;
import com.gongbotao.vcash.domain.entity.StockFinancial;
import com.gongbotao.vcash.domain.entity.StockBasic;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FinancialServiceTest {

    @Mock
    private StockFinancialMapper financialMapper;

    @Mock
    private StockBasicMapper stockBasicMapper;

    @Mock
    private StockDataAdapter adapter1;

    @Mock
    private StockDataAdapter adapter2;

    private FinancialService financialService;

    @BeforeEach
    void setUp() {
        financialService = new FinancialService(financialMapper, stockBasicMapper, Arrays.asList(adapter1, adapter2));
    }

    @Test
    void getByStockCode_stockNotFound() {
        when(stockBasicMapper.selectOne(any(LambdaQueryWrapper.class))).thenReturn(null);

        List<StockFinancial> result = financialService.getByStockCode("600000");

        assertTrue(result.isEmpty());
    }

    @Test
    void getByStockCode_success() {
        StockBasic stock = new StockBasic();
        stock.setId(1L);
        stock.setStockCode("600000");

        StockFinancial financial = createFinancial(1L, LocalDate.of(2024, 12, 31));
        when(stockBasicMapper.selectOne(any(LambdaQueryWrapper.class))).thenReturn(stock);
        when(financialMapper.selectList(any(LambdaQueryWrapper.class))).thenReturn(List.of(financial));

        List<StockFinancial> result = financialService.getByStockCode("600000");

        assertEquals(1, result.size());
    }

    @Test
    void getLatestByStockCode_found() {
        StockBasic stock = new StockBasic();
        stock.setId(1L);

        StockFinancial financial1 = createFinancial(1L, LocalDate.of(2024, 12, 31));
        StockFinancial financial2 = createFinancial(1L, LocalDate.of(2024, 6, 30));

        when(stockBasicMapper.selectOne(any(LambdaQueryWrapper.class))).thenReturn(stock);
        when(financialMapper.selectList(any(LambdaQueryWrapper.class))).thenReturn(Arrays.asList(financial1, financial2));

        StockFinancial result = financialService.getLatestByStockCode("600000");

        assertNotNull(result);
    }

    @Test
    void getLatestByStockCode_notFound() {
        when(stockBasicMapper.selectOne(any(LambdaQueryWrapper.class))).thenReturn(null);

        StockFinancial result = financialService.getLatestByStockCode("600000");

        assertNull(result);
    }

    @Test
    void fetchFromSource_success() {
        List<StockFinancial> mockList = List.of(createFinancial(1L, LocalDate.of(2024, 12, 31)));
        when(adapter1.getSourceName()).thenReturn("Tushare");
        when(adapter1.getFinancialData("600000")).thenReturn(mockList);

        List<StockFinancial> result = financialService.fetchFromSource("600000");

        assertEquals(1, result.size());
        verify(adapter1).getFinancialData("600000");
    }

    @Test
    void fetchFromSource_firstAdapterFail_trySecond() {
        List<StockFinancial> mockList = List.of(createFinancial(1L, LocalDate.of(2024, 12, 31)));
        when(adapter1.getFinancialData("600000")).thenThrow(new RuntimeException("Error"));
        when(adapter2.getFinancialData("600000")).thenReturn(mockList);

        List<StockFinancial> result = financialService.fetchFromSource("600000");

        assertEquals(1, result.size());
        verify(adapter2).getFinancialData("600000");
    }

    @Test
    void fetchFromSource_allAdapterFail() {
        when(adapter1.getFinancialData("600000")).thenThrow(new RuntimeException("Error"));
        when(adapter2.getFinancialData("600000")).thenThrow(new RuntimeException("Error"));

        List<StockFinancial> result = financialService.fetchFromSource("600000");

        assertTrue(result.isEmpty());
    }

    @Test
    void saveToDb_stockNotFound() {
        when(stockBasicMapper.selectOne(any(LambdaQueryWrapper.class))).thenReturn(null);

        financialService.saveToDb("600000", List.of(createFinancial(1L, LocalDate.now())));

        verify(financialMapper, never()).insert(any(StockFinancial.class));
        verify(financialMapper, never()).updateById(any(StockFinancial.class));
    }

    @Test
    void saveToDb_insertNew() {
        StockBasic stock = new StockBasic();
        stock.setId(1L);
        stock.setStockCode("600000");

        StockFinancial financial = createFinancial(null, LocalDate.of(2024, 12, 31));

        when(stockBasicMapper.selectOne(any(LambdaQueryWrapper.class))).thenReturn(stock);
        when(financialMapper.selectOne(any(LambdaQueryWrapper.class))).thenReturn(null);
        when(financialMapper.insert(any(StockFinancial.class))).thenReturn(1);

        financialService.saveToDb("600000", List.of(financial));

        verify(financialMapper).insert(any(StockFinancial.class));
    }

    @Test
    void saveToDb_updateExist() {
        StockBasic stock = new StockBasic();
        stock.setId(1L);
        stock.setStockCode("600000");

        StockFinancial financial = createFinancial(null, LocalDate.of(2024, 12, 31));
        StockFinancial existFinancial = createFinancial(1L, LocalDate.of(2024, 12, 31));

        when(stockBasicMapper.selectOne(any(LambdaQueryWrapper.class))).thenReturn(stock);
        when(financialMapper.selectOne(any(LambdaQueryWrapper.class))).thenReturn(existFinancial);
        when(financialMapper.updateById(any(StockFinancial.class))).thenReturn(1);

        financialService.saveToDb("600000", List.of(financial));

        verify(financialMapper).updateById(any(StockFinancial.class));
        verify(financialMapper, never()).insert(any(StockFinancial.class));
    }

    @Test
    void refreshFinancialData_success() {
        StockBasic stock = new StockBasic();
        stock.setId(1L);

        List<StockFinancial> mockList = List.of(createFinancial(1L, LocalDate.of(2024, 12, 31)));

        when(adapter1.getFinancialData("600000")).thenReturn(mockList);
        when(stockBasicMapper.selectOne(any(LambdaQueryWrapper.class))).thenReturn(stock);
        when(financialMapper.selectOne(any(LambdaQueryWrapper.class))).thenReturn(null);
        when(financialMapper.insert(any(StockFinancial.class))).thenReturn(1);

        financialService.refreshFinancialData("600000");

        verify(financialMapper).insert(any(StockFinancial.class));
    }

    private StockFinancial createFinancial(Long id, LocalDate reportPeriod) {
        StockFinancial financial = new StockFinancial();
        if (id != null) {
            financial.setId(id);
        }
        financial.setStockId(1L);
        financial.setReportPeriod(reportPeriod);
        financial.setRevenue(new BigDecimal("1000000000"));
        financial.setNetProfit(new BigDecimal("100000000"));
        financial.setRoe(new BigDecimal("0.15"));
        financial.setPe(new BigDecimal("10.5"));
        financial.setPb(new BigDecimal("1.2"));
        return financial;
    }
}