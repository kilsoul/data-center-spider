package com.fightermap.backend.spider.core.job;

import com.fightermap.backend.spider.common.cache.Memory;
import com.fightermap.backend.spider.common.enums.SourceType;
import com.fightermap.backend.spider.common.enums.SpiderTaskStatus;
import com.fightermap.backend.spider.core.model.entity.SpiderTask;
import com.fightermap.backend.spider.core.service.SpiderLogService;
import com.fightermap.backend.spider.core.service.SpiderService;
import com.fightermap.backend.spider.core.service.SpiderTaskService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.Spider;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentMap;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.fightermap.backend.spider.common.enums.SpiderTaskStatus.COMPLETED;
import static com.fightermap.backend.spider.common.enums.SpiderTaskStatus.INITIAL;
import static com.fightermap.backend.spider.common.enums.SpiderTaskStatus.INTERRUPTED;
import static com.fightermap.backend.spider.common.enums.SpiderTaskStatus.RUNNING;

@Slf4j
@Component
public class SchedulerJob {

    private final SpiderService spiderService;

    private final SpiderTaskService spiderTaskService;

    private final SpiderLogService spiderLogService;

    public SchedulerJob(SpiderService spiderService,
                        SpiderTaskService spiderTaskService,
                        SpiderLogService spiderLogService) {
        this.spiderService = spiderService;
        this.spiderTaskService = spiderTaskService;
        this.spiderLogService = spiderLogService;
    }

    @Scheduled(cron = "${fightermap.scheduler.spiders.lian-jia.cron}")
    public void collectLianJia() {
        spiderService.startSync(SourceType.LIANJIA, "https://sh.lianjia.com/ershoufang/shanyang", 4);
    }

    @Scheduled(cron = "${fightermap.scheduler.spiders.check-spider-task.cron}")
    public void checkSpiderTask() {
        List<SpiderTask> savingSpiderTask = new ArrayList<>();
        ConcurrentMap<String, Spider> spiderPoolMap = Memory.SPIDER_POOL.asMap();

        List<SpiderTask> allSpiderTasks = spiderTaskService.findAllByUuids(spiderPoolMap.keySet());
        Map<String, SpiderTask> allSpiderTaskMap = allSpiderTasks.stream()
                .collect(Collectors.toMap(SpiderTask::getSpiderUuid, Function.identity()));
        spiderPoolMap.forEach((uuid, spider) -> {
            SpiderTask spiderTask = allSpiderTaskMap.get(uuid);
            if (spiderTask == null) {
                return;
            }
            SpiderTaskStatus newStatus = SpiderTaskStatus.mappingSpiderStatus(spider.getStatus());
            SpiderTaskStatus existStatus = spiderTask.getStatus();
            if (existStatus == INITIAL && newStatus == RUNNING) {
                spiderTask.setStartTime(Instant.now());
                spiderTask.setStatus(newStatus);
                savingSpiderTask.add(spiderTask);
            } else if ((existStatus == RUNNING || existStatus == INTERRUPTED) && newStatus == COMPLETED) {
                spiderTask.setEndTime(Instant.now());
                spiderTask.setDuration(Duration.between(spiderTask.getStartTime(), spiderTask.getEndTime()).getSeconds());
//                spiderTask.setSuccessCount();
//                spiderTask.setFailCount();
                spiderTask.setStatus(newStatus);
                savingSpiderTask.add(spiderTask);
            }
        });

        spiderTaskService.saveAll(savingSpiderTask);
    }
}
