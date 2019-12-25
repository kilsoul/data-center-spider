package com.fightermap.backend.spider.core.service;

import com.fightermap.backend.spider.core.model.bo.spider.SpiderLogCount;
import com.fightermap.backend.spider.core.model.entity.SpiderLog;

import java.util.List;
import java.util.Map;

/**
 * @author zengqk
 */
public interface SpiderLogService {

    SpiderLog save(SpiderLog spiderLog);

    void saveAsync(SpiderLog spiderLog);

    Map<String, SpiderLogCount> countBySpiderUuid(List<String> uuids);
}
