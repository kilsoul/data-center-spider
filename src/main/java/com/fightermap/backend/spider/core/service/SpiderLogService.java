package com.fightermap.backend.spider.core.service;

import com.fightermap.backend.spider.core.model.entity.SpiderLog;

/**
 * @author zengqk
 */
public interface SpiderLogService {

    SpiderLog save(SpiderLog spiderLog);

    void saveAsync(SpiderLog spiderLog);
}
