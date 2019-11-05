package com.fightermap.backend.spider.core.service;

import com.fightermap.backend.spider.common.enums.SourceType;
import com.fightermap.backend.spider.core.model.bo.spider.HouseDetailInfo;
import com.fightermap.backend.spider.core.model.entity.HousePhoto;

import java.util.List;

/**
 * @author zengqk
 */
public interface HousePhotoService {

    List<HousePhoto> findBySourceTypeAndHouseBaseIdIn(SourceType sourceType, List<Long> houseBaseIds);

    List<HousePhoto> saveAll(List<HousePhoto> housePhotoList);

    List<HousePhoto> merge(List<HousePhoto> rawList, List<HousePhoto> dbList);

    List<HousePhoto> save(HouseDetailInfo houseDetailInfo, Long houseBaseId);
}
