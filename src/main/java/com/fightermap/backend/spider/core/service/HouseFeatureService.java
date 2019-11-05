package com.fightermap.backend.spider.core.service;

import com.fightermap.backend.spider.common.enums.SourceType;
import com.fightermap.backend.spider.core.model.bo.spider.HouseDetailInfo;
import com.fightermap.backend.spider.core.model.entity.HouseFeature;

import java.util.List;

/**
 * @author zengqk
 */
public interface HouseFeatureService {

    List<HouseFeature> findBySourceTypeAndHouseBaseIdIn(SourceType sourceType, List<Long> houseBaseIds);

    List<HouseFeature> saveAll(List<HouseFeature> houseFeatureList);

    List<HouseFeature> merge(List<HouseFeature> rawList, List<HouseFeature> dbList);

    HouseFeature save(HouseDetailInfo houseDetailInfo,Long houseBaseId);
}
