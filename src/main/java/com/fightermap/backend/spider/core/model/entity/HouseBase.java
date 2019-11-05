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
@SQLDelete(sql = "UPDATE house_base SET deleted=1,deleted_at=now(),version=version+1 WHERE id=? AND version=? AND deleted=0")
@DynamicInsert
@DynamicUpdate
@EntityListeners({AuditingEntityListener.class})
@Table(name = "house_base")
@Entity
public class HouseBase extends AbstractAuditEntity {

    @Convert(converter = SourceTypeConverter.class)
    @Column(name = "source_type")
    private SourceType sourceType;

    /**
     * 房屋网站ID
     */
    @Column(name = "house_id")
    private String houseId;

    /**
     * 房屋户型
     */
    @Column(name = "room_type")
    private String roomType;

    /**
     * 楼层信息
     */
    @Column(name = "floor_info")
    private String floorInfo;

    /**
     * 建筑面积
     */
    @Column(name = "construction_area")
    private Float constructionArea;

    /**
     * 套内实际面积
     */
    @Column(name = "actual_area")
    private Float actualArea;

    /**
     * 户型结构
     */
    @Column(name = "house_structure")
    private String houseStructure;

    /**
     * 建筑类型
     */
    @Column(name = "building_type")
    private String buildingType;

    /**
     * 朝向
     */
    @Column(name = "orientation")
    private String orientation;

    /**
     * 建筑结构类型
     */
    @Column(name = "building_structure_type")
    private String buildingStructureType;

    /**
     * 装修类型
     */
    @Column(name = "decoration_type")
    private String decorationType;

    /**
     * 梯户比例
     */
    @Column(name = "lift_house_scale")
    private String liftHouseScale;

    /**
     * 是否有电梯
     */
    @Column(name = "has_lift")
    private Boolean hasLift;

    /**
     * 产权年限
     */
    @Column(name = "house_hold_years")
    private Integer houseHoldYears;

    /**
     * 区域id
     */
    @Column(name = "area_id")
    private Long areaId;

    /**
     * 区域信息
     */
    @Column(name = "area_info")
    private String areaInfo;

    /**
     * 页面url
     */
    @Column(name = "url")
    private String url;

    /**
     * 未转换信息，json结构
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
