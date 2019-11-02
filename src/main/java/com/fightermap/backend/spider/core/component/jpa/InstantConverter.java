package com.fightermap.backend.spider.core.component.jpa;

import lombok.extern.slf4j.Slf4j;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

/**
 * Instant转上海时间
 *
 * @author zengqk
 */
@Slf4j
@Converter(autoApply = true)
public class InstantConverter implements AttributeConverter<Instant, String> {
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.S");
    private final ZoneId timezone = ZoneId.of("Asia/Shanghai");

    @Override
    public String convertToDatabaseColumn(Instant attribute) {
        return LocalDateTime.ofInstant(attribute, timezone).format(formatter);
    }

    @Override
    public Instant convertToEntityAttribute(String dbData) {
        return LocalDateTime.parse(dbData, formatter).atZone(timezone).toInstant();
    }
}
