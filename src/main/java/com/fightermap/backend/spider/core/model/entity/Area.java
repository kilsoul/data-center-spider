package com.fightermap.backend.spider.core.model.entity;

import com.fightermap.backend.spider.common.enums.AreaType;
import com.fightermap.backend.spider.common.enums.SourceType;
import com.fightermap.backend.spider.core.component.jpa.InstantConverter;
import com.fightermap.backend.spider.core.component.jpa.SourceTypeConverter;
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
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
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
@SQLDelete(sql = "UPDATE area SET deleted=1,deleted_at=now(),version=version+1 WHERE id=? AND version=? AND deleted=0")
@DynamicInsert
@DynamicUpdate
@EntityListeners({AuditingEntityListener.class})
@Table(name = "area")
@Entity
public class Area extends BaseEntity {

    @Convert(converter = SourceTypeConverter.class)
    @Column(name = "source_type")
    private SourceType sourceType;

    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    private AreaType type;

    @Basic
    @Column(name = "name")
    private String name;

    @Basic
    @Column(name = "name_cn")
    private String nameCn;

    @Basic
    @Column(name = "url")
    private String url;

    @Basic
    @Column(name = "parent_id")
    private Long parentId;

    @Column(name = "deleted")
    private boolean deleted;

    @Builder.Default
    @Convert(converter = InstantConverter.class)
    @Column(name = "deleted_at")
    private Instant deletedAt = Instant.EPOCH;
}
