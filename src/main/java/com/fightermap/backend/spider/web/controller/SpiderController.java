package com.fightermap.backend.spider.web.controller;

import com.fightermap.backend.spider.common.enums.SourceType;
import com.fightermap.backend.spider.core.service.SpiderService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;

/**
 * @author zengqk
 */
@RequestMapping(path = "/api/v1/spider", produces = APPLICATION_JSON_UTF8_VALUE)
@RestController
public class SpiderController {

    private final SpiderService spiderService;

    public SpiderController(SpiderService spiderService) {
        this.spiderService = spiderService;
    }

    @GetMapping(path = "/start")
    public void start(String sourceType, String seedUrl) {
        spiderService.start(SourceType.map(sourceType), seedUrl);
    }

}
