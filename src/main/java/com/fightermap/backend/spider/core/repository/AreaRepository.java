package com.fightermap.backend.spider.core.repository;

import com.fightermap.backend.spider.common.enums.AreaType;
import com.fightermap.backend.spider.common.enums.SourceType;
import com.fightermap.backend.spider.core.model.entity.Area;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @author zengqk
 */
public interface AreaRepository extends JpaRepository<Area, Long> {

    /**
     * 根据名称查询
     *
     * @param sourceType
     * @param areaType
     * @param names
     * @return
     */
    List<Area> findAllBySourceTypeAndTypeAndNameInAndDeletedFalse(SourceType sourceType, AreaType areaType, List<String> names);

    /**
     * 根据ID查询
     *
     * @param ids
     * @return
     */
    List<Area> findAllByIdIn(List<Long> ids);

    /**
     * 根据类型查询
     *
     * @param sourceType
     * @param areaType
     * @return
     */
    List<Area> findAllBySourceTypeAndTypeAndDeletedFalse(SourceType sourceType, AreaType areaType);
}
