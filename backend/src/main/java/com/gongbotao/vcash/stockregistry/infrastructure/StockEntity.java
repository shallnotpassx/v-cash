package com.gongbotao.vcash.stockregistry.infrastructure;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.gongbotao.vcash.stockregistry.domain.Stock;
import com.gongbotao.vcash.stockregistry.domain.Stock.ListedStatus;
import com.gongbotao.vcash.shared.domain.StockIdentity;
import lombok.Data;

import java.time.LocalDate;

@Data
@TableName("stock")
public class StockEntity {

    @TableId
    private Long id;

    private String market;
    private String stockCode;
    private String stockName;
    private String listedStatus;
    private LocalDate listingDate;
    private String exchange;

    public Stock toDomain() {
        return new Stock(
                new StockIdentity(market, stockCode),
                stockName,
                ListedStatus.valueOf(listedStatus),
                listingDate,
                exchange
        );
    }

    public static StockEntity fromDomain(Stock stock) {
        StockEntity entity = new StockEntity();
        entity.setMarket(stock.market());
        entity.setStockCode(stock.stockCode());
        entity.setStockName(stock.stockName());
        entity.setListedStatus(stock.listedStatus().name());
        entity.setListingDate(stock.listingDate());
        entity.setExchange(stock.exchange());
        return entity;
    }
}
