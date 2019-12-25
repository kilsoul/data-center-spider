package com.fightermap.backend.spider.core.service;

import com.fightermap.backend.spider.common.enums.SourceType;
import us.codecraft.webmagic.Spider;

/**
 * @author zengqk
 */
public interface SpiderService {

    Spider start(SourceType sourceType, String seedUrl,int threadCount);

    Spider startSync(SourceType sourceType, String seedUrl,int threadCount);

    Spider stop(String uuid);
}
