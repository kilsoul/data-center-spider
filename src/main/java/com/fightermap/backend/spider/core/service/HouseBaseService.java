package com.fightermap.backend.spider.core.service;

import com.fightermap.backend.spider.common.enums.SourceType;
import com.fightermap.backend.spider.core.model.bo.spider.HouseDetailInfo;
import com.fightermap.backend.spider.core.model.entity.HouseBase;

import java.util.List;
import java.util.Optional;

/**
 * @author zengqk
 */
public interface HouseBaseService {

    List<HouseBase> findBySourceTypeAndHouseIdIn(SourceType sourceType, List<String> houseIds);

    List<HouseBase> saveAll(List<HouseBase> houseBaseList);

    List<HouseBase> merge(List<HouseBase> rawList, List<HouseBase> dbList);

    Optional<HouseBase> findBySourceTypeAndHouseId(SourceType sourceType,String houseId);

    HouseBase save(HouseDetailInfo houseDetailInfo,Long areaId);
}
