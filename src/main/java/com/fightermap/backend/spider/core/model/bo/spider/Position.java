package com.fightermap.backend.spider.core.model.bo.spider;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 镇级
 *
 * @author zengqk
 */
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class Position extends BaseData{

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

    /**
     * 区级英文名称
     */
    private String districtName;
}
