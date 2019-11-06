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
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.TreeSet;
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
    public List<Area> findAllBySourceTypeAndPathIn(SourceType sourceType, AreaType areaType, List<String> paths) {
        if (CollectionUtils.isEmpty(paths)) {
            return Collections.emptyList();
        }
        return areaRepository.findAllBySourceTypeAndTypeAndPathInAndDeletedFalse(sourceType, areaType, paths);
    }

    @Override
    public List<Area> findAllByPathIn(SourceType sourceType, List<String> paths) {
        if (CollectionUtils.isEmpty(paths)) {
            return Collections.emptyList();
        }
        return areaRepository.findAllBySourceTypeAndPathInAndDeletedFalse(sourceType, paths);
    }

    @Override
    public Optional<Area> findByPath(SourceType sourceType, String path) {
        return areaRepository.findFirstBySourceTypeAndPathAndDeletedFalse(sourceType, path);
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
        Map<String, Area> dbInfoMap = dbList.stream()
                .distinct()
                .collect(Collectors.collectingAndThen(
                        Collectors.toCollection(() -> new TreeSet<>(Comparator.comparing(Area::getName))),
                        tree -> tree.stream().collect(Collectors.toMap(Area::getName, Function.identity()))
                ));
        return rawList.stream().filter(raw -> {
            String name = raw.getName();
            Area db = dbInfoMap.get(name);
            //取字段变更的
            return !raw.equals(db);
        }).collect(Collectors.toList())
                .stream()
                .peek(raw -> {
                    String name = raw.getName();
                    Area db = dbInfoMap.get(name);
                    if (db != null) {
                        raw.setId(db.getId());
                        raw.setCreatedBy(db.getCreatedBy());
                        raw.setCreatedAt(db.getCreatedAt());
                        raw.setVersion(db.getVersion());
                    }
                }).collect(Collectors.toList());
    }

    @Override
    public List<Area> findAreaByType(SourceType sourceType, AreaType areaType) {
        return areaRepository.findAllBySourceTypeAndTypeAndDeletedFalse(sourceType, areaType);
    }
}
