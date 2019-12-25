package com.fightermap.backend.spider.core.service;

import com.fightermap.backend.spider.core.model.entity.SpiderTask;

import java.util.List;
import java.util.Set;

/**
 * @author zengqk
 */
public interface SpiderTaskService {

    SpiderTask saveOrUpdate(SpiderTask spiderTask);

    List<SpiderTask> findAllByUuids(Set<String> uuids);

    List<SpiderTask> saveAll(List<SpiderTask> entities);
}
