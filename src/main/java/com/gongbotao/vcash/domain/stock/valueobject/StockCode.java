package com.gongbotao.vcash.domain.stock.valueobject;

import lombok.Getter;

@Getter
public class StockCode {
    private final String code;
    private final Market market;

    public StockCode(String code, String market) {
        this.code = code;
        this.market = Market.of(market);
    }

    public String getFullCode() {
        return market.getPrefix() + code;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StockCode stockCode = (StockCode) o;
        return code.equals(stockCode.code) && market == stockCode.market;
    }

    @Override
    public int hashCode() {
        int result = code.hashCode();
        result = 31 * result + market.hashCode();
        return result;
    }
}