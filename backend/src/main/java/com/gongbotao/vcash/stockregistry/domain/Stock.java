package com.gongbotao.vcash.stockregistry.domain;

import com.gongbotao.vcash.shared.domain.StockIdentity;

import java.time.LocalDate;
import java.util.Objects;

/**
 * 股票主数据领域实体。
 * 不依赖任何框架注解 —— 由 infrastructure 层负责映射。
 */
public class Stock {

    private final StockIdentity identity;
    private String stockName;
    private ListedStatus listedStatus;
    private LocalDate listingDate;
    private String exchange;

    public Stock(StockIdentity identity, String stockName, ListedStatus listedStatus,
                 LocalDate listingDate, String exchange) {
        this.identity = Objects.requireNonNull(identity);
        this.stockName = Objects.requireNonNull(stockName);
        this.listedStatus = Objects.requireNonNull(listedStatus);
        this.listingDate = listingDate;
        this.exchange = exchange;
    }

    public StockIdentity identity() { return identity; }
    public String market() { return identity.market(); }
    public String stockCode() { return identity.stockCode(); }
    public String stockName() { return stockName; }
    public ListedStatus listedStatus() { return listedStatus; }
    public LocalDate listingDate() { return listingDate; }
    public String exchange() { return exchange; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Stock other)) return false;
        return identity.equals(other.identity);
    }

    @Override
    public int hashCode() {
        return identity.hashCode();
    }

    @Override
    public String toString() {
        return "Stock{" + identity + ", name=" + stockName + '}';
    }

    public enum ListedStatus {
        LISTED,
        SUSPENDED,
        DELISTED
    }
}
