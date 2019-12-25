package com.fightermap.backend.spider.core.model.bo.spider;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author zengqk
 */
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class SpiderLogCount {

    private String uuid;

    private Integer successCount;

    private Integer failCount;
}
