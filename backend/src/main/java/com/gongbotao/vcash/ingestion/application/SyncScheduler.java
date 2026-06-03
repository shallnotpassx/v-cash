package com.gongbotao.vcash.ingestion.application;

/**
 * 定时同步调度器接口 —— 骨架期只定义契约，不实现具体调度逻辑。
 * 后续可接入 @Scheduled 或外部调度框架。
 */
public interface SyncScheduler {

    /** 触发一次全量或增量同步（具体策略由实现决定）。 */
    void triggerScheduledSync();
}
