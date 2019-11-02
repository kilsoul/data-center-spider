package com.fightermap.backend.spider.core.model.bo.spider;

import com.fightermap.backend.spider.common.enums.SourceType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author zengqk
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
public class BaseData {
    /**
     * 域名
     */
    private String host;

    /**
     * 来源
     */
    private SourceType sourceType;
}
