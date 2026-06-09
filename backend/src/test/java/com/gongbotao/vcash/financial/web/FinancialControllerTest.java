package com.gongbotao.vcash.financial.web;

import com.gongbotao.vcash.financial.application.FinancialQueryService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.hamcrest.Matchers.hasSize;

@WebMvcTest(FinancialController.class)
class FinancialControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private FinancialQueryService service;

    @Test
    void history_shouldReturnEmptyList() throws Exception {
        given(service.history("SH", "600000")).willReturn(Collections.emptyList());

        mockMvc.perform(get("/api/financial/history?market=SH&code=600000"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    void snapshots_shouldReturnEmptyList() throws Exception {
        given(service.snapshots()).willReturn(Collections.emptyList());

        mockMvc.perform(get("/api/financial/snapshot"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }
}
