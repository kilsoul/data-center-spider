package com.fightermap.backend.spider.core.component.magic;

import com.fightermap.backend.spider.core.model.District;
import com.fightermap.backend.spider.core.model.HouseDetailInfo;
import com.fightermap.backend.spider.core.model.HouseShortInfo;
import com.fightermap.backend.spider.core.model.Position;
import org.apache.commons.collections.CollectionUtils;
import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.pipeline.Pipeline;

import java.util.List;
import java.util.Map;

import static com.fightermap.backend.spider.common.constant.Constant.ITEM_KEY_DISTRICT_LIST;
import static com.fightermap.backend.spider.common.constant.Constant.ITEM_KEY_HOUSE_DETAIL_INFO;
import static com.fightermap.backend.spider.common.constant.Constant.ITEM_KEY_HOUSE_SHORT_INFO_LIST;
import static com.fightermap.backend.spider.common.constant.Constant.ITEM_KEY_POSITION_LIST;

/**
 * @author zengqk
 */
public class DatabasePipeline implements Pipeline {

    @Override
    public void process(ResultItems resultItems, Task task) {
        System.out.println("Got page: " + resultItems.getRequest().getUrl());

        Map<String, Object> resultItemsAll = resultItems.getAll();

        //TODO 数据落库
        saveDistrictResult(resultItemsAll);
        savePositionResult(resultItemsAll);
        saveHouseShortInfoResult(resultItemsAll);
        saveHouseDetailInfo(resultItemsAll);
    }

    private void saveDistrictResult(Map<String, Object> resultItemsAll) {
        List<District> districtList = (List<District>) resultItemsAll.get(ITEM_KEY_DISTRICT_LIST);
        if (CollectionUtils.isEmpty(districtList)) {
            return;
        }
        System.out.println(districtList);
    }

    private void savePositionResult(Map<String, Object> resultItemsAll) {
        List<Position> positionList = (List<Position>) resultItemsAll.get(ITEM_KEY_POSITION_LIST);
        if (CollectionUtils.isEmpty(positionList)) {
            return;
        }
        System.out.println(positionList);
    }

    private void saveHouseShortInfoResult(Map<String, Object> resultItemsAll) {
        List<HouseShortInfo> houseShortInfoList = (List<HouseShortInfo>) resultItemsAll.get(ITEM_KEY_HOUSE_SHORT_INFO_LIST);
        if (CollectionUtils.isEmpty(houseShortInfoList)) {
            return;
        }
        System.out.println(houseShortInfoList);
    }

    private void saveHouseDetailInfo(Map<String, Object> resultItemsAll) {
        HouseDetailInfo houseDetailInfo = (HouseDetailInfo) resultItemsAll.get(ITEM_KEY_HOUSE_DETAIL_INFO);
        if (houseDetailInfo == null) {
            return;
        }
        System.out.println(houseDetailInfo);
    }
}
