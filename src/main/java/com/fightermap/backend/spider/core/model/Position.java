package com.fightermap.backend.spider.core.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 镇级
 */
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class Position {

    /**
     * 名称
     */
    private String name;

    /**
     * 中文名
     */
    private String chineseName;

    /**
     * 连接地址
     */
    private String url;
}
