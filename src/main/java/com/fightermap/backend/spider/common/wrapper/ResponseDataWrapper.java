package com.fightermap.backend.spider.common.wrapper;

/**
 * @author zengqk
 */
public class ResponseDataWrapper<T> {

    private ResponseDataWrapper(T data) {
        this.data = data;
    }

    private T data;

    public static <T> ResponseDataWrapper<T> wrap(T data) {
        return new ResponseDataWrapper<>(data);
    }

    public T getData() {
        return data;
    }
}
