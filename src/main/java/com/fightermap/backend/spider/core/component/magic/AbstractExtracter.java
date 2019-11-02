package com.fightermap.backend.spider.core.component.magic;

import com.fightermap.backend.spider.core.model.bo.spider.District;
import com.fightermap.backend.spider.core.model.bo.spider.HouseDetailInfo;
import com.fightermap.backend.spider.core.model.bo.spider.HouseShortInfo;
import com.fightermap.backend.spider.core.model.bo.spider.Position;
import org.jsoup.nodes.Document;

import java.util.List;

/**
 * 数据提取器
 *
 * @author zengqk
 */
public abstract class AbstractExtracter {
    private String host;

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public abstract List<District> extractDistrict(Document document);

    public abstract List<Position> extractPosition(Document document);

    public abstract List<HouseShortInfo> extractShortInfo(Document document);

    public abstract HouseDetailInfo extractDetailInfo(Document document);

    public abstract HouseDetailInfo.BaseInfo extractBaseInfo(Document document);

    public abstract HouseDetailInfo.TransactionInfo extractTransactionInfo(Document document);

    public abstract HouseDetailInfo.FeatureInfo extractFeatureInfo(Document document);

    public abstract List<HouseDetailInfo.RoomLayout> extractRoomLayouts(Document document);

    public abstract List<HouseDetailInfo.HousePhoto> extractHousePhotos(Document document);
}
