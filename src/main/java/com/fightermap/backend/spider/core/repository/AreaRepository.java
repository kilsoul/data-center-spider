package com.fightermap.backend.spider.core.repository;

import com.fightermap.backend.spider.common.enums.AreaType;
import com.fightermap.backend.spider.common.enums.SourceType;
import com.fightermap.backend.spider.core.model.entity.Area;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/**
 * @author zengqk
 */
public interface AreaRepository extends JpaRepository<Area, Long> {

    /**
     * 根据名称查询
     *
     * @param sourceType
     * @param areaType
     * @param paths
     * @return
     */
    List<Area> findAllBySourceTypeAndTypeAndPathInAndDeletedFalse(SourceType sourceType, AreaType areaType, List<String> paths);

    List<Area> findAllBySourceTypeAndPathInAndDeletedFalse(SourceType sourceType, List<String> paths);

    Optional<Area> findFirstBySourceTypeAndPathAndDeletedFalse(SourceType sourceType, String path);

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
