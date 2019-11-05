package com.fightermap.backend.spider.core.model.entity;

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
@SQLDelete(sql = "UPDATE house_brief SET deleted=1,deleted_at=now(),version=version+1 WHERE id=? AND version=? AND deleted=0")
@DynamicInsert
@DynamicUpdate
@EntityListeners({AuditingEntityListener.class})
@Table(name = "house_brief")
@Entity
public class HouseBrief extends AbstractAuditEntity {

    @Convert(converter = SourceTypeConverter.class)
    @Column(name = "source_type")
    private SourceType sourceType;

    @Column(name = "house_id")
    private String houseId;

    @Column(name = "title")
    private String title;

    @Column(name = "community_name")
    private String communityName;

    @Column(name = "area_id")
    private Long areaId;

    @Column(name = "area_name_cn")
    private String areaNameCn;

    @Column(name = "short_info")
    private String shortInfo;

    @Column(name = "follow_info")
    private String followInfo;

    @Column(name = "total_price")
    private float totalPrice;

    @Column(name = "unit_price")
    private float unitPrice;

    @Column(name = "tags")
    private String tags;

    @Column(name = "detail_url")
    private String detailUrl;

    @Column(name = "deleted")
    private boolean deleted;

    @Builder.Default
    @Convert(converter = InstantConverter.class)
    @Column(name = "deleted_at")
    private Instant deletedAt = Instant.EPOCH;
}
