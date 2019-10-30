package com.fightermap.backend.spider.core.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 行政区
 */
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class District {

    /**
     * 行政区名
     */
    private String name;

    /**
     * 中文名
     */
    private String chineseName;

    /**
     * 行政区访问地址
     */
    private String url;
}
