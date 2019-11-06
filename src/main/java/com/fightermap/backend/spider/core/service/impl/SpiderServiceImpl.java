package com.fightermap.backend.spider.core.service.impl;

import com.fightermap.backend.spider.common.cache.Memory;
import com.fightermap.backend.spider.common.enums.SourceType;
import com.fightermap.backend.spider.core.component.magic.DatabasePipeline;
import com.fightermap.backend.spider.core.component.magic.LianjiaPageProcessor;
import com.fightermap.backend.spider.core.service.SpiderService;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.SpiderListener;
import us.codecraft.webmagic.processor.PageProcessor;

import static com.fightermap.backend.spider.common.util.AsyncUtil.POOL;

/**
 * @author zengqk
 */
@Slf4j
@Service
public class SpiderServiceImpl implements SpiderService {

    private final DatabasePipeline databasePipeline;

    public SpiderServiceImpl(DatabasePipeline databasePipeline) {
        this.databasePipeline = databasePipeline;
    }

    @Override
    public void start(SourceType sourceType, String seedUrl, int threadCount) {
        PageProcessor pageProcessor = null;
        if (sourceType == SourceType.LIANJIA) {
            pageProcessor = new LianjiaPageProcessor();
        }
        if (pageProcessor == null) {
            throw new IllegalArgumentException(String.format("Unsupport spider for source[%s] and url[%s].", sourceType.name(), seedUrl));
        }
        if (threadCount < 0) {
            threadCount = 4;
        }
        log.info("Starting spider for source[{}] and url[{}].", sourceType.name(), seedUrl);
        Spider.create(pageProcessor)
                .addUrl(seedUrl)
                .addPipeline(databasePipeline)
                .thread(POOL, threadCount)
                .setSpiderListeners(Lists.newArrayList(new SpiderListener() {
                    @Override
                    public void onSuccess(Request request) {
                    }

                    @Override
                    public void onError(Request request) {
                        //失败需要重新爬取
                        String url = request.getUrl();
                        Memory.remove(url);
                    }
                }))
                .start();
    }
}
