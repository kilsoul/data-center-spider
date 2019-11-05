package com.fightermap.backend.spider.core.service;

import com.fightermap.backend.spider.common.enums.SourceType;
import com.fightermap.backend.spider.core.model.entity.HouseBrief;

import java.util.List;

/**
 * @author zengqk
 */
public interface HouseBriefService {

    List<HouseBrief> findBySourceTypeAndHouseIdIn(SourceType sourceType, List<String> houseIds);

    List<HouseBrief> saveAll(List<HouseBrief> houseBriefList);

    List<HouseBrief> merge(List<HouseBrief> rawList, List<HouseBrief> dbList);
}
