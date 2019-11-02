package com.fightermap.backend.spider.common.enums;

/**
 * @author zengqk
 */

public enum AreaType {

    /**
     * 市
     */
    CITY {
        @Override
        public AreaType getParent() {
            return UNKNOWN;
        }
    },

    /**
     * 区，县
     */
    DISTRICT {
        @Override
        public AreaType getParent() {
            return CITY;
        }
    },

    /**
     * 镇
     */
    POSITION {
        @Override
        public AreaType getParent() {
            return DISTRICT;
        }
    },

    UNKNOWN;

    /**
     * 获取父级
     *
     * @return
     */
    public AreaType getParent() {
        return UNKNOWN;
    }
}
