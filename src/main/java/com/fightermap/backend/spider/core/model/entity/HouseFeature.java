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
@SQLDelete(sql = "UPDATE house_feature SET deleted=1,deleted_at=now(),version=version+1 WHERE id=? AND version=? AND deleted=0")
@DynamicInsert
@DynamicUpdate
@EntityListeners({AuditingEntityListener.class})
@Table(name = "house_feature")
@Entity
public class HouseFeature extends AbstractAuditEntity {

    @Convert(converter = SourceTypeConverter.class)
    @Column(name = "source_type")
    private SourceType sourceType;

    /**
     * 房间基本信息ID
     */
    @Column(name = "house_base_id")
    private Long houseBaseId;

    /**
     * 小区介绍
     */
    @Column(name = "description")
    private String description;

    /**
     * 户型介绍
     */
    @Column(name = "room_layout_desc")
    private String roomLayoutDesc;

    /**
     * 装修描述
     */
    @Column(name = "decoration_desc")
    private String decorationDesc;

    /**
     * 周边配套
     */
    @Column(name = "surroundingFacility")
    private String surroundingFacility;

    /**
     * 适宜人群
     */
    @Column(name = "suitable_people")
    private String suitablePeople;

    /**
     * 交通情况
     */
    @Column(name = "traffic_info")
    private String trafficInfo;

    /**
     * 税费信息
     */
    @Column(name = "taxes_info")
    private String taxesInfo;

    /**
     * 销售信息
     */
    @Column(name = "sale_info")
    private String saleInfo;

    /**
     * 卖点
     */
    @Column(name = "selling_point")
    private String sellingPoint;

    /**
     * 未知字段
     */
    @Column(name = "unknown_field")
    private String unknownField;

    @Column(name = "deleted")
    private boolean deleted;

    @Builder.Default
    @Convert(converter = InstantConverter.class)
    @Column(name = "deleted_at")
    private Instant deletedAt = Instant.EPOCH;
}
