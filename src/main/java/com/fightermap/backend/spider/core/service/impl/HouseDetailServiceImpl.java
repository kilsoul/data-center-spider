package com.fightermap.backend.spider.core.service.impl;

import com.fightermap.backend.spider.common.enums.SourceType;
import com.fightermap.backend.spider.core.model.bo.spider.HouseDetailInfo;
import com.fightermap.backend.spider.core.model.entity.Area;
import com.fightermap.backend.spider.core.model.entity.HouseBase;
import com.fightermap.backend.spider.core.model.entity.HouseFeature;
import com.fightermap.backend.spider.core.model.entity.HousePhoto;
import com.fightermap.backend.spider.core.model.entity.HouseRoomLayout;
import com.fightermap.backend.spider.core.model.entity.HouseTransaction;
import com.fightermap.backend.spider.core.service.AreaService;
import com.fightermap.backend.spider.core.service.HouseBaseService;
import com.fightermap.backend.spider.core.service.HouseDetailService;
import com.fightermap.backend.spider.core.service.HouseFeatureService;
import com.fightermap.backend.spider.core.service.HousePhotoService;
import com.fightermap.backend.spider.core.service.HouseRoomLayoutService;
import com.fightermap.backend.spider.core.service.HouseTransactionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * @author zengqk
 */
@Slf4j
@Service
public class HouseDetailServiceImpl implements HouseDetailService {

    private final AreaService areaService;

    private final HouseBaseService houseBaseService;

    private final HouseFeatureService houseFeatureService;

    private final HousePhotoService housePhotoService;

    private final HouseRoomLayoutService houseRoomLayoutService;

    private final HouseTransactionService houseTransactionService;

    public HouseDetailServiceImpl(AreaService areaService,
                                  HouseBaseService houseBaseService,
                                  HouseFeatureService houseFeatureService,
                                  HousePhotoService housePhotoService,
                                  HouseRoomLayoutService houseRoomLayoutService,
                                  HouseTransactionService houseTransactionService) {
        this.areaService = areaService;
        this.houseBaseService = houseBaseService;
        this.houseFeatureService = houseFeatureService;
        this.housePhotoService = housePhotoService;
        this.houseRoomLayoutService = houseRoomLayoutService;
        this.houseTransactionService = houseTransactionService;
    }

    @Override
    public void saveHouseDetail(HouseDetailInfo houseDetailInfo) {
        SourceType sourceType = houseDetailInfo.getSourceType();
        String path = houseDetailInfo.getPath();
        Optional<Area> areaOptional = areaService.findByPath(sourceType, path);

        HouseBase houseBase = houseBaseService.save(houseDetailInfo, areaOptional.map(Area::getId).orElse(0L));

        Long houseBaseId = houseBase.getId();

        HouseFeature houseFeature = houseFeatureService.save(houseDetailInfo,houseBaseId);
        HouseTransaction houseTransaction = houseTransactionService.save(houseDetailInfo,houseBaseId);
        List<HouseRoomLayout> houseRoomLayoutList = houseRoomLayoutService.save(houseDetailInfo,houseBaseId);
        List<HousePhoto> housePhotoList = housePhotoService.save(houseDetailInfo,houseBaseId);
    }
}
