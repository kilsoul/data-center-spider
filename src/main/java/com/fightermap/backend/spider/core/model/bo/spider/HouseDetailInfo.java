package com.fightermap.backend.spider.core.model.bo.spider;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 房屋明细信息
 *
 * @author zengqk
 */
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class HouseDetailInfo extends BaseData {

    /**
     * id
     */
    private String id;

    /**
     * 页面地址
     */
    private String url;

    /**
     * 区域信息
     */
    private String areaInfo;

    /**
     * 基本信息
     */
    private BaseInfo baseInfo;

    /**
     * 交易信息
     */
    private TransactionInfo transactionInfo;

    /**
     * 特色
     */
    private FeatureInfo featureInfo;

    /**
     * 户型
     */
    private List<RoomLayout> roomLayouts;

    /**
     * 房屋图片
     */
    private List<HousePhoto> housePhotos;

    /**
     * 基本信息
     */
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @Data
    public static class BaseInfo {

        /**
         * 房屋户型
         */
        private String roomType;

        /**
         * 楼层信息
         */
        private String floorInfo;

        /**
         * 建筑面积
         */
        private Float constructionArea;

        /**
         * 户型结构
         */
        private String houseStructure;

        /**
         * 套内实际面积
         */
        private Float actualArea;

        /**
         * 建筑类型
         */
        private String buildingType;

        /**
         * 朝向
         */
        private String orientation;

        /**
         * 建筑结构类型
         */
        private String buildingStructureType;

        /**
         * 装修类型
         */
        private String decorationType;

        /**
         * 梯户比例
         */
        private String liftHouseScale;

        /**
         * 是否有电梯
         */
        private Boolean hasLift;

        /**
         * 产权年限
         */
        private Integer houseHoldYears;

        /**
         * 未知字段
         */
        private Map<String, String> unknownField = new HashMap<>();
    }

    /**
     * 交易信息
     */
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @Data
    public static class TransactionInfo {

        /**
         * 挂牌日期
         */
        private LocalDate listingDate;

        /**
         * 上次交易日期
         */
        private LocalDate lastTransactionDate;

        /**
         * 交易权属类型，如 商品房
         */
        private String transactionHoldType;

        /**
         * 房屋用途类型，如 普通住宅
         */
        private String houseUsageType;

        /**
         * 房屋年限
         */
        private String houseLimit;

        /**
         * 产权所属，如 非共有
         */
        private String houseHoldType;

        /**
         * 抵押信息
         */
        private String mortgageInfo;

        /**
         * 是否有抵押
         */
        private Boolean hasMortgage;

        /**
         * 房本备件
         */
        private String houseCredential;

        /**
         * 未知字段
         */
        private Map<String, String> unknownField = new HashMap<>();
    }

    /**
     * 房源特色
     */
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @Data
    public static class FeatureInfo {

        /**
         * 小区介绍
         */
        private String desc;

        /**
         * 户型介绍
         */
        private String roomLayoutDesc;

        /**
         * 装修描述
         */
        private String decorationDesc;

        /**
         * 周边配套
         */
        private String surroundingFacility;

        /**
         * 适宜人群
         */
        private String suitablePeople;

        /**
         * 交通情况
         */
        private String trafficInfo;

        /**
         * 税费信息
         */
        private String taxesInfo;

        /**
         * 销售信息
         */
        private String saleInfo;

        /**
         * 卖点
         */
        private String sellingPoint;

        /**
         * 未知字段
         */
        private Map<String, String> unknownField = new HashMap<>();
    }

    /**
     * 房间布局
     */
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @Data
    public static class RoomLayout {

        /**
         * 房间类型
         */
        private String type;

        /**
         * 面积
         */
        private String area;

        /**
         * 朝向
         */
        private String orientation;

        /**
         * 窗户类型
         */
        private String window;
    }

    /**
     * 房屋照片
     */
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @Data
    public static class HousePhoto {

        /**
         * 说明
         */
        private String name;

        /**
         * 资源地址
         */
        private String uri;
    }
}
