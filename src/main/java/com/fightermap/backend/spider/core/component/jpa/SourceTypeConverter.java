package com.fightermap.backend.spider.core.component.jpa;

import com.fightermap.backend.spider.common.enums.SourceType;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

/**
 * @author zengqk
 */
@Slf4j
@Converter(autoApply = true)
public class SourceTypeConverter implements AttributeConverter<SourceType, String> {

    @Override
    public String convertToDatabaseColumn(SourceType attribute) {
        return attribute.name();
    }

    @Override
    public SourceType convertToEntityAttribute(String dbData) {
        return SourceType.valueOf(dbData);
    }
}
