package com.fightermap.backend.spider.core.repository;

import com.fightermap.backend.spider.common.enums.SourceType;
import com.fightermap.backend.spider.core.model.entity.HouseBrief;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @author zengqk
 */
public interface HouseBriefRepository extends JpaRepository<HouseBrief, Long> {

    List<HouseBrief> findAllBySourceTypeAndHouseIdInAndDeletedFalse(SourceType sourceType, List<String> houseIds);

    /**
     * 根据ID查询
     *
     * @param ids
     * @return
     */
    List<HouseBrief> findAllByIdIn(List<Long> ids);
}
