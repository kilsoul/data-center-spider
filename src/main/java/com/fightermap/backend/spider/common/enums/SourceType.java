package com.fightermap.backend.spider.common.enums;

/**
 * @author zengqk
 */

public enum SourceType {

    /**
     * 链家
     */
    LIANJIA,

    /**
     * 未知类型
     */
    UNKNOWN;

    public static SourceType map(String sourceType) {
        return SourceType.valueOf(sourceType);
    }
}
