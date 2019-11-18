package com.fightermap.backend.spider.core.job;

import com.fightermap.backend.spider.common.enums.SourceType;
import com.fightermap.backend.spider.core.service.SpiderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class SchedulerJob {

    private final SpiderService spiderService;

    public SchedulerJob(SpiderService spiderService) {
        this.spiderService = spiderService;
    }

    @Scheduled(cron = "${fightermap.spider.scheduler.default}")
    public void spiderLianjia() {
        spiderService.start(SourceType.LIANJIA, "https://sh.lianjia.com/ershoufang/shanyang", 4);
    }
}
