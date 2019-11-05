package com.fightermap.backend.spider.core.component.magic;

import com.fightermap.backend.spider.common.enums.AreaType;
import com.fightermap.backend.spider.common.enums.SourceType;
import com.fightermap.backend.spider.core.model.bo.spider.District;
import com.fightermap.backend.spider.core.model.bo.spider.HouseBriefInfo;
import com.fightermap.backend.spider.core.model.bo.spider.HouseDetailInfo;
import com.fightermap.backend.spider.core.model.bo.spider.Position;
import com.fightermap.backend.spider.core.model.entity.Area;
import com.fightermap.backend.spider.core.model.entity.HouseBase;
import com.fightermap.backend.spider.core.model.entity.HouseBrief;
import com.fightermap.backend.spider.core.service.AreaService;
import com.fightermap.backend.spider.core.service.HouseBriefService;
import com.fightermap.backend.spider.core.service.HouseDetailService;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;
import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.pipeline.Pipeline;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.fightermap.backend.spider.common.constant.Constant.ITEM_KEY_DISTRICT_LIST;
import static com.fightermap.backend.spider.common.constant.Constant.ITEM_KEY_HOUSE_DETAIL_INFO;
import static com.fightermap.backend.spider.common.constant.Constant.ITEM_KEY_HOUSE_SHORT_INFO_LIST;
import static com.fightermap.backend.spider.common.constant.Constant.ITEM_KEY_POSITION_LIST;
import static com.fightermap.backend.spider.common.util.PageUtil.getAreaFromUrl;

/**
 * @author zengqk
 */
@Service
public class DatabasePipeline implements Pipeline {

    private final AreaService areaService;

    private final HouseBriefService houseBriefService;

    private final HouseDetailService houseDetailService;

    public DatabasePipeline(AreaService areaService,
                            HouseBriefService houseBriefService,
                            HouseDetailService houseDetailService) {
        this.areaService = areaService;
        this.houseBriefService = houseBriefService;
        this.houseDetailService = houseDetailService;
    }

    @Override
    public void process(ResultItems resultItems, Task task) {
        System.out.println("Got page: " + resultItems.getRequest().getUrl());

        Map<String, Object> resultItemsAll = resultItems.getAll();

        //TODO 数据落库
//        saveCityResult(resultItemsAll);
        saveDistrictResult(resultItemsAll);
        savePositionResult(resultItemsAll);
        saveHouseBriefInfoResult(resultItemsAll);
        saveHouseDetailInfo(resultItemsAll);
    }

    /**
     * 存储区级信息
     *
     * @param resultItemsAll
     */
    private void saveDistrictResult(Map<String, Object> resultItemsAll) {
        List<District> districtList = (List<District>) resultItemsAll.get(ITEM_KEY_DISTRICT_LIST);
        if (CollectionUtils.isEmpty(districtList)) {
            return;
        }

        AreaType areaType = AreaType.DISTRICT;
        District district = districtList.get(0);
        SourceType sourceType = district.getSourceType();
        List<String> paths = districtList.stream().map(d -> getAreaFromUrl(d.getUrl(), 1)).collect(Collectors.toList());
        List<Area> parents = areaService.findAreaByType(sourceType, areaType.getParent());
        Long parentId = parents.stream().filter(p -> district.getCityName().equalsIgnoreCase(p.getName())).findFirst().map(Area::getId).orElse(0L);
        List<Area> dbInfoList = areaService.findAllBySourceTypeAndPathIn(sourceType, areaType, paths);

        areaService.saveAll(areaService.merge(districtList.stream().map(d -> Area.builder()
                .sourceType(sourceType)
                .type(areaType)
                .name(d.getName())
                .nameCn(d.getChineseName())
                .path(d.getPath())
                .url(d.getUrl())
                .parentId(parentId)
                .build())
                .collect(Collectors.toList()), dbInfoList));
    }

    /**
     * 存储镇级信息
     *
     * @param resultItemsAll
     */
    private void savePositionResult(Map<String, Object> resultItemsAll) {
        List<Position> positionList = (List<Position>) resultItemsAll.get(ITEM_KEY_POSITION_LIST);
        if (CollectionUtils.isEmpty(positionList)) {
            return;
        }

        AreaType areaType = AreaType.POSITION;
        Position position = positionList.get(0);
        SourceType sourceType = position.getSourceType();
        List<String> paths = positionList.stream().map(p -> getAreaFromUrl(p.getUrl(), 2)).collect(Collectors.toList());
        List<Area> parents = areaService.findAreaByType(sourceType, areaType.getParent());
        Long parentId = parents.stream().filter(p -> position.getDistrictName().equalsIgnoreCase(p.getName())).findFirst().map(Area::getId).orElse(0L);
        List<Area> dbInfoList = areaService.findAllBySourceTypeAndPathIn(sourceType, areaType, paths);

        areaService.saveAll(areaService.merge(positionList.stream().map(d -> Area.builder()
                .sourceType(sourceType)
                .type(areaType)
                .name(d.getName())
                .nameCn(d.getChineseName())
                .path(d.getPath())
                .url(d.getUrl())
                .parentId(parentId)
                .build())
                .collect(Collectors.toList()), dbInfoList));
    }

    /**
     * 存储简要信息
     *
     * @param resultItemsAll
     */
    private void saveHouseBriefInfoResult(Map<String, Object> resultItemsAll) {
        List<HouseBriefInfo> houseBriefInfoList = (List<HouseBriefInfo>) resultItemsAll.get(ITEM_KEY_HOUSE_SHORT_INFO_LIST);
        if (CollectionUtils.isEmpty(houseBriefInfoList)) {
            return;
        }

        HouseBriefInfo houseBriefInfo = houseBriefInfoList.get(0);
        SourceType sourceType = houseBriefInfo.getSourceType();
        List<HouseBrief> dbInfoList = houseBriefService.findBySourceTypeAndHouseIdIn(sourceType, houseBriefInfoList.stream().map(HouseBriefInfo::getId).collect(Collectors.toList()));
        List<String> paths = houseBriefInfoList.stream().map(HouseBriefInfo::getPath).collect(Collectors.toList());
        Map<String, Area> areaPathMap = areaService.findAllByPathIn(sourceType, paths).stream().collect(Collectors.toMap(Area::getPath, Function.identity()));

        houseBriefService.saveAll(houseBriefService.merge(houseBriefInfoList.stream().map(info ->
                HouseBrief.builder()
                        .sourceType(sourceType)
                        .houseId(info.getId())
                        .title(info.getTitle())
                        .communityName(info.getCommunityName())
                        .areaId(Optional.ofNullable(areaPathMap.get(info.getPath())).map(Area::getId).orElse(0L))
                        .areaNameCn(info.getPosition())
                        .shortInfo(info.getShortInfo())
                        .followInfo(info.getFollowInfo())
                        .totalPrice(info.getTotalPrice())
                        .unitPrice(info.getUnitPrice())
                        .tags(String.join(",", info.getTags()))
                        .detailUrl(info.getDetailUrl())
                        .build()
        )
                .collect(Collectors.toList()), dbInfoList));
    }

    /**
     * 存储明细信息
     *
     * @param resultItemsAll
     */
    private void saveHouseDetailInfo(Map<String, Object> resultItemsAll) {
        HouseDetailInfo houseDetailInfo = (HouseDetailInfo) resultItemsAll.get(ITEM_KEY_HOUSE_DETAIL_INFO);
        if (houseDetailInfo == null) {
            return;
        }

        houseDetailService.saveHouseDetail(houseDetailInfo);
    }
}
