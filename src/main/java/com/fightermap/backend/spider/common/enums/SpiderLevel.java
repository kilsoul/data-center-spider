package com.fightermap.backend.spider.common.enums;

import lombok.Getter;

/**
 * 爬虫优先级
 *
 * @author zengqk
 */
@Getter
public enum SpiderLevel {

    /**
     * 高优先级
     */
    HIGH(100L, "高"),

    /**
     * 中优先级
     */
    NORMAL(0L, "中"),

    /**
     * 低优先级
     */
    LOW(-100L, "低");

    /**
     * 值
     */
    private Long value;

    /**
     * 描述
     */
    private String desc;

    SpiderLevel(Long value, String desc) {
        this.value = value;
        this.desc = desc;
    }
}
