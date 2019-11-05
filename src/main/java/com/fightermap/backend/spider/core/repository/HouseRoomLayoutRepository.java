package com.fightermap.backend.spider.core.repository;

import com.fightermap.backend.spider.common.enums.SourceType;
import com.fightermap.backend.spider.core.model.entity.HouseRoomLayout;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @author zengqk
 */
public interface HouseRoomLayoutRepository extends JpaRepository<HouseRoomLayout, Long> {

    List<HouseRoomLayout> findAllBySourceTypeAndHouseBaseIdInAndDeletedFalse(SourceType sourceType, List<String> houseBaseIds);

    List<HouseRoomLayout> findAllBySourceTypeAndHouseBaseIdAndDeletedFalse(SourceType sourceType, Long houseBaseId);

    /**
     * 根据ID查询
     *
     * @param ids
     * @return
     */
    List<HouseRoomLayout> findAllByIdIn(List<Long> ids);
}
