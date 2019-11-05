package com.fightermap.backend.spider.common.util;

/**
 * @author zengqk
 */
public class StringUtil {

    private StringUtil() {
    }

    public static String generateKey(String... keys) {
        return String.join("-", keys);
    }
}
