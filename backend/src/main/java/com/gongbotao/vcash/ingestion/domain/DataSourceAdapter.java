package com.gongbotao.vcash.ingestion.domain;

import com.gongbotao.vcash.shared.domain.StockIdentity;

/** 数据源适配器接口 —— 骨架期只定义契约 */
public interface DataSourceAdapter {
    String sourceName();
    SyncResult sync(StockIdentity stock);
}
