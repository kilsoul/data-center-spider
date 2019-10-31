package com.fightermap.backend.spider.common.cache;

import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * 数据存储，暂为内存
 *
 * @author zengqk
 */
public class Memory {

    /**
     * 已处理的URL池
     */
    public static final ConcurrentHashMap<String, Integer> PROCESSED_URL_POOL = new ConcurrentHashMap<>();

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
                isNew = PROCESSED_URL_POOL.putIfAbsent(url, 1) == null;
            }
            if (isNew) {
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
}
