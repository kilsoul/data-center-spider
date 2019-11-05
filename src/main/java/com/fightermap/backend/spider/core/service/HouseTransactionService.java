package com.fightermap.backend.spider.core.service;

import com.fightermap.backend.spider.common.enums.SourceType;
import com.fightermap.backend.spider.core.model.bo.spider.HouseDetailInfo;
import com.fightermap.backend.spider.core.model.entity.HouseTransaction;

import java.util.List;

/**
 * @author zengqk
 */
public interface HouseTransactionService {

    List<HouseTransaction> findBySourceTypeAndHouseBaseIdIn(SourceType sourceType, List<Long> houseBaseIds);

    List<HouseTransaction> saveAll(List<HouseTransaction> houseTransactionList);

    List<HouseTransaction> merge(List<HouseTransaction> rawList, List<HouseTransaction> dbList);

    HouseTransaction save(HouseDetailInfo houseDetailInfo,Long houseBaseId);
}
