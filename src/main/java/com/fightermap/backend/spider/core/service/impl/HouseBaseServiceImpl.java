package com.fightermap.backend.spider.core.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fightermap.backend.spider.common.enums.SourceType;
import com.fightermap.backend.spider.common.util.JsonUtil;
import com.fightermap.backend.spider.core.model.bo.spider.HouseDetailInfo;
import com.fightermap.backend.spider.core.model.entity.HouseBase;
import com.fightermap.backend.spider.core.repository.HouseBaseRepository;
import com.fightermap.backend.spider.core.service.HouseBaseService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author zengqk
 */
@Slf4j
@Service
public class HouseBaseServiceImpl implements HouseBaseService {

    private final ObjectMapper objectMapper;

    private final HouseBaseRepository houseBaseRepository;

    public HouseBaseServiceImpl(ObjectMapper objectMapper,
                                HouseBaseRepository houseBaseRepository) {
        this.objectMapper = objectMapper;
        this.houseBaseRepository = houseBaseRepository;
    }

    @Override
    public List<HouseBase> findBySourceTypeAndHouseIdIn(SourceType sourceType, List<String> houseIds) {
        if (CollectionUtils.isEmpty(houseIds)) {
            return Collections.emptyList();
        }
        return houseBaseRepository.findAllBySourceTypeAndHouseIdInAndDeletedFalse(sourceType, houseIds);
    }

    @Override
    public List<HouseBase> saveAll(List<HouseBase> houseBaseList) {
        if (CollectionUtils.isEmpty(houseBaseList)) {
            return Collections.emptyList();
        }
        return houseBaseRepository.saveAll(houseBaseList);
    }

    @Override
    public List<HouseBase> merge(List<HouseBase> rawList, List<HouseBase> dbList) {
        Map<String, HouseBase> dbInfoMap = dbList.stream().collect(Collectors.toMap(HouseBase::getHouseId, Function.identity()));
        return rawList.stream().filter(raw -> {
            String houseId = raw.getHouseId();
            HouseBase db = dbInfoMap.get(houseId);
            //取字段变更的
            return !raw.equals(db);
        }).collect(Collectors.toList())
                .stream()
                .peek(raw -> {
                    String houseId = raw.getHouseId();
                    HouseBase db = dbInfoMap.get(houseId);
                    if (db != null) {
                        raw.setId(db.getId());
                        raw.setCreatedBy(db.getCreatedBy());
                        raw.setCreatedAt(db.getCreatedAt());
                        raw.setVersion(db.getVersion());
                    }
                }).collect(Collectors.toList());
    }

    @Override
    public Optional<HouseBase> findBySourceTypeAndHouseId(SourceType sourceType, String houseId) {
        return houseBaseRepository.findFirstBySourceTypeAndHouseIdAndDeletedFalse(sourceType, houseId);
    }

    @Override
    public HouseBase save(HouseDetailInfo houseDetailInfo, Long areaId) {
        HouseBase result;
        SourceType sourceType = houseDetailInfo.getSourceType();
        HouseDetailInfo.BaseInfo baseInfo = houseDetailInfo.getBaseInfo();
        HouseBase houseBase = HouseBase.builder()
                .sourceType(sourceType)
                .houseId(houseDetailInfo.getId())
                .roomType(baseInfo.getRoomType())
                .floorInfo(baseInfo.getFloorInfo())
                .constructionArea(baseInfo.getConstructionArea())
                .actualArea(baseInfo.getActualArea())
                .houseStructure(baseInfo.getHouseStructure())
                .buildingType(baseInfo.getBuildingType())
                .orientation(baseInfo.getOrientation())
                .buildingStructureType(baseInfo.getBuildingStructureType())
                .decorationType(baseInfo.getDecorationType())
                .liftHouseScale(baseInfo.getLiftHouseScale())
                .hasLift(baseInfo.getHasLift())
                .houseHoldYears(baseInfo.getHouseHoldYears())
                .areaId(areaId)
                .areaInfo(houseDetailInfo.getAreaInfo())
                .url(houseDetailInfo.getUrl())
                .unknownField(JsonUtil.writeToJson(baseInfo.getUnknownField(), objectMapper))
                .build();

        HouseBase dbInfo = houseBaseRepository.findFirstBySourceTypeAndHouseIdAndDeletedFalse(sourceType, houseBase.getHouseId()).orElse(null);

        if (dbInfo == null) {
            result = houseBaseRepository.save(houseBase);
        } else if (!dbInfo.equals(houseBase)) {
            houseBase.setId(dbInfo.getId());
            houseBase.setCreatedBy(dbInfo.getCreatedBy());
            houseBase.setCreatedAt(dbInfo.getCreatedAt());
            houseBase.setVersion(dbInfo.getVersion());
            result = houseBaseRepository.save(houseBase);
        } else {
            result = dbInfo;
        }
        return result;
    }
}
