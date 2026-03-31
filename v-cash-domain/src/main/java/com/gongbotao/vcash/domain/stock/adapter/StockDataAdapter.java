package com.gongbotao.vcash.domain.stock.adapter;

import com.gongbotao.vcash.domain.financial.entity.StockFinancial;
import com.gongbotao.vcash.domain.stock.entity.StockBasic;

import java.util.List;

public interface StockDataAdapter {

    String getSourceName();

    List<StockBasic> getStockList(String market);

    StockBasic getStockDetail(String stockCode);

    List<StockFinancial> getFinancialData(String stockCode);

    StockFinancial getLatestFinancialData(String stockCode);
}