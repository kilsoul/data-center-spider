package com.fightermap.backend.spider.core.model.bo.spider;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

/**
 * 房屋简要信息
 *
 * @author zengqk
 */
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class HouseBriefInfo extends BaseData{

    /**
     * id
     */
    private String id;

    /**
     * 标题
     */
    private String title;

    /**
     * 小区名称
     */
    private String communityName;

    /**
     * 位置
     */
    private String position;

    /**
     * 简要信息
     */
    private String shortInfo;

    /**
     * 关注信息
     */
    private String followInfo;

    /**
     * 总价
     */
    private Float totalPrice;

    /**
     * 单价
     */
    private Float unitPrice;

    /**
     * 标签
     */
    private List<String> tags;

    /**
     * 明细信息地址
     */
    private String detailUrl;
}
