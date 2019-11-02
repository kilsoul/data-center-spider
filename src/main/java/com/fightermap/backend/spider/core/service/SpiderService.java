package com.fightermap.backend.spider.core.service;

import com.fightermap.backend.spider.common.enums.SourceType;

/**
 * @author zengqk
 */
public interface SpiderService {

    void start(SourceType sourceType, String seedUrl);
}
