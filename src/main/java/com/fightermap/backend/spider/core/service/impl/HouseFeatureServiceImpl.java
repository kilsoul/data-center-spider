package com.fightermap.backend.spider.core.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fightermap.backend.spider.common.enums.SourceType;
import com.fightermap.backend.spider.common.util.JsonUtil;
import com.fightermap.backend.spider.core.model.bo.spider.HouseDetailInfo;
import com.fightermap.backend.spider.core.model.entity.HouseFeature;
import com.fightermap.backend.spider.core.repository.HouseFeatureRepository;
import com.fightermap.backend.spider.core.service.HouseFeatureService;
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
public class HouseFeatureServiceImpl implements HouseFeatureService {

    private final ObjectMapper objectMapper;

    private final HouseFeatureRepository houseFeatureRepository;

    public HouseFeatureServiceImpl(ObjectMapper objectMapper,
                                   HouseFeatureRepository houseFeatureRepository) {
        this.objectMapper = objectMapper;
        this.houseFeatureRepository = houseFeatureRepository;
    }

    @Override
    public List<HouseFeature> findBySourceTypeAndHouseBaseIdIn(SourceType sourceType, List<Long> houseBaseIds) {
        if (CollectionUtils.isEmpty(houseBaseIds)) {
            return Collections.emptyList();
        }
        return houseFeatureRepository.findAllBySourceTypeAndHouseBaseIdInAndDeletedFalse(sourceType, houseBaseIds);
    }

    @Override
    public List<HouseFeature> saveAll(List<HouseFeature> houseFeatureList) {
        if (CollectionUtils.isEmpty(houseFeatureList)) {
            return Collections.emptyList();
        }
        return houseFeatureRepository.saveAll(houseFeatureList);
    }

    @Override
    public List<HouseFeature> merge(List<HouseFeature> rawList, List<HouseFeature> dbList) {
        Map<Long, HouseFeature> dbInfoMap = dbList.stream().collect(Collectors.toMap(HouseFeature::getHouseBaseId, Function.identity()));
        return rawList.stream().filter(raw -> {
            Long houseBaseId = raw.getHouseBaseId();
            HouseFeature db = dbInfoMap.get(houseBaseId);
            //取字段变更的
            return !raw.equals(db);
        }).collect(Collectors.toList())
                .stream()
                .peek(raw -> {
                    Long houseBaseId = raw.getHouseBaseId();
                    HouseFeature db = dbInfoMap.get(houseBaseId);
                    if (db != null) {
                        raw.setId(db.getId());
                        raw.setCreatedBy(db.getCreatedBy());
                        raw.setCreatedAt(db.getCreatedAt());
                        raw.setVersion(db.getVersion());
                    }
                }).collect(Collectors.toList());
    }

    @Override
    public HouseFeature save(HouseDetailInfo houseDetailInfo, Long houseBaseId) {
        HouseFeature result;
        SourceType sourceType = houseDetailInfo.getSourceType();
        HouseDetailInfo.FeatureInfo featureInfo = houseDetailInfo.getFeatureInfo();
        HouseFeature houseFeature = HouseFeature.builder()
                .sourceType(sourceType)
                .houseBaseId(houseBaseId)
                .description(featureInfo.getDesc())
                .roomLayoutDesc(featureInfo.getRoomLayoutDesc())
                .decorationDesc(featureInfo.getDecorationDesc())
                .surroundingFacility(featureInfo.getSurroundingFacility())
                .suitablePeople(featureInfo.getSuitablePeople())
                .trafficInfo(featureInfo.getTrafficInfo())
                .taxesInfo(featureInfo.getTaxesInfo())
                .saleInfo(featureInfo.getSaleInfo())
                .sellingPoint(featureInfo.getSellingPoint())
                .unknownField(JsonUtil.writeToJson(featureInfo.getUnknownField(), objectMapper))
                .build();

        HouseFeature dbInfo = houseFeatureRepository.findAllBySourceTypeAndHouseBaseIdAndDeletedFalse(sourceType, houseBaseId).orElse(null);

        if (dbInfo == null) {
            result = houseFeatureRepository.save(houseFeature);
        } else if (!dbInfo.equals(houseFeature)) {
            houseFeature.setId(dbInfo.getId());
            houseFeature.setCreatedBy(dbInfo.getCreatedBy());
            houseFeature.setCreatedAt(dbInfo.getCreatedAt());
            houseFeature.setVersion(dbInfo.getVersion());
            result = houseFeatureRepository.save(houseFeature);
        } else {
            result = dbInfo;
        }
        return result;
    }
}
