package com.fightermap.backend.spider.common.constant;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

/**
 * @author zengqk
 */
public class Mapper {
    public static final Map<String, String> LIANJIA_BASE_INFO_MAPPER = new HashMap<>();
    public static final Map<String, String> LIANJIA_TRANSACTION_INFO_MAPPER = new HashMap<>();
    public static final Map<String, String> LIANJIA_FEATURE_INFO_MAPPER = new HashMap<>();
    private static final Map<String, Boolean> LIANJIA_BOOLEAN_MAPPER = new HashMap<>();

    public static Boolean exists(String value) {
        return LIANJIA_BOOLEAN_MAPPER.keySet().stream().noneMatch(value::contains);
    }

    public static <T> T existsTransfer(String value, Supplier<T> supplier, T defaultValue) {
        if (exists(value)) {
            return supplier.get();
        } else {
            return defaultValue;
        }
    }

    static {
        LIANJIA_BASE_INFO_MAPPER.put("房屋户型", "roomType");
        LIANJIA_BASE_INFO_MAPPER.put("所在楼层", "floorInfo");
        LIANJIA_BASE_INFO_MAPPER.put("建筑面积", "constructionArea");
        LIANJIA_BASE_INFO_MAPPER.put("户型结构", "houseStructure");
        LIANJIA_BASE_INFO_MAPPER.put("套内面积", "actualArea");
        LIANJIA_BASE_INFO_MAPPER.put("建筑类型", "buildingType");
        LIANJIA_BASE_INFO_MAPPER.put("房屋朝向", "orientation");
        LIANJIA_BASE_INFO_MAPPER.put("建筑结构", "buildingStructureType");
        LIANJIA_BASE_INFO_MAPPER.put("装修情况", "decorationType");
        LIANJIA_BASE_INFO_MAPPER.put("梯户比例", "liftHouseScale");
        LIANJIA_BASE_INFO_MAPPER.put("配备电梯", "hasLift");
        LIANJIA_BASE_INFO_MAPPER.put("产权年限", "houseHoldYears");

        LIANJIA_TRANSACTION_INFO_MAPPER.put("挂牌时间", "listingDate");
        LIANJIA_TRANSACTION_INFO_MAPPER.put("交易权属", "transactionHoldType");
        LIANJIA_TRANSACTION_INFO_MAPPER.put("上次交易", "lastTransactionDate");
        LIANJIA_TRANSACTION_INFO_MAPPER.put("房屋用途", "houseUsageType");
        LIANJIA_TRANSACTION_INFO_MAPPER.put("房屋年限", "houseLimit");
        LIANJIA_TRANSACTION_INFO_MAPPER.put("产权所属", "houseHoldType");
        LIANJIA_TRANSACTION_INFO_MAPPER.put("抵押信息", "mortgageInfo");
        LIANJIA_TRANSACTION_INFO_MAPPER.put("房本备件", "houseCredential");

        LIANJIA_FEATURE_INFO_MAPPER.put("小区介绍", "desc");
        LIANJIA_FEATURE_INFO_MAPPER.put("户型介绍", "roomLayoutDesc");
        LIANJIA_FEATURE_INFO_MAPPER.put("装修描述", "decorationDesc");
        LIANJIA_FEATURE_INFO_MAPPER.put("周边配套", "surroundingFacility");
        LIANJIA_FEATURE_INFO_MAPPER.put("适宜人群", "suitablePeople");
        LIANJIA_FEATURE_INFO_MAPPER.put("交通出行", "trafficInfo");
        LIANJIA_FEATURE_INFO_MAPPER.put("税费解析", "taxesInfo");
        LIANJIA_FEATURE_INFO_MAPPER.put("售房详情", "saleInfo");
        LIANJIA_FEATURE_INFO_MAPPER.put("核心卖点", "sellingPoint");

        LIANJIA_BOOLEAN_MAPPER.put("无", Boolean.FALSE);
        LIANJIA_BOOLEAN_MAPPER.put("未知", Boolean.FALSE);
        LIANJIA_BOOLEAN_MAPPER.put("没有", Boolean.FALSE);
        LIANJIA_BOOLEAN_MAPPER.put("暂无数据", Boolean.FALSE);
    }
}
