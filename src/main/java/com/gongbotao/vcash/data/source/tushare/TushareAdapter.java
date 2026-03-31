package com.gongbotao.vcash.data.source.tushare;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.gongbotao.vcash.data.source.adapter.StockDataAdapter;
import com.gongbotao.vcash.domain.entity.StockBasic;
import com.gongbotao.vcash.domain.entity.StockFinancial;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class TushareAdapter implements StockDataAdapter {

    private final RestTemplate restTemplate;

    @Value("${tushare.token:}")
    private String token;

    @Value("${tushare.api-url:https://api.tushare.pro}")
    private String apiUrl;

    @Override
    public String getSourceName() {
        return "Tushare";
    }

    @Override
    public List<StockBasic> getStockList(String market) {
        Map<String, Object> params = new HashMap<>();
        params.put("api_name", "stock_basic");
        params.put("token", token);
        params.put("fields", "ts_code,symbol,name,industry,list_date,market");
        
        if ("HK".equals(market)) {
            params.put("exchange", "HKEX");
        } else {
            params.put("exchange", "SSE");
        }

        try {
            JSONObject response = callApi(params);
            JSONArray data = response.getJSONArray("data");
            if (data == null) {
                return new ArrayList<>();
            }
            return parseStockList(data, market);
        } catch (Exception e) {
            log.error("获取股票列表失败: {}", e.getMessage());
            return new ArrayList<>();
        }
    }

    @Override
    public StockBasic getStockDetail(String stockCode) {
        Map<String, Object> params = new HashMap<>();
        params.put("api_name", "stock_basic");
        params.put("token", token);
        params.put("fields", "ts_code,symbol,name,industry,list_date,market");
        params.put("ts_code", formatStockCode(stockCode));

        try {
            JSONObject response = callApi(params);
            JSONArray data = response.getJSONArray("data");
            if (data != null && !data.isEmpty()) {
                return parseStock(data.getJSONObject(0), null);
            }
        } catch (Exception e) {
            log.error("获取股票详情失败: {}", e.getMessage());
        }
        return null;
    }

    @Override
    public List<StockFinancial> getFinancialData(String stockCode) {
        Map<String, Object> params = new HashMap<>();
        params.put("api_name", "fina_indicator");
        params.put("token", token);
        params.put("fields", "ts_code,end_date,revenue,net_profit,roe,pe,pb");
        params.put("ts_code", formatStockCode(stockCode));

        try {
            JSONObject response = callApi(params);
            JSONArray data = response.getJSONArray("data");
            if (data == null) {
                return new ArrayList<>();
            }
            return parseFinancialList(data);
        } catch (Exception e) {
            log.error("获取财务数据失败: {}", e.getMessage());
            return new ArrayList<>();
        }
    }

    @Override
    public StockFinancial getLatestFinancialData(String stockCode) {
        List<StockFinancial> list = getFinancialData(stockCode);
        return list.isEmpty() ? null : list.get(0);
    }

    private JSONObject callApi(Map<String, Object> params) {
        String response = restTemplate.postForObject(apiUrl, params, String.class);
        return JSON.parseObject(response);
    }

    private List<StockBasic> parseStockList(JSONArray data, String market) {
        List<StockBasic> result = new ArrayList<>();
        for (int i = 0; i < data.size(); i++) {
            StockBasic stock = parseStock(data.getJSONObject(i), market);
            if (stock != null) {
                result.add(stock);
            }
        }
        return result;
    }

    private StockBasic parseStock(JSONObject json, String market) {
        try {
            StockBasic stock = new StockBasic();
            String tsCode = json.getString("ts_code");
            if (tsCode == null) {
                return null;
            }
            stock.setStockCode(tsCode);
            stock.setStockName(json.getString("name"));
            stock.setIndustry(json.getString("industry"));
            
            if (tsCode.endsWith(".HK")) {
                stock.setMarket("HK");
            } else {
                stock.setMarket("A");
            }

            if (market != null && !market.equals(stock.getMarket())) {
                return null;
            }

            String listDate = json.getString("list_date");
            if (listDate != null && listDate.length() == 8) {
                stock.setListDate(LocalDate.parse(listDate, java.time.format.DateTimeFormatter.ofPattern("yyyyMMdd")));
            }
            return stock;
        } catch (Exception e) {
            log.warn("解析股票数据失败: {}", e.getMessage());
            return null;
        }
    }

    private List<StockFinancial> parseFinancialList(JSONArray data) {
        List<StockFinancial> result = new ArrayList<>();
        for (int i = 0; i < data.size(); i++) {
            try {
                JSONObject json = data.getJSONObject(i);
                StockFinancial financial = new StockFinancial();
                String endDate = json.getString("end_date");
                if (endDate != null && endDate.length() == 8) {
                    financial.setReportPeriod(LocalDate.parse(endDate, java.time.format.DateTimeFormatter.ofPattern("yyyyMMdd")));
                }
                financial.setRevenue(json.getBigDecimal("revenue"));
                financial.setNetProfit(json.getBigDecimal("net_profit"));
                financial.setRoe(json.getBigDecimal("roe"));
                financial.setPe(json.getBigDecimal("pe"));
                financial.setPb(json.getBigDecimal("pb"));
                result.add(financial);
            } catch (Exception e) {
                log.warn("解析财务数据失败: {}", e.getMessage());
            }
        }
        return result;
    }

    private String formatStockCode(String stockCode) {
        if (stockCode == null) {
            return "";
        }
        if (stockCode.contains(".")) {
            return stockCode;
        }
        if (stockCode.startsWith("6")) {
            return stockCode + ".SH";
        } else if (stockCode.startsWith("0") || stockCode.startsWith("3")) {
            return stockCode + ".SZ";
        } else if (stockCode.startsWith("4") || stockCode.startsWith("8")) {
            return stockCode + ".BJ";
        } else if (stockCode.matches("^\\d{5}$")) {
            return stockCode + ".HK";
        }
        return stockCode;
    }
}