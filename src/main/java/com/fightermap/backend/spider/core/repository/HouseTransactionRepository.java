package com.fightermap.backend.spider.core.repository;

import com.fightermap.backend.spider.common.enums.SourceType;
import com.fightermap.backend.spider.core.model.entity.HouseTransaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/**
 * @author zengqk
 */
public interface HouseTransactionRepository extends JpaRepository<HouseTransaction, Long> {

    List<HouseTransaction> findAllBySourceTypeAndHouseBaseIdInAndDeletedFalse(SourceType sourceType, List<Long> houseBaseIds);

    Optional<HouseTransaction> findAllBySourceTypeAndHouseBaseIdAndDeletedFalse(SourceType sourceType, Long houseBaseId);

    /**
     * 根据ID查询
     *
     * @param ids
     * @return
     */
    List<HouseTransaction> findAllByIdIn(List<Long> ids);
}
