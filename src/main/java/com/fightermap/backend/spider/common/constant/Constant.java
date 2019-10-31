package com.fightermap.backend.spider.common.constant;

import java.time.format.DateTimeFormatter;

/**
 * @author zengqk
 */
public class Constant {

    public static final String SQUARE_METER = "㎡";

    public static final DateTimeFormatter DEFAULT_DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    /**
     * 空字符串
     */
    public static final String EMPTY_STRING = "";

    public static final String SPLIT_PATH = "/";

    public static final String ITEM_KEY_DISTRICT_LIST= "districtList";
    public static final String ITEM_KEY_POSITION_LIST= "positionList";
    public static final String ITEM_KEY_HOUSE_SHORT_INFO_LIST= "houseShortInfoList";
    public static final String ITEM_KEY_HOUSE_DETAIL_INFO= "houseDetailInfo";

}
