package com.fightermap.backend.spider.core.service.impl;

import com.fightermap.backend.spider.common.enums.SourceType;
import com.fightermap.backend.spider.core.model.bo.spider.HouseDetailInfo;
import com.fightermap.backend.spider.core.model.entity.HouseRoomLayout;
import com.fightermap.backend.spider.core.repository.HouseRoomLayoutRepository;
import com.fightermap.backend.spider.core.service.HouseRoomLayoutService;
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
public class HouseRoomLayoutServiceImpl implements HouseRoomLayoutService {

    private final HouseRoomLayoutRepository houseRoomLayoutRepository;

    public HouseRoomLayoutServiceImpl(HouseRoomLayoutRepository houseRoomLayoutRepository) {
        this.houseRoomLayoutRepository = houseRoomLayoutRepository;
    }

    @Override
    public List<HouseRoomLayout> findBySourceTypeAndHouseBaseIdIn(SourceType sourceType, List<String> houseBaseIds) {
        if (CollectionUtils.isEmpty(houseBaseIds)) {
            return Collections.emptyList();
        }
        return houseRoomLayoutRepository.findAllBySourceTypeAndHouseBaseIdInAndDeletedFalse(sourceType, houseBaseIds);
    }

    @Override
    public List<HouseRoomLayout> saveAll(List<HouseRoomLayout> houseRoomLayoutList) {
        if (CollectionUtils.isEmpty(houseRoomLayoutList)) {
            return Collections.emptyList();
        }
        return houseRoomLayoutRepository.saveAll(houseRoomLayoutList);
    }

    @Override
    public List<HouseRoomLayout> merge(List<HouseRoomLayout> rawList, List<HouseRoomLayout> dbList) {
        Map<String, HouseRoomLayout> dbInfoMap = dbList.stream().collect(Collectors.toMap(l -> generateKey(String.valueOf(l.getHouseBaseId()), l.getType()), Function.identity()));
        return rawList.stream().filter(raw -> {
            Long houseBaseId = raw.getHouseBaseId();
            HouseRoomLayout db = dbInfoMap.get(String.valueOf(houseBaseId).concat("-").concat(raw.getType()));
            //取字段变更的
            return !raw.equals(db);
        }).collect(Collectors.toList())
                .stream()
                .peek(raw -> {
                    Long houseBaseId = raw.getHouseBaseId();
                    HouseRoomLayout db = dbInfoMap.get(generateKey(String.valueOf(houseBaseId), raw.getType()));
                    if (db != null) {
                        raw.setId(db.getId());
                        raw.setCreatedBy(db.getCreatedBy());
                        raw.setCreatedAt(db.getCreatedAt());
                        raw.setVersion(db.getVersion());
                    }
                }).collect(Collectors.toList());
    }

    @Override
    public List<HouseRoomLayout> save(HouseDetailInfo houseDetailInfo, Long houseBaseId) {
        SourceType sourceType = houseDetailInfo.getSourceType();
        List<HouseDetailInfo.RoomLayout> roomLayoutInfoList = houseDetailInfo.getRoomLayouts();
        List<HouseRoomLayout> houseRoomLayoutList = Optional.ofNullable(roomLayoutInfoList)
                .orElse(Collections.emptyList())
                .stream()
                .map(data -> HouseRoomLayout.builder()
                        .sourceType(sourceType)
                        .houseBaseId(houseBaseId)
                        .type(data.getType())
                        .area(data.getArea())
                        .orientation(data.getOrientation())
                        .window(data.getWindow())
                        .build())
                .collect(Collectors.toList());


        List<HouseRoomLayout> dbInfoList = houseRoomLayoutRepository.findAllBySourceTypeAndHouseBaseIdAndDeletedFalse(sourceType, houseBaseId);

        List<HouseRoomLayout> mergedList = merge(houseRoomLayoutList, dbInfoList);

        return saveAll(mergedList);
    }
}
