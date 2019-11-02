package com.fightermap.backend.spider.core.service.impl;

import com.fightermap.backend.spider.common.enums.SourceType;
import com.fightermap.backend.spider.core.component.magic.DatabasePipeline;
import com.fightermap.backend.spider.core.component.magic.LianjiaPageProcessor;
import com.fightermap.backend.spider.core.service.SpiderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import us.codecraft.webmagic.Spider;
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
    public void start(SourceType sourceType, String seedUrl) {
        PageProcessor pageProcessor = null;
        if (sourceType == SourceType.LIANJIA) {
            pageProcessor = new LianjiaPageProcessor();
        }
        if (pageProcessor == null) {
            throw new IllegalArgumentException(String.format("Unsupport spider for source[%s] and url[%s].", sourceType.name(), seedUrl));
        }
        log.info("Starting spider for source[{}] and url[{}].", sourceType.name(), seedUrl);
        Spider.create(pageProcessor)
                .addUrl(seedUrl)
                .addPipeline(databasePipeline)
                .thread(POOL,1)
                .run();
    }
}
