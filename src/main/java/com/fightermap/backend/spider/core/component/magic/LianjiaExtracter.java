package com.fightermap.backend.spider.core.component.magic;

import com.fightermap.backend.spider.common.constant.Regex;
import com.fightermap.backend.spider.common.enums.SourceType;
import com.fightermap.backend.spider.core.model.bo.spider.District;
import com.fightermap.backend.spider.core.model.bo.spider.HouseDetailInfo;
import com.fightermap.backend.spider.core.model.bo.spider.HouseShortInfo;
import com.fightermap.backend.spider.core.model.bo.spider.Position;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import static com.fightermap.backend.spider.common.constant.Constant.DEFAULT_DATE_FORMATTER;
import static com.fightermap.backend.spider.common.constant.Constant.SQUARE_METER;
import static com.fightermap.backend.spider.common.constant.Mapper.LIANJIA_BASE_INFO_MAPPER;
import static com.fightermap.backend.spider.common.constant.Mapper.LIANJIA_FEATURE_INFO_MAPPER;
import static com.fightermap.backend.spider.common.constant.Mapper.LIANJIA_TRANSACTION_INFO_MAPPER;
import static com.fightermap.backend.spider.common.constant.Mapper.exists;
import static com.fightermap.backend.spider.common.constant.Mapper.existsTransfer;
import static com.fightermap.backend.spider.common.util.AsyncUtil.acquire;
import static com.fightermap.backend.spider.common.util.AsyncUtil.execute;
import static com.fightermap.backend.spider.common.util.ClassUtil.setField;
import static com.fightermap.backend.spider.common.util.PageUtil.concatUrlPath;
import static com.fightermap.backend.spider.common.util.PageUtil.subFirstDomain;
import static com.fightermap.backend.spider.common.util.PageUtil.subKeyBeforeWords;

/**
 * lianjia.com 数据提取器
 *
 * @author zengqk
 */
public class LianjiaExtracter extends AbstractExtracter {
    private final SourceType sourceType = SourceType.LIANJIA;

    /**
     * 获取区级
     */
    @Override
    public List<District> extractDistrict(Document document) {
        List<District> districtList = new ArrayList<>();
        Elements elements = document.select("div.position > dl > dd > div[data-role=ershoufang] > div > a[title]");
        for (Element element : elements) {
            //like: /ershoufang/pudong/pgx
            String href = element.attr("href");
            String districtPath = subKeyBeforeWords(href, Regex.Lianjia.PAGE_KEY);
            District district = new District();
            district.setName(subDistrictName(href));
            district.setUrl(concatUrlPath(super.getHost(), Arrays.asList(districtPath)));
            district.setChineseName(element.text());
            district.setHost(super.getHost());
            district.setSourceType(sourceType);
            district.setCityName(subFirstDomain(super.getHost()));
            districtList.add(district);
        }
        return districtList;
    }

    private String subDistrictName(String href) {
        return subKeyBeforeWords(href, Regex.Lianjia.PAGE_KEY).replace(Regex.Lianjia.SECOND_HAND_KEY, "").replaceAll("/", "").trim();
    }

    /**
     * 获取镇级
     */
    @Override
    public List<Position> extractPosition(Document document) {
        List<Position> positionList = new ArrayList<>();
        String districtName = subDistrictName(document.select("div.position > dl > dd > div[data-role=ershoufang] > div > a[title].selected").first().attr("href"));
        Elements elements = document.select("div.position > dl > dd > div[data-role=ershoufang] > div > a:not([title])");
        for (Element element : elements) {
            // like: /ershoufang/beicai/pgx
            String positionPath = subKeyBeforeWords(element.attr("href"), Regex.Lianjia.PAGE_KEY);
            List<String> positionPaths = Arrays.asList(positionPath.split("/"));
            List<String> paths = new ArrayList<>();
            paths.add(positionPaths.get(1));
            paths.add(districtName);
            paths.add(positionPaths.get(2));
            Position position = new Position();
            position.setName(positionPath.replace(Regex.Lianjia.SECOND_HAND_KEY, "").replaceAll("/", "").trim());
            position.setUrl(concatUrlPath(super.getHost(), paths));
            position.setChineseName(element.text());
            position.setDistrictName(districtName);
            position.setHost(super.getHost());
            position.setSourceType(sourceType);
            positionList.add(position);
        }
        return positionList;
    }

    /**
     * 获取简要信息
     *
     * @param document
     */
    @Override
    public List<HouseShortInfo> extractShortInfo(Document document) {
        Elements secondHandHouseList = document.select("#content > div.leftContent > ul.sellListContent > li[class^=clear]");
        List<HouseShortInfo> result = new ArrayList<>();
        for (Element content : secondHandHouseList) {
            String id = content.select("a.noresultRecommend.img").attr("data-housecode");
            String detailUrl = content.select("a.noresultRecommend.img").attr("href");
            String title = content.select("div > div.title > a").text();
            String communityName = content.select("div > div.flood > div.positionInfo > a[data-el=region]").text();
            String position = content.select("div > div.flood > div.positionInfo > a").get(1).text();
            String shortInfo = content.select("div > div.address > div.houseInfo").text().trim();
            String followInfo = content.select("div > div.followInfo").text().trim();
            List<String> tags = content.select("div > div.tag > span").eachText();
            String totalPrice = content.select("div > div.priceInfo > div.totalPrice > span").text();
            String unitPrice = content.select("div > div.priceInfo > div.unitPrice").text();

            HouseShortInfo data = new HouseShortInfo();
            data.setId(id);
            data.setDetailUrl(detailUrl);
            data.setTitle(title);
            data.setCommunityName(communityName);
            data.setPosition(position);
            data.setShortInfo(shortInfo);
            data.setFollowInfo(followInfo);
            data.setTags(tags);
            data.setTotalPrice(totalPrice);
            data.setUnitPrice(unitPrice.replaceAll("单价", "").replaceAll("元/平米", ""));
            data.setHost(super.getHost());
            data.setSourceType(sourceType);
            result.add(data);
        }
        return result;
    }

    /**
     * 获取明细信息
     *
     * @param document
     * @return
     */
    @Override
    public HouseDetailInfo extractDetailInfo(Document document) {
        CompletableFuture<HouseDetailInfo.BaseInfo> baseInfoFuture = acquire(() -> extractBaseInfo(document));
        CompletableFuture<HouseDetailInfo.TransactionInfo> transactionInfoFuture = acquire(() -> extractTransactionInfo(document));
        CompletableFuture<HouseDetailInfo.FeatureInfo> featureInfoFuture = acquire(() -> extractFeatureInfo(document));
        CompletableFuture<List<HouseDetailInfo.RoomLayout>> roomLayoutsFuture = acquire(() -> extractRoomLayouts(document));
        CompletableFuture<List<HouseDetailInfo.HousePhoto>> housePhotosFuture = acquire(() -> extractHousePhotos(document));

        HouseDetailInfo houseDetailInfo = HouseDetailInfo.builder()
                .baseInfo(execute(baseInfoFuture, "Get base info", new HouseDetailInfo.BaseInfo()))
                .transactionInfo(execute(transactionInfoFuture, "Get transaction info", new HouseDetailInfo.TransactionInfo()))
                .featureInfo(execute(featureInfoFuture, "Get feature info", new HouseDetailInfo.FeatureInfo()))
                .roomLayouts(execute(roomLayoutsFuture, "Get room layouts", new ArrayList<>()))
                .housePhotos(execute(housePhotosFuture, "Get house photos", new ArrayList<>()))
                .build();
        houseDetailInfo.setHost(super.getHost());
        houseDetailInfo.setSourceType(sourceType);

        return houseDetailInfo;
    }

    /**
     * 基本信息
     *
     * @param document
     * @return
     */
    @Override
    public HouseDetailInfo.BaseInfo extractBaseInfo(Document document) {
        HouseDetailInfo.BaseInfo baseInfo = new HouseDetailInfo.BaseInfo();
        Elements baseElements = document.select("#introduction > div > div.introContent > div.base > div.content > ul > li");
        for (Element element : baseElements) {
            String label = element.select("span.label").first().ownText().trim();
            String fieldName = LIANJIA_BASE_INFO_MAPPER.getOrDefault(label, null);
            String tagValue = element.ownText().trim();
            Object value = tagValue;
            if (fieldName == null) {
                baseInfo.getUnknownField().put(label, tagValue);
            } else {
                switch (fieldName) {
                    case "constructionArea":
                        value = existsTransfer(tagValue, () -> Float.valueOf(tagValue.replaceAll(SQUARE_METER, "")), null);
                        break;
                    case "actualArea":
                        value = existsTransfer(tagValue, () -> Float.valueOf(tagValue.replaceAll(SQUARE_METER, "")), null);
                        break;
                    case "hasLift":
                        value = exists(tagValue);
                        break;
                    case "houseHoldYears":
                        value = existsTransfer(tagValue, () -> Integer.valueOf(tagValue.replaceAll("年", "").trim()), null);
                        break;
                    default:
                        break;
                }
                setField(baseInfo, fieldName, value);
            }
        }
        return baseInfo;
    }

    /**
     * 交易信息
     *
     * @param document
     * @return
     */
    @Override
    public HouseDetailInfo.TransactionInfo extractTransactionInfo(Document document) {
        HouseDetailInfo.TransactionInfo transactionInfo = new HouseDetailInfo.TransactionInfo();
        Elements transactionElements = document.select("#introduction > div > div.introContent > div.transaction > div.content > ul > li");
        for (Element element : transactionElements) {
            String label = element.select("span.label").first().ownText().trim();
            String fieldName = LIANJIA_TRANSACTION_INFO_MAPPER.getOrDefault(label, null);
            String tagValue = element.select("span:not([class])").first().ownText().trim();
            Object value = tagValue;
            if (fieldName == null) {
                transactionInfo.getUnknownField().put(label, tagValue);
            } else {
                switch (fieldName) {
                    case "listingDate":
                        value = existsTransfer(tagValue, () -> LocalDate.parse(tagValue, DEFAULT_DATE_FORMATTER), null);
                        break;
                    case "lastTransactionDate":
                        value = existsTransfer(tagValue, () -> LocalDate.parse(tagValue, DEFAULT_DATE_FORMATTER), null);
                        break;
                    case "mortgageInfo":
                        //处理是否包含抵押信息
                        transactionInfo.setHasMortgage(exists(tagValue));
                        break;
                    default:
                        break;
                }
                setField(transactionInfo, fieldName, value);
            }
        }
        return transactionInfo;
    }

    /**
     * 房源特色
     *
     * @param document
     * @return
     */
    @Override
    public HouseDetailInfo.FeatureInfo extractFeatureInfo(Document document) {
        HouseDetailInfo.FeatureInfo featureInfo = new HouseDetailInfo.FeatureInfo();
        Elements featureElements = document.select("div.m-content > div.box-l > div.baseinform > div.showbasemore > div.baseattribute");
        for (Element element : featureElements) {
            String label = element.select("div.name").first().ownText().trim();
            String fieldName = LIANJIA_FEATURE_INFO_MAPPER.getOrDefault(label, null);
            String tagValue = element.select("div.content").first().ownText().trim();
            Object value = tagValue;
            if (fieldName == null) {
                featureInfo.getUnknownField().put(label, tagValue);
            } else {
                switch (fieldName) {
                    default:
                        break;
                }
                setField(featureInfo, fieldName, value);
            }
        }
        return featureInfo;
    }

    /**
     * 房间布局
     *
     * @param document
     * @return
     */
    @Override
    public List<HouseDetailInfo.RoomLayout> extractRoomLayouts(Document document) {
        List<HouseDetailInfo.RoomLayout> roomLayouts = new ArrayList<>();
        Elements layoutElements = document.select("#infoList > div.row");
        for (Element element : layoutElements) {
            HouseDetailInfo.RoomLayout roomLayout = new HouseDetailInfo.RoomLayout();
            roomLayout.setType(element.select("div:nth-child(1)").text());
            roomLayout.setArea(element.select("div:nth-child(2)").text());
            roomLayout.setOrientation(element.select("div:nth-child(3)").text());
            roomLayout.setWindow(element.select("div:nth-child(4)").text());
            roomLayouts.add(roomLayout);
        }
        return roomLayouts;
    }

    /**
     * 房屋照片
     *
     * @param document
     * @return
     */
    @Override
    public List<HouseDetailInfo.HousePhoto> extractHousePhotos(Document document) {
        List<HouseDetailInfo.HousePhoto> housePhotos = new ArrayList<>();
        Elements pictureElements = document.select("div.m-content > div.box-l > div.newwrap > div.housePic > div.container > div.list > div[data-index]");
        for (Element element : pictureElements) {
            housePhotos.add(HouseDetailInfo.HousePhoto.builder()
                    .desc(element.select("span.name").first().ownText())
                    .uri(element.select("img").first().attr("src"))
                    .build());
        }
        return housePhotos;
    }
}
