package com.gongbotao.vcash.ingestion.web;

import com.gongbotao.vcash.ingestion.application.SyncLogQueryService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.bean.annotation.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.hamcrest.Matchers.hasSize;

@WebMvcTest(SyncLogController.class)
class SyncLogControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private SyncLogQueryService service;

    @Test
    void listAll_shouldReturnEmptyList_whenNoLogs() throws Exception {
        given(service.listAll()).willReturn(Collections.emptyList());

        mockMvc.perform(get("/api/sync/logs"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }
}
