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
import java.time.LocalDate;

/**
 * @author zengqk
 */
@Builder
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@NoArgsConstructor
@Data
@SQLDelete(sql = "UPDATE house_transaction SET deleted=1,deleted_at=now(),version=version+1 WHERE id=? AND version=? AND deleted=0")
@DynamicInsert
@DynamicUpdate
@EntityListeners({AuditingEntityListener.class})
@Table(name = "house_transaction")
@Entity
public class HouseTransaction extends AbstractAuditEntity {

    @Convert(converter = SourceTypeConverter.class)
    @Column(name = "source_type")
    private SourceType sourceType;

    /**
     * 房间基本信息ID
     */
    @Column(name = "house_base_id")
    private Long houseBaseId;

    /**
     * 挂牌日期
     */
    @Column(name = "listing_date")
    private LocalDate listingDate;

    /**
     * 上次交易日期
     */
    @Column(name = "last_transaction_date")
    private LocalDate lastTransactionDate;

    /**
     * 交易权属类型，如 商品房
     */
    @Column(name = "transaction_hold_type")
    private String transactionHoldType;

    /**
     * 房屋用途类型，如 普通住宅
     */
    @Column(name = "house_usage_type")
    private String houseUsageType;

    /**
     * 房屋年限
     */
    @Column(name = "house_limit")
    private String houseLimit;

    /**
     * 产权所属，如 非共有
     */
    @Column(name = "house_hold_type")
    private String houseHoldType;

    /**
     * 抵押信息
     */
    @Column(name = "mortgage_info")
    private String mortgageInfo;

    /**
     * 是否有抵押
     */
    @Column(name = "has_mortgage")
    private boolean hasMortgage;

    /**
     * 房本备件
     */
    @Column(name = "house_credential")
    private String houseCredential;

    /**
     * 未知字段json
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
