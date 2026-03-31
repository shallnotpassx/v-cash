package com.gongbotao.vcash.infrastructure.eastmoney;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.gongbotao.vcash.domain.financial.entity.StockFinancial;
import com.gongbotao.vcash.domain.stock.adapter.StockDataAdapter;
import com.gongbotao.vcash.domain.stock.entity.StockBasic;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class EastMoneyStockAdapter implements StockDataAdapter {

    private final RestTemplate restTemplate;

    private static final String EM_API_URL = "https://datacenter.eastmoney.com/api/data/v1/get";

    @Override
    public String getSourceName() {
        return "东方财富";
    }

    @Override
    public List<StockBasic> getStockList(String market) {
        try {
            if ("HK".equals(market)) {
                return getHkStockList();
            } else {
                return getAStockList();
            }
        } catch (Exception e) {
            log.error("获取股票列表失败: {}", e.getMessage());
            return new ArrayList<>();
        }
    }

    @Override
    public StockBasic getStockDetail(String stockCode) {
        try {
            if (isHkStock(stockCode)) {
                return getHkStockDetail(stockCode);
            } else {
                return getAStockDetail(stockCode);
            }
        } catch (Exception e) {
            log.error("获取股票详情失败: {}", e.getMessage());
            return null;
        }
    }

    @Override
    public List<StockFinancial> getFinancialData(String stockCode) {
        try {
            if (isHkStock(stockCode)) {
                return getHkFinancialData(stockCode);
            } else {
                return getAFinancialData(stockCode);
            }
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

    private List<StockBasic> getAStockList() {
        String url = EM_API_URL + "?reportName=RPT_BASIC_STOCKINFO&columns=ALL&source=WEB&client=WEB&filter=(EXCHANGE%3D%220001%22%20OR%20EXCHANGE%3D%220003%22)";
        
        String response = restTemplate.getForObject(url, String.class);
        JSONObject json = JSON.parseObject(response);
        JSONArray data = json.getJSONArray("data");
        
        if (data == null) {
            return new ArrayList<>();
        }
        
        List<StockBasic> result = new ArrayList<>();
        for (int i = 0; i < data.size(); i++) {
            try {
                JSONObject obj = data.getJSONObject(i);
                StockBasic stock = new StockBasic();
                stock.setStockCode(obj.getString("SYMBOL"));
                stock.setStockName(obj.getString("NAME"));
                stock.setMarket("A");
                stock.setIndustry(obj.getString("INDUSTRY"));
                String listDate = obj.getString("LIST_DATE");
                if (listDate != null) {
                    stock.setListDate(LocalDate.parse(listDate));
                }
                result.add(stock);
            } catch (Exception e) {
                log.warn("解析A股列表失败: {}", e.getMessage());
            }
        }
        return result;
    }

    private List<StockBasic> getHkStockList() {
        String url = EM_API_URL + "?reportName=RPT_BASIC_STOCKINFO_HK&columns=ALL&source=WEB&client=WEB";
        
        String response = restTemplate.getForObject(url, String.class);
        JSONObject json = JSON.parseObject(response);
        JSONArray data = json.getJSONArray("data");
        
        if (data == null) {
            return new ArrayList<>();
        }
        
        List<StockBasic> result = new ArrayList<>();
        for (int i = 0; i < data.size(); i++) {
            try {
                JSONObject obj = data.getJSONObject(i);
                StockBasic stock = new StockBasic();
                stock.setStockCode(obj.getString("SYMBOL"));
                stock.setStockName(obj.getString("NAME"));
                stock.setMarket("HK");
                stock.setIndustry(obj.getString("INDUSTRY"));
                result.add(stock);
            } catch (Exception e) {
                log.warn("解析港股列表失败: {}", e.getMessage());
            }
        }
        return result;
    }

    private StockBasic getAStockDetail(String stockCode) {
        String url = EM_API_URL + "?reportName=RPT_BASIC_STOCKINFO&columns=ALL&source=WEB&client=WEB&filter=(SYMBOL%3D'" + stockCode + "')";
        
        String response = restTemplate.getForObject(url, String.class);
        JSONObject json = JSON.parseObject(response);
        JSONArray data = json.getJSONArray("data");
        
        if (data != null && !data.isEmpty()) {
            JSONObject obj = data.getJSONObject(0);
            StockBasic stock = new StockBasic();
            stock.setStockCode(obj.getString("SYMBOL"));
            stock.setStockName(obj.getString("NAME"));
            stock.setMarket("A");
            stock.setIndustry(obj.getString("INDUSTRY"));
            String listDate = obj.getString("LIST_DATE");
            if (listDate != null) {
                stock.setListDate(LocalDate.parse(listDate));
            }
            return stock;
        }
        return null;
    }

    private StockBasic getHkStockDetail(String stockCode) {
        String url = EM_API_URL + "?reportName=RPT_BASIC_STOCKINFO_HK&columns=ALL&source=WEB&client=WEB&filter=(SYMBOL%3D'" + stockCode + "')";
        
        String response = restTemplate.getForObject(url, String.class);
        JSONObject json = JSON.parseObject(response);
        JSONArray data = json.getJSONArray("data");
        
        if (data != null && !data.isEmpty()) {
            JSONObject obj = data.getJSONObject(0);
            StockBasic stock = new StockBasic();
            stock.setStockCode(obj.getString("SYMBOL"));
            stock.setStockName(obj.getString("NAME"));
            stock.setMarket("HK");
            stock.setIndustry(obj.getString("INDUSTRY"));
            return stock;
        }
        return null;
    }

    private List<StockFinancial> getAFinancialData(String stockCode) {
        String url = EM_API_URL + "?reportName=RPT_FINMAIN_INDICATOR&columns=ALL&source=WEB&client=WEB&filter=(SECUCODE%3D'" + stockCode + ".SI')&pageNumber=1&pageSize=50";
        
        String response = restTemplate.getForObject(url, String.class);
        JSONObject json = JSON.parseObject(response);
        JSONArray data = json.getJSONArray("data");
        
        if (data == null) {
            return new ArrayList<>();
        }
        
        List<StockFinancial> result = new ArrayList<>();
        for (int i = 0; i < data.size(); i++) {
            try {
                JSONObject obj = data.getJSONObject(i);
                StockFinancial financial = new StockFinancial();
                financial.setReportPeriod(LocalDate.parse(obj.getString("END_DATE")));
                financial.setRevenue(obj.getBigDecimal("OPER_REVENUE"));
                financial.setNetProfit(obj.getBigDecimal("NET_PROFIT"));
                financial.setRoe(obj.getBigDecimal("ROE"));
                financial.setPe(obj.getBigDecimal("PE"));
                financial.setPb(obj.getBigDecimal("PB"));
                result.add(financial);
            } catch (Exception e) {
                log.warn("解析A股财务数据失败: {}", e.getMessage());
            }
        }
        return result;
    }

    private List<StockFinancial> getHkFinancialData(String stockCode) {
        String url = EM_API_URL + "?reportName=RPT_FINMAIN_INDICATOR_HK&columns=ALL&source=WEB&client=WEB&filter=(SECUCODE%3D'" + stockCode + ".HK')&pageNumber=1&pageSize=50";
        
        String response = restTemplate.getForObject(url, String.class);
        JSONObject json = JSON.parseObject(response);
        JSONArray data = json.getJSONArray("data");
        
        if (data == null) {
            return new ArrayList<>();
        }
        
        List<StockFinancial> result = new ArrayList<>();
        for (int i = 0; i < data.size(); i++) {
            try {
                JSONObject obj = data.getJSONObject(i);
                StockFinancial financial = new StockFinancial();
                financial.setReportPeriod(LocalDate.parse(obj.getString("END_DATE")));
                financial.setRevenue(obj.getBigDecimal("OPER_REVENUE"));
                financial.setNetProfit(obj.getBigDecimal("NET_PROFIT"));
                financial.setRoe(obj.getBigDecimal("ROE"));
                financial.setPe(obj.getBigDecimal("PE"));
                financial.setPb(obj.getBigDecimal("PB"));
                result.add(financial);
            } catch (Exception e) {
                log.warn("解析港股财务数据失败: {}", e.getMessage());
            }
        }
        return result;
    }

    private boolean isHkStock(String stockCode) {
        return stockCode.matches("^\\d{5}$");
    }
}