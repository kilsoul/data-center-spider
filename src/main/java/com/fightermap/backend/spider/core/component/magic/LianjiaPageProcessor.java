package com.fightermap.backend.spider.core.component.magic;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fightermap.backend.spider.common.cache.Memory;
import com.fightermap.backend.spider.common.constant.Regex;
import com.fightermap.backend.spider.common.enums.SpiderLevel;
import com.fightermap.backend.spider.common.util.PageUtil;
import com.fightermap.backend.spider.core.model.bo.spider.District;
import com.fightermap.backend.spider.core.model.bo.spider.HouseBriefInfo;
import com.fightermap.backend.spider.core.model.bo.spider.HouseDetailInfo;
import com.fightermap.backend.spider.core.model.bo.spider.Position;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.pipeline.ConsolePipeline;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.selector.Html;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.fightermap.backend.spider.common.constant.Constant.ITEM_KEY_DISTRICT_LIST;
import static com.fightermap.backend.spider.common.constant.Constant.ITEM_KEY_HOUSE_DETAIL_INFO;
import static com.fightermap.backend.spider.common.constant.Constant.ITEM_KEY_HOUSE_SHORT_INFO_LIST;
import static com.fightermap.backend.spider.common.constant.Constant.ITEM_KEY_POSITION_LIST;
import static com.fightermap.backend.spider.common.constant.Regex.Lianjia.PAGE_KEY;
import static com.fightermap.backend.spider.common.util.PageUtil.getMatcher;
import static com.fightermap.backend.spider.common.util.PageUtil.getRawUrl;
import static com.fightermap.backend.spider.common.util.PageUtil.subHost;

/**
 * @author zengqk
 */
@Slf4j
public class LianjiaPageProcessor implements PageProcessor {
    private Site site = Site.me().setRetryTimes(3).setSleepTime(1000).setTimeOut(10000);

    @Override
    public void process(Page page) {
        String url = page.getUrl().get();
        Html html = page.getHtml();
        Document document = Jsoup.parse(html.toString());

        AbstractExtracter lianjiaExtracter = new LianjiaExtracter();
        lianjiaExtracter.setHost(subHost(url));
        lianjiaExtracter.setUrl(url);

        if (PageUtil.isMatch(url, Regex.Lianjia.HOME)) {
            //获取区级信息
            List<District> districtList = lianjiaExtracter.extractDistrict(document);
            final List<String> requests = Memory.addAll(districtList.stream().map(District::getUrl).collect(Collectors.toList()));

            page.putField(ITEM_KEY_DISTRICT_LIST, districtList.stream().filter(district -> requests.contains(district.getUrl())).collect(Collectors.toList()));
            //高优先级
            page.addTargetRequests(requests, SpiderLevel.HIGH.getValue());
        } else if (PageUtil.isMatch(url, Regex.Lianjia.AREA)) {
            String positionName = document.select("div.position > dl > dd > div[data-role=ershoufang] > div:nth-child(2) > a.selected").attr("href");
            if (StringUtils.isEmpty(positionName)) {
                //区级信息页面，获取position
                List<Position> positionList = lianjiaExtracter.extractPosition(document);
                final List<String> positionRequests = Memory.addAll(positionList.stream().map(Position::getUrl).collect(Collectors.toList()));

                page.putField(ITEM_KEY_POSITION_LIST, positionList.stream().filter(position -> positionRequests.contains(position.getUrl())).collect(Collectors.toList()));
                //高优先级
                page.addTargetRequests(positionRequests.stream().map(s -> s.concat(PAGE_KEY).concat("1")).collect(Collectors.toList()), SpiderLevel.HIGH.getValue());
            } else {
                String pageUrlInfo = getMatcher(url, Regex.Lianjia.POSITION, 3);
                if (pageUrlInfo == null || pageUrlInfo.replace(PAGE_KEY, "").equals("1")) {
                    //第一页,需要放分页信息
                    String pageJson = document.select("#content > div.leftContent > div.contentBottom.clear > div.page-box.fr > div.page-box.house-lst-page-box").attr("page-data");
                    ObjectMapper mapper = new ObjectMapper();
                    Integer totalPage = 0;
                    try {
                        //获取分页信息
                        Map<String, Object> pageMapper = mapper.readValue(pageJson, Map.class);
                        totalPage = (Integer) pageMapper.getOrDefault("totalPage", 0);
                    } catch (Exception e) {
                        log.warn("Failed to get page count from string[{}]!", pageJson);
                    }

                    if (totalPage > 1) {
                        List<String> pageableUrls = new ArrayList<>();
                        String rawUrl = getRawUrl(url, PAGE_KEY.concat("[0-9]+?/?"));
                        for (int i = 2; i <= totalPage; i++) {
                            pageableUrls.add(rawUrl.concat(PAGE_KEY).concat(String.valueOf(i)));
                        }

                        page.addTargetRequests(Memory.addAll(pageableUrls), SpiderLevel.NORMAL.getValue());
                    }
                }

                //获取简要信息
                List<HouseBriefInfo> houseBriefInfoList = lianjiaExtracter.extractBriefInfo(document);
                final List<String> houseDetailRequests = Memory.addAll(houseBriefInfoList.stream().map(HouseBriefInfo::getDetailUrl).collect(Collectors.toList()));

                page.putField(ITEM_KEY_HOUSE_SHORT_INFO_LIST, houseBriefInfoList.stream().filter(houseBriefInfo -> houseDetailRequests.contains(houseBriefInfo.getDetailUrl())).collect(Collectors.toList()));
                //中优先级
                page.addTargetRequests(houseDetailRequests, SpiderLevel.NORMAL.getValue());
            }
        } else if (PageUtil.isMatch(url, Regex.Lianjia.DETAIL)) {
            //获取明细信息
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

    public static void main(String[] args) {
        Spider.create(new LianjiaPageProcessor())
//                .addUrl("https://sh.lianjia.com/ershoufang")
//                .addUrl("https://sh.lianjia.com/ershoufang/pudong")
//                .addUrl("https://sh.lianjia.com/ershoufang/beicai/pg1")
//                .addUrl("https://sh.lianjia.com/ershoufang/beicai")
                .addUrl("https://sh.lianjia.com/ershoufang/107100960067.html")
                .addPipeline(new ConsolePipeline())
                .run();
    }
}
