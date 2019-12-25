package com.fightermap.backend.spider.core.model.entity;

import com.fightermap.backend.spider.common.enums.SpiderTaskStatus;
import com.fightermap.backend.spider.core.component.jpa.InstantConverter;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.SQLDelete;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.Table;
import java.time.Instant;

/**
 * @author zengqk
 */
@Builder
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@NoArgsConstructor
@Data
@SQLDelete(sql = "UPDATE spider_task SET deleted=1,deleted_at=now(),version=version+1 WHERE id=? AND version=? AND deleted=0")
@DynamicInsert
@DynamicUpdate
@EntityListeners({AuditingEntityListener.class})
@Table(name = "spider_task")
@Entity
public class SpiderTask extends AbstractAuditEntity {

    /**
     * 任务UUID
     */
    @Basic
    @Column(name = "spider_uuid")
    private String spiderUuid;

    /**
     * 开始时间
     */
    @Basic
    @Column(name = "start_time")
    private Instant startTime;

    /**
     * 结束时间
     */
    @Basic
    @Column(name = "end_time")
    private Instant endTime;

    /**
     * 持续时间
     */
    @Basic
    @Column(name = "duration")
    private Long duration;

    /**
     * 种子url地址
     */
    @Basic
    @Column(name = "seed_url")
    private String seedUrl;

    /**
     * 成功数量
     */
    @Basic
    @Column(name = "success_count")
    private Integer successCount;

    /**
     * 失败数量
     */
    @Basic
    @Column(name = "fail_count")
    private Integer failCount;

    /**
     * 状态
     */
    @Basic
    @Column(name = "status")
    private SpiderTaskStatus status;

    @Column(name = "deleted")
    private boolean deleted;

    @Builder.Default
    @Convert(converter = InstantConverter.class)
    @Column(name = "deleted_at")
    private Instant deletedAt = Instant.EPOCH;
}
