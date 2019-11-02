package com.fightermap.backend.spider.core.component.magic;

import com.fightermap.backend.spider.common.enums.AreaType;
import com.fightermap.backend.spider.common.enums.SourceType;
import com.fightermap.backend.spider.core.model.bo.spider.District;
import com.fightermap.backend.spider.core.model.bo.spider.HouseDetailInfo;
import com.fightermap.backend.spider.core.model.bo.spider.HouseShortInfo;
import com.fightermap.backend.spider.core.model.bo.spider.Position;
import com.fightermap.backend.spider.core.model.entity.Area;
import com.fightermap.backend.spider.core.service.AreaService;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;
import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.pipeline.Pipeline;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.fightermap.backend.spider.common.constant.Constant.ITEM_KEY_DISTRICT_LIST;
import static com.fightermap.backend.spider.common.constant.Constant.ITEM_KEY_HOUSE_DETAIL_INFO;
import static com.fightermap.backend.spider.common.constant.Constant.ITEM_KEY_HOUSE_SHORT_INFO_LIST;
import static com.fightermap.backend.spider.common.constant.Constant.ITEM_KEY_POSITION_LIST;

/**
 * @author zengqk
 */
@Service
public class DatabasePipeline implements Pipeline {

    private final AreaService areaService;

    public DatabasePipeline(AreaService areaService) {
        this.areaService = areaService;
    }

    @Override
    public void process(ResultItems resultItems, Task task) {
        System.out.println("Got page: " + resultItems.getRequest().getUrl());

        Map<String, Object> resultItemsAll = resultItems.getAll();

        //TODO 数据落库
//        saveCityResult(resultItemsAll);
        saveDistrictResult(resultItemsAll);
        savePositionResult(resultItemsAll);
        saveHouseShortInfoResult(resultItemsAll);
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
        List<String> names = districtList.stream().map(District::getName).collect(Collectors.toList());
        List<Area> parents = areaService.findAreaByType(sourceType, areaType.getParent());
        Long parentId = parents.stream().filter(p -> district.getCityName().equalsIgnoreCase(p.getName())).findFirst().map(Area::getId).orElse(0L);
        List<Area> dbInfoList = areaService.findAllBySourceTypeAndNameIn(sourceType, areaType, names);

        areaService.saveAll(areaService.merge(districtList.stream().map(d -> Area.builder()
                .sourceType(sourceType)
                .type(areaType)
                .name(d.getName())
                .nameCn(d.getChineseName())
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
        List<String> names = positionList.stream().map(Position::getName).collect(Collectors.toList());
        List<Area> parents = areaService.findAreaByType(sourceType, areaType.getParent());
        Long parentId = parents.stream().filter(p -> position.getDistrictName().equalsIgnoreCase(p.getName())).findFirst().map(Area::getId).orElse(0L);
        List<Area> dbInfoList = areaService.findAllBySourceTypeAndNameIn(sourceType, areaType, names);

        areaService.saveAll(areaService.merge(positionList.stream().map(d -> Area.builder()
                .sourceType(sourceType)
                .type(areaType)
                .name(d.getName())
                .nameCn(d.getChineseName())
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
    private void saveHouseShortInfoResult(Map<String, Object> resultItemsAll) {
        List<HouseShortInfo> houseShortInfoList = (List<HouseShortInfo>) resultItemsAll.get(ITEM_KEY_HOUSE_SHORT_INFO_LIST);
        if (CollectionUtils.isEmpty(houseShortInfoList)) {
            return;
        }
        System.out.println(houseShortInfoList);
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
        System.out.println(houseDetailInfo);
    }
}
