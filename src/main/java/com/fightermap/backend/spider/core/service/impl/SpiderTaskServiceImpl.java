package com.fightermap.backend.spider.core.service.impl;

import com.fightermap.backend.spider.common.util.ClassUtil;
import com.fightermap.backend.spider.core.model.entity.SpiderTask;
import com.fightermap.backend.spider.core.repository.SpiderTaskRepository;
import com.fightermap.backend.spider.core.service.SpiderTaskService;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * @author zengqk
 */
@Slf4j
@Service
public class SpiderTaskServiceImpl implements SpiderTaskService {

    private final SpiderTaskRepository spiderTaskRepository;

    public SpiderTaskServiceImpl(SpiderTaskRepository spiderTaskRepository) {
        this.spiderTaskRepository = spiderTaskRepository;
    }

    @Override
    public SpiderTask saveOrUpdate(SpiderTask spiderTask) {
        Optional<SpiderTask> optional = spiderTaskRepository.findFirstBySpiderUuidAndDeleted(spiderTask.getSpiderUuid(), false);
        optional.ifPresent(db -> ClassUtil.copyBaseAuditFields(spiderTask, db));
        return spiderTaskRepository.save(spiderTask);
    }

    @Override
    public List<SpiderTask> findAllByUuids(Set<String> uuids) {
        //todo
        return Lists.newArrayList();
    }

    @Override
    public List<SpiderTask> saveAll(List<SpiderTask> entities) {
        //todo
        return Lists.newArrayList();
    }
}
