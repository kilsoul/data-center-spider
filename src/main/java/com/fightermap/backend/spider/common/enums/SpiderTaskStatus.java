package com.fightermap.backend.spider.common.enums;

import us.codecraft.webmagic.Spider;

/**
 * 爬虫任务状态
 */
public enum SpiderTaskStatus {

    /**
     * 初始化
     */
    INITIAL,

    /**
     * 运行中
     */
    RUNNING,

    /**
     * 意外中断
     */
    INTERRUPTED,

    /**
     * 正常完成
     */
    COMPLETED,

    /**
     * 未知
     */
    UNKNOWN;

    public static SpiderTaskStatus mappingSpiderStatus(Spider.Status status) {
        switch (status) {
            case Init:
                return INITIAL;
            case Running:
                return RUNNING;
            case Stopped:
                return COMPLETED;
            default:
                return UNKNOWN;
        }
    }
}
