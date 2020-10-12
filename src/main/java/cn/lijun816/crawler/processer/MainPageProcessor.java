package cn.lijun816.crawler.processer;

import cn.lijun816.common.util.SpringBeanUtil;
import cn.lijun816.crawler.netservice.DownloadService;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

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
        Document document = downloadService.downloadHtml(YANG_PAI_URL);
        if (document == null) {
            log.warn("crawler::没有下载到主页");
            return;
        }
        Elements tagAs = document.select("div.playlist ul li a");
        for (Element li : tagAs) {
            String href = mainPageUrl.getProtocol() + "://" + mainPageUrl.getHost() + li.attr("href");
            twoLevelParser(href);
            break;
        }
    }

    private void twoLevelParser(String href) throws IOException {
        Document document = downloadService.downloadHtml(href);
        if (document == null) {
            return;
        }
        Elements select = document.select("iframe#play");
        if (select.first() == null) {
            return;
        }
        String src = select.first().attr("src");
        src = mainPageUrl.getProtocol() + "://" + mainPageUrl.getHost() + src;
        threeLevelParser(src);
    }

    private void threeLevelParser(String href) throws IOException {
        Document document = downloadService.downloadHtml(href);
        if (document == null) {
            return;
        }
        Elements select = document.select("iframe#play");
        if (select.first() == null) {
            return;
        }
        String src = select.first().attr("src");
        src = mainPageUrl.getProtocol() + "://" + mainPageUrl.getHost() + src;
        threeLevelParser(src);
    }
}