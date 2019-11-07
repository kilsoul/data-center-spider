package com.fightermap.backend.spider.core.model.entity;

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
@SQLDelete(sql = "UPDATE spider_log SET deleted=1,deleted_at=now(),version=version+1 WHERE id=? AND version=? AND deleted=0")
@DynamicInsert
@DynamicUpdate
@EntityListeners({AuditingEntityListener.class})
@Table(name = "spider_log")
@Entity
public class SpiderLog extends AbstractAuditEntity {

    @Basic
    @Column(name = "occur_time")
    private Instant occurTime = Instant.now();

    @Basic
    @Column(name = "url")
    private String url;

    @Builder.Default
    @Column(name = "success")
    private boolean success = true;

    @Column(name = "deleted")
    private boolean deleted;

    @Builder.Default
    @Convert(converter = InstantConverter.class)
    @Column(name = "deleted_at")
    private Instant deletedAt = Instant.EPOCH;
}
