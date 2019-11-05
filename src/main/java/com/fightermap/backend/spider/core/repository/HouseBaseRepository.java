package com.fightermap.backend.spider.core.repository;

import com.fightermap.backend.spider.common.enums.SourceType;
import com.fightermap.backend.spider.core.model.entity.HouseBase;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/**
 * @author zengqk
 */
public interface HouseBaseRepository extends JpaRepository<HouseBase, Long> {

    List<HouseBase> findAllBySourceTypeAndHouseIdInAndDeletedFalse(SourceType sourceType, List<String> houseIds);

    Optional<HouseBase> findFirstBySourceTypeAndHouseIdAndDeletedFalse(SourceType sourceType, String houseIds);

    /**
     * 根据ID查询
     *
     * @param ids
     * @return
     */
    List<HouseBase> findAllByIdIn(List<Long> ids);
}
