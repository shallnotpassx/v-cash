package com.gongbotao.vcash.stockregistry.web;

import com.gongbotao.vcash.stockregistry.application.StockQueryService;
import com.gongbotao.vcash.stockregistry.domain.Stock;
import com.gongbotao.vcash.stockregistry.domain.Stock.ListedStatus;
import com.gongbotao.vcash.shared.domain.StockIdentity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.Collections;
import java.util.Optional;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.hamcrest.Matchers.hasSize;

@WebMvcTest(StockController.class)
class StockControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private StockQueryService stockQueryService;

    @Test
    void listAll_shouldReturnEmptyList_whenNoStocks() throws Exception {
        given(stockQueryService.listAll()).willReturn(Collections.emptyList());

        mockMvc.perform(get("/api/stocks"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    void findByMarketAndCode_shouldReturnStock_whenFound() throws Exception {
        Stock stock = new Stock(
                new StockIdentity("SH", "600000"),
                "浦发银行",
                ListedStatus.LISTED,
                LocalDate.of(1999, 11, 10),
                "SSE"
        );
        given(stockQueryService.findByMarketAndCode("SH", "600000"))
                .willReturn(Optional.of(stock));

        mockMvc.perform(get("/api/stocks?market=SH&code=600000"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.market").value("SH"))
                .andExpect(jsonPath("$.stockCode").value("600000"))
                .andExpect(jsonPath("$.stockName").value("浦发银行"));
    }
}
