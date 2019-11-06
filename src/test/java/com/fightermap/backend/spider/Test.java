package com.fightermap.backend.spider;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import org.springframework.util.StopWatch;

import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

public class Test {

    public static void main(String[] args) throws ExecutionException, InterruptedException {
//        LoadingCache<String, Optional<Integer>> cache = CacheBuilder.newBuilder()
//                .maximumSize(100000)
//                .expireAfterWrite(5, TimeUnit.SECONDS)
//                .build(new CacheLoader<String, Optional<Integer>>() {
//                    @Override
//                    public Optional<Integer> load(String key) throws Exception {
//                        return Optional.empty();
//                    }
//                });

        StopWatch watch = new StopWatch();
        watch.start();
        Cache<String,Integer> cache = CacheBuilder.newBuilder()
                .maximumSize(1000000)
                .expireAfterWrite(5,TimeUnit.SECONDS)
                .build();

        cache.put("a", 2);

        int i = 7;
        while (i > 0) {
//            System.out.println(cache.getUnchecked("a").isPresent());
            System.out.println(cache.getIfPresent("a"));
            System.out.println(cache.size());
            TimeUnit.SECONDS.sleep(1);
            i--;
        }
        watch.stop();
        System.out.println(watch.getTotalTimeMillis());
    }
}
