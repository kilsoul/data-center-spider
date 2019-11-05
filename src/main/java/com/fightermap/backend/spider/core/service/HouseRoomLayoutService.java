package com.fightermap.backend.spider.core.service;

import com.fightermap.backend.spider.common.enums.SourceType;
import com.fightermap.backend.spider.core.model.bo.spider.HouseDetailInfo;
import com.fightermap.backend.spider.core.model.entity.HouseRoomLayout;

import java.util.List;

/**
 * @author zengqk
 */
public interface HouseRoomLayoutService {

    List<HouseRoomLayout> findBySourceTypeAndHouseBaseIdIn(SourceType sourceType, List<String> houseBaseIds);

    List<HouseRoomLayout> saveAll(List<HouseRoomLayout> houseRoomLayoutList);

    List<HouseRoomLayout> merge(List<HouseRoomLayout> rawList, List<HouseRoomLayout> dbList);

    List<HouseRoomLayout> save(HouseDetailInfo houseDetailInfo, Long houseBaseId);
}
