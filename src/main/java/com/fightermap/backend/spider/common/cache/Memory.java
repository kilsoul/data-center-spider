package com.fightermap.backend.spider.common.cache;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import org.apache.commons.lang3.StringUtils;
import us.codecraft.webmagic.Spider;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * 数据存储，暂为内存
 *
 * @author zengqk
 */
public class Memory {

    public static final Cache<String, Spider> SPIDER_POOL = CacheBuilder.newBuilder()
            .maximumSize(1000000)
            .expireAfterWrite(1, TimeUnit.DAYS)
            .build();

    /**
     * 已处理的URL池
     */
    public static final Cache<String, Integer> PROCESSED_URL_POOL = CacheBuilder.newBuilder()
            .maximumSize(1000000)
            .expireAfterWrite(1, TimeUnit.DAYS)
            .build();

    /**
     * 单条添加，如果重复则返回null
     *
     * @param url 需要去重的url
     * @return 新url返回原对象；否则返回null
     */
    public static String add(String url) {
        synchronized (Memory.class) {
            boolean isNew = false;
            if (!StringUtils.isEmpty(url)) {
                isNew = PROCESSED_URL_POOL.getIfPresent(url) == null;
            }
            if (isNew) {
                PROCESSED_URL_POOL.put(url, 1);
                return url;
            }
            return null;
        }
    }

    /**
     * 批量添加，返回去重后的url
     *
     * @param urls 未去重的url列表
     * @return 去重后的url列表
     */
    public static List<String> addAll(List<String> urls) {
        return urls.stream().filter(url -> add(url) != null).collect(Collectors.toList());
    }

    /**
     * 移除缓存的url
     *
     * @param url
     */
    public static void remove(String url) {
        PROCESSED_URL_POOL.invalidate(url);
    }

    /**
     * 获取url缓存的值
     *
     * @param url
     * @return
     */
    public static Integer get(String url) {
        return PROCESSED_URL_POOL.getIfPresent(url);
    }
}
