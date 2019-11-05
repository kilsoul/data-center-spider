package com.fightermap.backend.spider.core.service.impl;

import com.fightermap.backend.spider.common.enums.SourceType;
import com.fightermap.backend.spider.core.model.bo.spider.HouseDetailInfo;
import com.fightermap.backend.spider.core.model.entity.HousePhoto;
import com.fightermap.backend.spider.core.repository.HousePhotoRepository;
import com.fightermap.backend.spider.core.service.HousePhotoService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.fightermap.backend.spider.common.util.StringUtil.generateKey;

/**
 * @author zengqk
 */
@Slf4j
@Service
public class HousePhotoServiceImpl implements HousePhotoService {

    private final HousePhotoRepository housePhotoRepository;

    public HousePhotoServiceImpl(HousePhotoRepository housePhotoRepository) {
        this.housePhotoRepository = housePhotoRepository;
    }

    @Override
    public List<HousePhoto> findBySourceTypeAndHouseBaseIdIn(SourceType sourceType, List<Long> houseBaseIds) {
        if (CollectionUtils.isEmpty(houseBaseIds)) {
            return Collections.emptyList();
        }
        return housePhotoRepository.findAllBySourceTypeAndHouseBaseIdInAndDeletedFalse(sourceType, houseBaseIds);
    }

    @Override
    public List<HousePhoto> saveAll(List<HousePhoto> housePhotoList) {
        if (CollectionUtils.isEmpty(housePhotoList)) {
            return Collections.emptyList();
        }
        return housePhotoRepository.saveAll(housePhotoList);
    }

    @Override
    public List<HousePhoto> merge(List<HousePhoto> rawList, List<HousePhoto> dbList) {
        Map<String, HousePhoto> dbInfoMap = dbList.stream()
                .collect(Collectors.toMap(p -> generateKey(String.valueOf(p.getHouseBaseId()), p.getName()), Function.identity()));
        return rawList.stream().filter(raw -> {
            Long houseBaseId = raw.getHouseBaseId();
            HousePhoto db = dbInfoMap.get(generateKey(String.valueOf(houseBaseId), raw.getName()));
            //取字段变更的
            return !raw.equals(db);
        }).collect(Collectors.toList())
                .stream()
                .peek(raw -> {
                    Long houseBaseId = raw.getHouseBaseId();
                    HousePhoto db = dbInfoMap.get(String.valueOf(houseBaseId).concat("-").concat(raw.getName()));
                    if (db != null) {
                        raw.setId(db.getId());
                        raw.setCreatedBy(db.getCreatedBy());
                        raw.setCreatedAt(db.getCreatedAt());
                        raw.setVersion(db.getVersion());
                    }
                }).collect(Collectors.toList());
    }

    @Override
    public List<HousePhoto> save(HouseDetailInfo houseDetailInfo, Long houseBaseId) {
        SourceType sourceType = houseDetailInfo.getSourceType();
        List<HouseDetailInfo.HousePhoto> housePhotoInfoList = houseDetailInfo.getHousePhotos();
        List<HousePhoto> housePhotoList = Optional.ofNullable(housePhotoInfoList)
                .orElse(Collections.emptyList())
                .stream()
                .map(data -> HousePhoto.builder()
                        .sourceType(sourceType)
                        .houseBaseId(houseBaseId)
                        .name(data.getName())
                        .uri(data.getUri())
                        .build())
                .collect(Collectors.toList());


        List<HousePhoto> dbInfoList = housePhotoRepository.findAllBySourceTypeAndHouseBaseIdAndDeletedFalse(sourceType, houseBaseId);

        List<HousePhoto> mergedList = merge(housePhotoList, dbInfoList);

        return saveAll(mergedList);
    }
}
