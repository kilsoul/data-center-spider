package com.fightermap.backend.spider.core.service;

import com.fightermap.backend.spider.common.enums.AreaType;
import com.fightermap.backend.spider.common.enums.SourceType;
import com.fightermap.backend.spider.core.model.entity.Area;

import java.util.List;
import java.util.Optional;

/**
 * @author zengqk
 */
public interface AreaService {

    List<Area> findAllBySourceTypeAndPathIn(SourceType sourceType, AreaType areaType, List<String> paths);


    List<Area> findAllByPathIn(SourceType sourceType, List<String> paths);

    Optional<Area> findByPath(SourceType sourceType, String path);

    List<Area> findAllByIdIn(List<Long> ids);

    List<Area> saveAll(List<Area> areaList);

    List<Area> merge(List<Area> rawList, List<Area> dbList);

    List<Area> findAreaByType(SourceType sourceType, AreaType areaType);
}
