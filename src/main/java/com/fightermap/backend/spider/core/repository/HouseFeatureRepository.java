package com.fightermap.backend.spider.core.repository;

import com.fightermap.backend.spider.common.enums.SourceType;
import com.fightermap.backend.spider.core.model.entity.HouseFeature;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/**
 * @author zengqk
 */
public interface HouseFeatureRepository extends JpaRepository<HouseFeature, Long> {

    List<HouseFeature> findAllBySourceTypeAndHouseBaseIdInAndDeletedFalse(SourceType sourceType, List<Long> houseBaseIds);

    Optional<HouseFeature> findAllBySourceTypeAndHouseBaseIdAndDeletedFalse(SourceType sourceType, Long houseBaseId);

    /**
     * 根据ID查询
     *
     * @param ids
     * @return
     */
    List<HouseFeature> findAllByIdIn(List<Long> ids);
}
