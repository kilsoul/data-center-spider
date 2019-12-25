package com.fightermap.backend.spider.core.service.impl;

import com.fightermap.backend.spider.common.cache.Memory;
import com.fightermap.backend.spider.common.enums.SourceType;
import com.fightermap.backend.spider.common.enums.SpiderTaskStatus;
import com.fightermap.backend.spider.common.exception.NotFoundException;
import com.fightermap.backend.spider.common.util.AsyncUtil;
import com.fightermap.backend.spider.core.component.magic.DatabasePipeline;
import com.fightermap.backend.spider.core.component.magic.LianjiaPageProcessor;
import com.fightermap.backend.spider.core.model.entity.SpiderLog;
import com.fightermap.backend.spider.core.model.entity.SpiderTask;
import com.fightermap.backend.spider.core.service.SpiderLogService;
import com.fightermap.backend.spider.core.service.SpiderService;
import com.fightermap.backend.spider.core.service.SpiderTaskService;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StopWatch;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.SpiderListener;
import us.codecraft.webmagic.processor.PageProcessor;

import java.time.Instant;
import java.util.UUID;

import static com.fightermap.backend.spider.common.util.AsyncUtil.POOL;

/**
 * @author zengqk
 */
@Slf4j
@Service
public class SpiderServiceImpl implements SpiderService {

    private final int retryTimes = 3;

    private final DatabasePipeline databasePipeline;

    private final SpiderLogService spiderLogService;

    private final SpiderTaskService spiderTaskService;

    public SpiderServiceImpl(DatabasePipeline databasePipeline,
                             SpiderLogService spiderLogService,
                             SpiderTaskService spiderTaskService) {
        this.databasePipeline = databasePipeline;
        this.spiderLogService = spiderLogService;
        this.spiderTaskService = spiderTaskService;
    }

    @Override
    public Spider start(SourceType sourceType, String seedUrl, int threadCount) {
        Spider spider = generateSpider(sourceType, seedUrl, threadCount);

        //保存任务信息
        spiderTaskService.saveOrUpdate(SpiderTask.builder()
                .spiderUuid(spider.getUUID())
                .seedUrl(seedUrl)
                .startTime(Instant.now())
                .status(SpiderTaskStatus.RUNNING)
                .build());

        AsyncUtil.acquire(spider::start);

        return spider;
    }

    @Override
    public Spider startSync(SourceType sourceType, String seedUrl, int threadCount) {
        StopWatch watch = new StopWatch();

        Spider spider = generateSpider(sourceType, seedUrl, threadCount);

        SpiderTask spiderTask = spiderTaskService.saveOrUpdate(SpiderTask.builder()
                .spiderUuid(spider.getUUID())
                .seedUrl(seedUrl)
                .startTime(Instant.now())
                .status(SpiderTaskStatus.RUNNING)
                .build());

        watch.start();

        spider.run();

        watch.stop();

        log.info("Spider[uuid={}] used time: {}s.", spider.getUUID(), watch.getTotalTimeSeconds());

        spiderTask.setEndTime(Instant.now());
        spiderTask.setStatus(SpiderTaskStatus.COMPLETED);
        spiderTaskService.saveOrUpdate(spiderTask);
        return spider;
    }

    @Override
    public Spider stop(String uuid) {
        Spider spider = Memory.SPIDER_POOL.getIfPresent(uuid);
        if (spider == null) {
            throw new NotFoundException(String.format("Can't find spider for uuid=%s", uuid));
        }
        spider.stop();

        //保存任务信息
        spiderTaskService.saveOrUpdate(SpiderTask.builder()
                .spiderUuid(spider.getUUID())
                .endTime(Instant.now())
                .status(SpiderTaskStatus.INTERRUPTED)
                .build());
        return spider;
    }

    private Spider generateSpider(SourceType sourceType, String seedUrl, int threadCount) {
        PageProcessor pageProcessor = null;
        if (sourceType == SourceType.LIANJIA) {
            pageProcessor = new LianjiaPageProcessor();
        }
        if (pageProcessor == null) {
            throw new IllegalArgumentException(String.format("Un-support spider for source[%s] and url[%s].", sourceType.name(), seedUrl));
        }
        if (threadCount <= 0) {
            threadCount = 4;
        }
        log.info("Starting spider for source[{}] and url[{}].", sourceType.name(), seedUrl);
        final String uuid = UUID.randomUUID().toString();
        Spider spider = Spider.create(pageProcessor)
                .setUUID(uuid)
                .addUrl(seedUrl)
                .addPipeline(databasePipeline)
                .thread(POOL, threadCount);

        spider.setSpiderListeners(Lists.newArrayList(new SpiderListener() {
            @Override
            public void onSuccess(Request request) {
                String url = request.getUrl();

                spiderLogService.saveAsync(SpiderLog.builder()
                        .spiderUuid(uuid)
                        .url(url)
                        .occurTime(Instant.now())
                        .success(false)
                        .build());
            }

            @Override
            public void onError(Request request) {
                //失败需要重新爬取
                String url = request.getUrl();

                Integer value = (Integer) request.getExtra(Request.CYCLE_TRIED_TIMES);
                value = value == null ? 1 : value;

                if (value < retryTimes) {
                    value++;
                    request.putExtra(Request.CYCLE_TRIED_TIMES, value);
                    spider.addRequest(request);
                } else {
                    Memory.remove(url);
                    //保存失败的任务
                    spiderLogService.saveAsync(SpiderLog.builder()
                            .spiderUuid(uuid)
                            .url(url)
                            .occurTime(Instant.now())
                            .success(false)
                            .build());
                }
            }
        }));

        Memory.SPIDER_POOL.put(uuid, spider);

        return spider;
    }
}
