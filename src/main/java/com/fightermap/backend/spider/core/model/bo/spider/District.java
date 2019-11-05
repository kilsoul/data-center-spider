package com.fightermap.backend.spider.core.model.bo.spider;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * 行政区
 *
 * @author zengqk
 */
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class District extends BaseData {

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

    /**
     * 市级名称
     */
    private String cityName;
}
