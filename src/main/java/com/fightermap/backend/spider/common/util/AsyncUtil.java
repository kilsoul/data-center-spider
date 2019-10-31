package com.fightermap.backend.spider.common.util;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.function.Supplier;

/**
 * @author zengqk
 */
@Slf4j
public class AsyncUtil {
    private static final Executor POOL = Executors.newFixedThreadPool(8);

    public static <T> CompletableFuture<T> acquire(Supplier<T> supplier) {
        return CompletableFuture.supplyAsync(supplier);
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
