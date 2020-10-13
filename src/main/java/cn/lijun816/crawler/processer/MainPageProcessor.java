package cn.lijun816.crawler.processer;

import cn.lijun816.common.util.SpringBeanUtil;
import cn.lijun816.crawler.dto.DownloadReq;
import cn.lijun816.crawler.netservice.DownloadService;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * TestProcessor
 *
 * @author lijun 2020/10/9
 */
@Slf4j
public class MainPageProcessor {

    private final static String YANG_PAI_URL = "http://www.2uxs.com/youshengxiaoshuo/9808/";

    private static URL mainPageUrl;

    static {
        try {
            mainPageUrl = new URL(YANG_PAI_URL);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    private final DownloadService downloadService = SpringBeanUtil.getBean(DownloadService.class);

    public void crawler() throws IOException {
        // 下载首页
        DownloadReq req = new DownloadReq(YANG_PAI_URL).setUseCache(false);
        Document document = downloadService.downloadHtml(req);
        if (document == null) {
            log.warn("crawler::没有下载到主页");
            return;
        }
        Elements tagAs = document.select("div.playlist ul li a");
        int i = 0;
        for (Element li : tagAs) {
            String title = li.text();
            String href = mainPageUrl.getProtocol() + "://" + mainPageUrl.getHost() + li.attr("href");
            twoLevelParser(title, new DownloadReq(href).setUseCache(false));
            if (i++ > 50) {
                break;
            }
        }
    }

    private void twoLevelParser(String title, DownloadReq req) throws IOException {
        Document document = downloadService.downloadHtml(req);
        if (document == null) {
            return;
        }
        Elements select = document.select("iframe#play");
        if (select.first() == null) {
            return;
        }
        String src = select.first().attr("src");
        src = mainPageUrl.getProtocol() + "://" + mainPageUrl.getHost() + src;
        DownloadReq downloadReq = new DownloadReq(src)
                .putHeader("Referer", req.getUrl());
        threeLevelParser(title, downloadReq);
    }

    private void threeLevelParser(String title, DownloadReq downloadReq) throws IOException {
        downloadReq.setUseCache(false);
        Document document = downloadService.downloadHtml(downloadReq);
        if (document == null) {
            return;
        }
        String mp3 = parseUrl(document.html());
        if (mp3 == null) {
            log.warn("threeLevelParser:没有解析到路径:{}", downloadReq.getUrl());
            return;
        }
        DownloadReq req = new DownloadReq(mp3)
                .setTitle(title)
                .putHeader("authority", "mp3.dongporen.top")
                .putHeader("Referer", "http://www.2uxs.com/");
        downloadService.downloadMedia(req);
    }

    private final static Pattern compile = Pattern.compile("mp3:'(.*)'");
    private final static Pattern compile1 = Pattern.compile("'\\+(.*)\\+'.*");

    public String parseUrl(String content) {
        Matcher matcher = compile.matcher(content);
        if (matcher.find()) {
            String group1 = matcher.group(1);
            if (group1.contains("'+") && group1.contains("+'")) {
                Matcher matcher1 = compile1.matcher(group1);
                if (matcher1.find()) {
                    String varUrl = matcher1.group(1);
                    Pattern compile = Pattern.compile("(?:^|\\n)" + varUrl + " = '(.*)';");
                    Matcher matcher2 = compile.matcher(content);
                    if (matcher2.find()) {
                        String txt = matcher2.group(1);
                        int endIndex = txt.indexOf("'+");
                        if (endIndex > -1) {
                            txt = txt.substring(0, endIndex);
                            txt += ".mp3";
                        }
                        System.out.println(txt);
                        group1 = group1.replace("'", "")
                                .replace("+", "").replace(varUrl, txt);
                    }
                }
            }
            String realMp3Url = "";
            if (group1.startsWith("http")) {
                realMp3Url = group1;
            } else {
                realMp3Url = "http://www.2uxs.com" + group1;
            }
            return realMp3Url;
        }
        return null;
    }
}