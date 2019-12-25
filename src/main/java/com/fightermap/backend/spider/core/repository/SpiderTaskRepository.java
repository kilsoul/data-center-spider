package com.fightermap.backend.spider.core.repository;

import com.fightermap.backend.spider.core.model.entity.SpiderTask;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * @author zengqk
 */
public interface SpiderTaskRepository extends JpaRepository<SpiderTask, Long> {

    Optional<SpiderTask> findFirstBySpiderUuidAndDeleted(String uuid,boolean deleted);
}
