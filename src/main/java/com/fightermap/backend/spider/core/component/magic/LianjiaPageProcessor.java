package com.fightermap.backend.spider.core.component.magic;

import com.fightermap.backend.spider.common.cache.Memory;
import com.fightermap.backend.spider.common.constant.Regex;
import com.fightermap.backend.spider.common.enums.SpiderLevel;
import com.fightermap.backend.spider.common.util.PageUtil;
import com.fightermap.backend.spider.core.model.bo.spider.District;
import com.fightermap.backend.spider.core.model.bo.spider.HouseDetailInfo;
import com.fightermap.backend.spider.core.model.bo.spider.HouseShortInfo;
import com.fightermap.backend.spider.core.model.bo.spider.Position;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.selector.Html;

import java.util.List;
import java.util.stream.Collectors;

import static com.fightermap.backend.spider.common.constant.Constant.ITEM_KEY_DISTRICT_LIST;
import static com.fightermap.backend.spider.common.constant.Constant.ITEM_KEY_HOUSE_DETAIL_INFO;
import static com.fightermap.backend.spider.common.constant.Constant.ITEM_KEY_HOUSE_SHORT_INFO_LIST;
import static com.fightermap.backend.spider.common.constant.Constant.ITEM_KEY_POSITION_LIST;
import static com.fightermap.backend.spider.common.util.PageUtil.getMatcher;
import static com.fightermap.backend.spider.common.util.PageUtil.subHost;

/**
 * @author zengqk
 */
public class LianjiaPageProcessor implements PageProcessor {
    private Site site = Site.me().setRetryTimes(3).setSleepTime(1000).setTimeOut(10000);

    @Override
    public void process(Page page) {
        String url = page.getUrl().get();
        Html html = page.getHtml();
        Document document = Jsoup.parse(html.toString());

        AbstractExtracter lianjiaExtracter = new LianjiaExtracter();
        lianjiaExtracter.setHost(subHost(url));

        if (PageUtil.isMatch(url, Regex.Lianjia.HOME)) {
            List<District> districtList = lianjiaExtracter.extractDistrict(document);
            final List<String> requests = Memory.addAll(districtList.stream().map(District::getUrl).collect(Collectors.toList()));

            page.putField(ITEM_KEY_DISTRICT_LIST, districtList.stream().filter(district -> requests.contains(district.getUrl())).collect(Collectors.toList()));
            //高优先级
            page.addTargetRequests(requests, SpiderLevel.HIGH.getValue());
        } else if (PageUtil.isMatch(url, Regex.Lianjia.DISTRICT)) {
            List<Position> positionList = lianjiaExtracter.extractPosition(document);
            final List<String> positionRequests = Memory.addAll(positionList.stream().map(Position::getUrl).collect(Collectors.toList()));

            page.putField(ITEM_KEY_POSITION_LIST, positionList.stream().filter(position -> positionRequests.contains(position.getUrl())).collect(Collectors.toList()));
            //高优先级
//            page.addTargetRequests(positionRequests, SpiderLevel.HIGH.getValue());

        } else if (PageUtil.isMatch(url, Regex.Lianjia.POSITION)) {
            //TODO 分页
            List<HouseShortInfo> houseShortInfoList = lianjiaExtracter.extractShortInfo(document);
            final List<String> houseShortInfoRequests = Memory.addAll(houseShortInfoList.stream().map(HouseShortInfo::getDetailUrl).collect(Collectors.toList()));

            page.putField(ITEM_KEY_HOUSE_SHORT_INFO_LIST, houseShortInfoList.stream().filter(houseShortInfo -> houseShortInfoRequests.contains(houseShortInfo.getDetailUrl())).collect(Collectors.toList()));
            //中优先级
//            page.addTargetRequests(houseShortInfoRequests, SpiderLevel.NORMAL.getValue());
        } else if (PageUtil.isMatch(url, Regex.Lianjia.DETAIL)) {
            //TODO 分页
            HouseDetailInfo houseDetailInfo = lianjiaExtracter.extractDetailInfo(document);
            houseDetailInfo.setId(getMatcher(url, Regex.Lianjia.DETAIL, 2));
            houseDetailInfo.setUrl(url);

            page.putField(ITEM_KEY_HOUSE_DETAIL_INFO, houseDetailInfo);
        }
    }


    @Override
    public Site getSite() {
        return site;
    }

//    public static void main(String[] args) {
//        Spider.create(new LianjiaPageProcessor())
////                .addUrl("https://sh.lianjia.com/ershoufang")
//                .addUrl("https://sh.lianjia.com/ershoufang/pudong")
////                .addUrl("https://sh.lianjia.com/ershoufang/pudong/beicai")
////                .addUrl("https://sh.lianjia.com/ershoufang/107100960067.html")
//                .addPipeline(new ConsolePipeline())
//                .run();
//    }
}
