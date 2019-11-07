package com.fightermap.backend.spider.common.util;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.function.Supplier;

/**
 * @author zengqk
 */
@Slf4j
public class AsyncUtil {
    public static final ExecutorService POOL = Executors.newFixedThreadPool(8);

    public static <T> CompletableFuture<T> acquire(Supplier<T> supplier) {
        return CompletableFuture.supplyAsync(supplier, POOL);
    }

    public static CompletableFuture<Void> acquire(Runnable runnable) {
        return CompletableFuture.runAsync(runnable, POOL);
    }

    public static <T> T execute(CompletableFuture<T> task, String taskName, T defaultValue) {
        try {
            return task.get(3, TimeUnit.SECONDS);
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            log.error("Executing was failed for task[name={}]!", taskName, e);
        }
        return defaultValue;
    }
}
