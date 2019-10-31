package com.fightermap.backend.spider.common.util;

import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;

/**
 * @author zengqk
 */
@Slf4j
public class ClassUtil {

    public static Class getFieldType(Object target, String fieldName) {
        Class clazz = target.getClass();
        try {
            Field field;
            field = clazz.getDeclaredField(fieldName);
            return field.getType();
        } catch (NoSuchFieldException e) {
            log.error("There were no field[name={}] for class[{}]!", fieldName, clazz, e);
        }
        return null;
    }

    public static void setField(Object target, String fieldName, Object value) {
        Class clazz = target.getClass();
        try {
            Field field;
            field = clazz.getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(target, value);
        } catch (NoSuchFieldException e) {
            log.error("There were no field[name={}] for class[{}]!", fieldName, clazz, e);
        } catch (IllegalAccessException e) {
            log.error("Can't access field[name={}] for class[{}]!", fieldName, clazz, e);
        }
    }
}
