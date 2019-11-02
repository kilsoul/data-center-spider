package com.fightermap.backend.spider.core.service.impl;

import com.fightermap.backend.spider.common.enums.AreaType;
import com.fightermap.backend.spider.common.enums.SourceType;
import com.fightermap.backend.spider.core.model.entity.Area;
import com.fightermap.backend.spider.core.repository.AreaRepository;
import com.fightermap.backend.spider.core.service.AreaService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author zengqk
 */
@Slf4j
@Service
public class AreaServiceImpl implements AreaService {

    private final AreaRepository areaRepository;

    public AreaServiceImpl(AreaRepository areaRepository) {
        this.areaRepository = areaRepository;
    }


    @Override
    public List<Area> findAllBySourceTypeAndNameIn(SourceType sourceType, AreaType areaType, List<String> names) {
        if (CollectionUtils.isEmpty(names)) {
            return Collections.emptyList();
        }
        return areaRepository.findAllBySourceTypeAndTypeAndNameInAndDeletedFalse(sourceType, areaType, names);
    }

    @Override
    public List<Area> findAllByIdIn(List<Long> ids) {
        if (CollectionUtils.isEmpty(ids)) {
            return Collections.emptyList();
        }
        return areaRepository.findAllByIdIn(ids);
    }

    @Override
    public List<Area> saveAll(List<Area> areaList) {
        return areaRepository.saveAll(areaList);
    }

    @Override
    public List<Area> merge(List<Area> rawList, List<Area> dbList) {
        Map<String, Area> dbInfoMap = dbList.stream().collect(Collectors.toMap(Area::getName, Function.identity()));
        return rawList.stream().filter(raw -> {
            String name = raw.getName();
            Area db = dbInfoMap.get(name);
            //取字段变更的
            return !raw.equals(db);
        }).collect(Collectors.toList());
    }

    @Override
    public List<Area> findAreaByType(SourceType sourceType, AreaType areaType) {
        return areaRepository.findAllBySourceTypeAndTypeAndDeletedFalse(sourceType, areaType);
    }
}
