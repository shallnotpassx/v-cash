package com.gongbotao.vcash.shared.domain;

import java.util.Objects;

/**
 * 跨上下文统一股票标识 —— market + stock_code。
 * A 股 market="SH"/"SZ"，港股 market="HK"。
 */
public record StockIdentity(String market, String stockCode) {

    public StockIdentity {
        Objects.requireNonNull(market, "market must not be null");
        Objects.requireNonNull(stockCode, "stockCode must not be null");
    }

    /** 返回 "market:stockCode" 格式，用于日志和缓存 key。 */
    public String toCompositeKey() {
        return market + ":" + stockCode;
    }

    @Override
    public String toString() {
        return toCompositeKey();
    }
}
