package com.fightermap.backend.spider.core.service.impl;

import com.fightermap.backend.spider.common.util.AsyncUtil;
import com.fightermap.backend.spider.core.model.entity.SpiderLog;
import com.fightermap.backend.spider.core.repository.SpiderLogRepository;
import com.fightermap.backend.spider.core.service.SpiderLogService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Slf4j
@Service
public class SpiderLogServiceImpl implements SpiderLogService {

    private final SpiderLogRepository spiderLogRepository;

    public SpiderLogServiceImpl(SpiderLogRepository spiderLogRepository) {
        this.spiderLogRepository = spiderLogRepository;
    }

    @Override
    public SpiderLog save(SpiderLog spiderLog) {
        if (spiderLog == null) {
            return null;
        }
        return spiderLogRepository.save(spiderLog);
    }

    @Override
    public void saveAsync(SpiderLog spiderLog) {
        CompletableFuture<SpiderLog> future = AsyncUtil.acquire(() -> spiderLogRepository.save(spiderLog));

        AsyncUtil.execute(future, "save spider log", null);
    }
}
