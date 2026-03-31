package com.gongbotao.vcash.data.source.adapter;

import com.gongbotao.vcash.domain.entity.StockFinancial;
import com.gongbotao.vcash.domain.entity.StockBasic;

import java.util.List;

public interface StockDataAdapter {

    String getSourceName();

    List<StockBasic> getStockList(String market);

    StockBasic getStockDetail(String stockCode);

    List<StockFinancial> getFinancialData(String stockCode);

    StockFinancial getLatestFinancialData(String stockCode);
}