package com.fightermap.backend.spider.core.repository;

import com.fightermap.backend.spider.common.enums.SourceType;
import com.fightermap.backend.spider.core.model.entity.HousePhoto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @author zengqk
 */
public interface HousePhotoRepository extends JpaRepository<HousePhoto, Long> {

    List<HousePhoto> findAllBySourceTypeAndHouseBaseIdInAndDeletedFalse(SourceType sourceType, List<Long> houseBaseIds);

    List<HousePhoto> findAllBySourceTypeAndHouseBaseIdAndDeletedFalse(SourceType sourceType, Long houseBaseId);

    /**
     * 根据ID查询
     *
     * @param ids
     * @return
     */
    List<HousePhoto> findAllByIdIn(List<Long> ids);
}
