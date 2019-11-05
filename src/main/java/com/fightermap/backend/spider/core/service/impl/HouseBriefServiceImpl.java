package com.fightermap.backend.spider.core.service.impl;

import com.fightermap.backend.spider.common.enums.SourceType;
import com.fightermap.backend.spider.core.model.entity.HouseBrief;
import com.fightermap.backend.spider.core.repository.HouseBriefRepository;
import com.fightermap.backend.spider.core.service.HouseBriefService;
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
public class HouseBriefServiceImpl implements HouseBriefService {

    private final HouseBriefRepository houseBriefRepository;

    public HouseBriefServiceImpl(HouseBriefRepository houseBriefRepository) {
        this.houseBriefRepository = houseBriefRepository;
    }

    @Override
    public List<HouseBrief> findBySourceTypeAndHouseIdIn(SourceType sourceType, List<String> houseIds) {
        if (CollectionUtils.isEmpty(houseIds)) {
            return Collections.emptyList();
        }
        return houseBriefRepository.findAllBySourceTypeAndHouseIdInAndDeletedFalse(sourceType, houseIds);
    }

    @Override
    public List<HouseBrief> saveAll(List<HouseBrief> houseBriefList) {
        if (CollectionUtils.isEmpty(houseBriefList)) {
            return Collections.emptyList();
        }
        return houseBriefRepository.saveAll(houseBriefList);
    }

    @Override
    public List<HouseBrief> merge(List<HouseBrief> rawList, List<HouseBrief> dbList) {
        Map<String, HouseBrief> dbInfoMap = dbList.stream().collect(Collectors.toMap(HouseBrief::getHouseId, Function.identity()));
        return rawList.stream().filter(raw -> {
            String houseId = raw.getHouseId();
            HouseBrief db = dbInfoMap.get(houseId);
            //取字段变更的
            return !raw.equals(db);
        }).collect(Collectors.toList())
                .stream()
                .peek(raw -> {
                    String houseId = raw.getHouseId();
                    HouseBrief db = dbInfoMap.get(houseId);
                    if (db != null) {
                        raw.setId(db.getId());
                        raw.setCreatedBy(db.getCreatedBy());
                        raw.setCreatedAt(db.getCreatedAt());
                        raw.setVersion(db.getVersion());
                    }
                }).collect(Collectors.toList());
    }
}
