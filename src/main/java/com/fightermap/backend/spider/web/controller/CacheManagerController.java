package com.fightermap.backend.spider.web.controller;

import com.fightermap.backend.spider.common.cache.Memory;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import us.codecraft.webmagic.Spider;

import java.util.Set;

import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;

/**
 * @author zengqk
 */
@RequestMapping(path = "/caches", produces = APPLICATION_JSON_UTF8_VALUE)
@RestController
public class CacheManagerController {

    @GetMapping(path = "/spider/keys")
    public Set<String> getSpiderKeys() {
        return Memory.SPIDER_POOL.asMap().keySet();
    }

    @GetMapping(path = "/spider/keys/{key}", produces = APPLICATION_JSON_UTF8_VALUE)
    public Spider getSpiderByKey(@PathVariable("key") String key) {
        return Memory.SPIDER_POOL.getIfPresent(key);
    }

    @DeleteMapping(path = "/spider/keys/{key}")
    public void deleteSpiderByKey(@PathVariable("key") String key) {
        Memory.SPIDER_POOL.invalidate(key);
    }

    @DeleteMapping(path = "/spider/keys/all")
    public void deleteSpiderAll() {
        Memory.SPIDER_POOL.invalidateAll();
    }

    @GetMapping(path = "/processed-url/keys")
    public Set<String> getProcessedUrlKeys() {
        return Memory.PROCESSED_URL_POOL.asMap().keySet();
    }

    @GetMapping(path = "/processed-url/keys/{key}", produces = APPLICATION_JSON_UTF8_VALUE)
    public Integer getProcessedUrlByKey(@PathVariable("key") String key) {
        return Memory.PROCESSED_URL_POOL.getIfPresent(key);
    }

    @DeleteMapping(path = "/processed-url/keys/{key}")
    public void deleteProcessedUrlByKey(@PathVariable("key") String key) {
        Memory.PROCESSED_URL_POOL.invalidate(key);
    }

    @DeleteMapping(path = "/processed-url/keys/all")
    public void deleteProcessedUrlAll() {
        Memory.PROCESSED_URL_POOL.invalidateAll();
    }

}
