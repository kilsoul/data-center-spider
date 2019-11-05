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
@SQLDelete(sql = "UPDATE house_room_layout SET deleted=1,deleted_at=now(),version=version+1 WHERE id=? AND version=? AND deleted=0")
@DynamicInsert
@DynamicUpdate
@EntityListeners({AuditingEntityListener.class})
@Table(name = "house_room_layout")
@Entity
public class HouseRoomLayout extends AbstractAuditEntity {

    @Convert(converter = SourceTypeConverter.class)
    @Column(name = "source_type")
    private SourceType sourceType;

    /**
     * 房间基本信息ID
     */
    @Column(name = "house_base_id")
    private Long houseBaseId;

    /**
     * 房间类型
     */
    @Column(name = "type")
    private String type;

    /**
     * 面积
     */
    @Column(name = "area")
    private String area;

    /**
     * 朝向
     */
    @Column(name = "orientation")
    private String orientation;

    /**
     * 窗户类型
     */
    @Column(name = "window")
    private String window;

    @Column(name = "deleted")
    private boolean deleted;

    @Builder.Default
    @Convert(converter = InstantConverter.class)
    @Column(name = "deleted_at")
    private Instant deletedAt = Instant.EPOCH;
}
