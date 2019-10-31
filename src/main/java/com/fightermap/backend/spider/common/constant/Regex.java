package com.fightermap.backend.spider.common.constant;

public class Regex {


    public static class Lianjia {
        public static final String PAGE_KEY = "/pg";
        public static final String SECOND_HAND_KEY = "ershoufang";
        public static final String REGION = "(http[s]?://[\\w.\\w]*)/"+SECOND_HAND_KEY+"/.*/.*("+PAGE_KEY+"[0-9]+)?/?";
        public static final String HOME = "(http[s]?://[\\w.\\w]*)/"+SECOND_HAND_KEY+"/?";
        public static final String DETAIL = "(http[s]?://[\\w.\\w]*)/"+SECOND_HAND_KEY+"/([0-9]+)\\.html";
    }


}
