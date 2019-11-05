package com.fightermap.backend.spider.common.util;

import com.fightermap.backend.spider.common.constant.Constant;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static com.fightermap.backend.spider.common.constant.Constant.SPLIT_PATH;
import static com.fightermap.backend.spider.common.constant.Regex.Lianjia.PAGE_KEY;


/**
 * @author zengqk
 */
public class PageUtil {
    public static final String REGEX_URL_PATH = "(http[s]?://[\\w.\\w]*)(/?\\w+)(/?.*/?)";
    public static final Pattern PATTERN_URL_PATH = Pattern.compile(REGEX_URL_PATH);

    public static String getLastUrlPath(String url, String split) {
        String[] urls = url.split(split);
        return urls[urls.length - 1];
    }

    public static String getMatcher(String content, String regex, int groupIndex) {
        Matcher matcher = Pattern.compile(regex).matcher(content);
        if (matcher.find()) {
            matcher.reset();
            while (matcher.find()) {
                return matcher.group(groupIndex);
            }
        }
        return Constant.EMPTY_STRING;
    }

    public static String subKeyBeforeWords(String words, String key) {
        if (!words.contains(key)) {
            return words;
        }
        return words.substring(0, words.indexOf(key));
    }

    public static String subKeyBackWords(String words, String key) {
        if (!words.contains(key)) {
            return words;
        }
        return words.substring(words.indexOf(key) + key.length());
    }

    public static boolean isMatch(String sequence, String regex) {
        return Pattern.compile(regex).matcher(sequence).matches();
    }

    public static String replaceKeyWord(String source, String sourceKey, String key) {
        return source.replace(sourceKey, key);
    }

    public static String removeEmojiCharacter(String target) {
        return target.replaceAll("[\\ud800\\udc00-\\udbff\\udfff\\ud800-\\udfff]", "*");
    }

    public static String subHost(String url) {
        return getMatcher(url, REGEX_URL_PATH, 1);
    }

    public static String subFirstDomain(String url) {
        return subDomain(url, 0);
    }

    public static String subDomain(String url, int index) {
        String ch = "://";
        int i = url.indexOf(ch);
        return url.substring(i + ch.length()).split("\\.")[index];
    }

    public static String subHostBase(String url) {
        return getMatcher(url, REGEX_URL_PATH, 1).concat(getMatcher(url, REGEX_URL_PATH, 2));
    }

    public static String concatUrlPath(String base, List<String> appenders) {
        String url = appendAfter(base, SPLIT_PATH)
                .concat(appenders.stream()
                        .map(s -> formatPath(s).replaceFirst("/", ""))
                        .collect(Collectors.joining("")));
        return formatUrl(url);
    }

    public static String concatUrlPath(String base, String... appenders) {
        String url = appendAfter(base, SPLIT_PATH)
                .concat(Arrays.stream(appenders)
                        .map(s -> formatPath(s).replaceFirst("/", ""))
                        .collect(Collectors.joining("")));
        return formatUrl(url);
    }

    public static String formatPath(String path) {
        return appendAfter(appendBefore(path, SPLIT_PATH), SPLIT_PATH);
    }

    public static String formatUrl(String url) {
        if (url.endsWith(SPLIT_PATH)) {
            return replaceLast(url, SPLIT_PATH, "", true);
        }
        return url;
    }

    public static String replaceHead(String str, String prefix, String replacement) {
        if (!str.startsWith(prefix)) {
            return str;
        }
        return str.replaceFirst(prefix, replacement);
    }

    public static String replaceLast(String str, String regex, String replacement, boolean ignore) {
        int i = str.lastIndexOf(regex);
        if (i >= 0) {
            String temp = str.substring(0, i).concat(replacement);
            return ignore ? temp : temp.concat(str.substring(i + 1));
        }
        return str.concat(replacement);
    }


    public static String appendBefore(String str, String split) {
        if (!str.startsWith(split)) {
            str = split.concat(str);
        }
        return str;
    }

    public static String appendAfter(String str, String split) {
        if (!str.endsWith(split)) {
            str = str.concat(split);
        }
        return str;
    }

    public static String getAreaFromUrl(String url, int pathCount) {
        String[] str = url.replace("//", "").split("/");
        int length = str.length;
        StringBuilder result = new StringBuilder();
        result.append("/").append(subFirstDomain(url));
        for (int i = pathCount; i > 0; i--) {
            result.append("/").append(str[length - i]);
        }
        return result.toString();
    }

    public static String getRawUrl(String url, String pageRegex) {
        return url.replaceAll(pageRegex, "");
    }

    public static void main(String[] args) {
        String url = "https://sh.lianjia.com/ershoufang/pudong";

//        System.out.println(subFirstDomain(url));

        String home = "https://sh.lianjia.com/ershoufang";
        String disUrl = "https://sh.lianjia.com/ershoufang/pudong";
        String posUrl = "https://sh.lianjia.com/ershoufang/pudong/zhangjiang";
        String posUrl1 = "https://sh.lianjia.com/ershoufang/pudong/zhangjiang/pg1";

        System.out.println(getRawUrl(posUrl, PAGE_KEY.concat("[0-9]+?/?")));
//
//        System.out.println(getAreaFromUrl(disUrl, 1));
//        System.out.println(getAreaFromUrl(posUrl, 2));


//        System.out.println(PageUtil.isMatch(home, Regex.Lianjia.HOME));
//        System.out.println(PageUtil.isMatch(disUrl, Regex.Lianjia.HOME));
//        System.out.println(PageUtil.isMatch(posUrl, Regex.Lianjia.HOME));
//        System.out.println(PageUtil.isMatch(home, Regex.Lianjia.AREA));
//        System.out.println(PageUtil.isMatch(disUrl, Regex.Lianjia.AREA));
//        System.out.println(PageUtil.isMatch(posUrl, Regex.Lianjia.AREA));
//        System.out.println(PageUtil.isMatch(home, Regex.Lianjia.POSITION));
//        System.out.println(PageUtil.isMatch(disUrl, Regex.Lianjia.POSITION));
//        System.out.println(PageUtil.isMatch(posUrl, Regex.Lianjia.POSITION));
//
//        System.out.println(PageUtil.getMatcher("https://sh.lianjia.com/ershoufang/pudong/zhangjiang/pg1", Regex.Lianjia.POSITION, 2));
//
//        System.out.println(Arrays.asList(replaceHead("/pudong/zhangjiang","/","").split("/")));

        String regex = "(http[s]?://[\\w.\\w]*)(/?\\w+)(/?.*/?)";
//        String regex = "http[s]?://[a-z]+\\.[a-zA-Z0-9]+/\\w+/.*";
//
//        System.out.println(isMatch(url,regex));
//
//        Matcher matcher = Pattern.compile(regex).matcher(url);
//        System.out.println(matcher.matches());
//        System.out.println(matcher.groupCount());
//
//        for (int i = 0; i <= matcher.groupCount(); i++) {
//            System.out.println(matcher.group(i));
//        }

//        System.out.println(subHost(url));
//        System.out.println(subHostBase(url));
//        System.out.println(concatUrlPath(subHost(url),"/ershoufang/pudong/pgx"));

//        System.out.println(subKeyBackWords("/ershoufang/pudong/pgx","/ershoufang/"));
//        System.out.println(subKeyBeforeWords("/ershoufang/pudong/pgx","/pg"));

//        System.out.println(replaceLast("abc", "b", "123", false));
//        System.out.println(replaceLast("abc", "d", "123", false));
//        System.out.println(replaceLast("abc", "a", "123", false));
//
//        System.out.println(Pattern.compile(Regex.Lianjia.REGION).matcher("https://sh.lianjia.com/ershoufang/pudong").matches());
//        System.out.println(Pattern.compile(Regex.Lianjia.DETAIL).matcher("https://sh.lianjia.com/ershoufang/107101753504.html").matches());
//        System.out.println(getMatcher("https://sh.lianjia.com/ershoufang/107101753504.html", Regex.Lianjia.DETAIL, 2));

    }
}
