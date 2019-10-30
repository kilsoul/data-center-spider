package com.fightermap.backend.spider.core.component.magic;

import com.fightermap.backend.spider.core.model.District;
import com.fightermap.backend.spider.core.model.HouseDetailInfo;
import com.fightermap.backend.spider.core.model.HouseShortInfo;
import com.fightermap.backend.spider.core.model.Position;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.pipeline.ConsolePipeline;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.selector.Html;

import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.CompletableFuture;

import static com.fightermap.backend.spider.common.constant.Constant.DEFAULT_DATE_FORMATTER;
import static com.fightermap.backend.spider.common.constant.Constant.SQUARE_METER;
import static com.fightermap.backend.spider.common.constant.Mapper.*;
import static com.fightermap.backend.spider.common.util.AsyncUtil.acquire;
import static com.fightermap.backend.spider.common.util.AsyncUtil.execute;
import static com.fightermap.backend.spider.common.util.ClassUtil.getFieldType;
import static com.fightermap.backend.spider.common.util.ClassUtil.setField;

public class LianjiaPageProcessor implements PageProcessor {
    private Site site = Site.me().setRetryTimes(3).setSleepTime(1000).setTimeOut(10000);

    @Override
    public void process(Page page) {
        String host = page.getUrl().get();
        Html html = page.getHtml();
        Document document = Jsoup.parse(html.toString());

        HouseDetailInfo houseDetailInfo = getDetailInfo(document);
        System.out.println(houseDetailInfo);
    }

    /**
     * 获取区级
     */
    private List<District> getDistrict(String host, Document document) {
        List<District> districtList = new ArrayList<>();
        Elements elements = document.select("div.district > dl > dd > div[data-role=ershoufang] > div > a[title]");
        for (Element element : elements) {
            String districtName = element.attr("href").replace("/ershoufang/", "").replaceAll("/", "");
            District district = new District();
            district.setName(districtName);
            district.setUrl(host.concat("/").concat(districtName));
            district.setChineseName(element.text());
            districtList.add(district);
        }
        return districtList;
    }

    /**
     * 获取镇级
     */
    private List<Position> getPosition(String host, Document document) {
        List<Position> positionList = new ArrayList<>();
        Elements elements = document.select("div.position > dl > dd > div[data-role=ershoufang] > div > a:not([title])");
        for (Element element : elements) {
            String positionName = element.attr("href").replace("/ershoufang/", "").replaceAll("/", "");
            Position position = new Position();
            position.setName(positionName);
            position.setUrl(host.concat("/").concat(positionName));
            position.setChineseName(element.text());
            positionList.add(position);
        }
        return positionList;
    }

    /**
     * 获取简要信息
     *
     * @param document
     */
    private List<HouseShortInfo> getShorInfo(Document document) {
        Elements secondHandHouseList = document.select("#content > div.leftContent > ul.sellListContent > li[class^=clear]");
        System.out.println(secondHandHouseList.size());
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
    private HouseDetailInfo getDetailInfo(Document document) {
        CompletableFuture<HouseDetailInfo.BaseInfo> baseInfoFuture = acquire(() -> getBaseInfo(document));
        CompletableFuture<HouseDetailInfo.TransactionInfo> transactionInfoFuture = acquire(() -> getTransactionInfo(document));
        CompletableFuture<HouseDetailInfo.FeatureInfo> featureInfoFuture = acquire(() -> getFeatureInfo(document));
        CompletableFuture<List<HouseDetailInfo.RoomLayout>> roomLayoutsFuture = acquire(() -> getRoomLayouts(document));
        CompletableFuture<List<HouseDetailInfo.HousePhoto>> housePhotosFuture = acquire(() -> getHousePhotos(document));

        return HouseDetailInfo.builder()
                .baseInfo(execute(baseInfoFuture, "Get base info", new HouseDetailInfo.BaseInfo()))
                .transactionInfo(execute(transactionInfoFuture, "Get transaction info", new HouseDetailInfo.TransactionInfo()))
                .featureInfo(execute(featureInfoFuture, "Get feature info", new HouseDetailInfo.FeatureInfo()))
                .roomLayouts(execute(roomLayoutsFuture, "Get room layouts", new ArrayList<>()))
                .housePhotos(execute(housePhotosFuture, "Get house photos", new ArrayList<>()))
                .build();
    }

    /**
     * 基本信息
     *
     * @param document
     * @return
     */
    private HouseDetailInfo.BaseInfo getBaseInfo(Document document) {
        HouseDetailInfo.BaseInfo baseInfo = new HouseDetailInfo.BaseInfo();
        Elements baseElements = document.select("#introduction > div > div.introContent > div.base > div.content > ul > li");
        for (Element element : baseElements) {
            String label = element.select("span.label").first().ownText().trim();
            String fieldName = LIANJIA_BASE_INFO_MAPPER.getOrDefault(label, null);
            String tagValue = element.ownText().trim();
            Object value = tagValue;
            switch (fieldName) {
                case "constructionArea":
                    value = Float.valueOf(tagValue.replaceAll(SQUARE_METER, ""));
                    break;
                case "actualArea":
                    value = exists(tagValue) ? Float.valueOf(tagValue.replaceAll(SQUARE_METER, "")) : null;
                    break;
                case "hasLift":
                    value = exists(tagValue);
                    break;
                case "houseHoldYears":
                    value = Integer.valueOf(tagValue.replaceAll("年", "").trim());
                    break;
                default:
                    break;
            }
            setField(baseInfo, fieldName, value);
        }
        return baseInfo;
    }

    /**
     * 交易信息
     *
     * @param document
     * @return
     */
    private HouseDetailInfo.TransactionInfo getTransactionInfo(Document document) {
        HouseDetailInfo.TransactionInfo transactionInfo = new HouseDetailInfo.TransactionInfo();
        Elements transactionElements = document.select("#introduction > div > div.introContent > div.transaction > div.content > ul > li");
        for (Element element : transactionElements) {
            String label = element.select("span.label").first().ownText().trim();
            String fieldName = LIANJIA_TRANSACTION_INFO_MAPPER.getOrDefault(label, null);
            String tagValue = element.select("span:not([class])").first().ownText().trim();
            Object value = tagValue;
            switch (fieldName) {
                case "listingDate":
                    value = LocalDate.parse(tagValue, DEFAULT_DATE_FORMATTER);
                    break;
                case "lastTransactionDate":
                    value = LocalDate.parse(tagValue, DEFAULT_DATE_FORMATTER);
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
        return transactionInfo;
    }

    /**
     * 房源特色
     *
     * @param document
     * @return
     */
    private HouseDetailInfo.FeatureInfo getFeatureInfo(Document document) {
        HouseDetailInfo.FeatureInfo featureInfo = new HouseDetailInfo.FeatureInfo();
        Elements featureElements = document.select("div.m-content > div.box-l > div.baseinform > div.showbasemore > div.baseattribute");
        for (Element element : featureElements) {
            String label = element.select("div.name").first().ownText().trim();
            String fieldName = LIANJIA_FEATURE_INFO_MAPPER.getOrDefault(label, null);
            String tagValue = element.select("div.content").first().ownText().trim();
            Object value = tagValue;
            switch (fieldName) {
                default:
                    break;
            }
            setField(featureInfo, fieldName, value);
        }
        return featureInfo;
    }

    /**
     * 房间布局
     *
     * @param document
     * @return
     */
    private List<HouseDetailInfo.RoomLayout> getRoomLayouts(Document document) {
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
    private List<HouseDetailInfo.HousePhoto> getHousePhotos(Document document) {
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

    @Override
    public Site getSite() {
        return site;
    }

    public static void main(String[] args) {
        Spider.create(new LianjiaPageProcessor())
//                .addUrl("https://sh.lianjia.com/ershoufang")
//                .addUrl("https://sh.lianjia.com/ershoufang/pudong")
//                .addUrl("https://sh.lianjia.com/ershoufang/pudong/zhangjiang/pg1")
                .addUrl("https://sh.lianjia.com/ershoufang/107101753504.html")
                .addPipeline(new ConsolePipeline())
                .run();
    }
}
