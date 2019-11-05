package com.fightermap.backend.spider.common.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;

/**
 * @author zengqk
 */
@Slf4j
public class JsonUtil {

    public static <T> String writeToJson(T t, ObjectMapper mapper) {
        String result = null;
        if (t == null) {
            return null;
        }
        try {
            result = mapper.writeValueAsString(t);
        } catch (Exception e) {
            log.warn("Failed to convert object[{}] to json string!", t, e);
        }
        return result;
    }
}
