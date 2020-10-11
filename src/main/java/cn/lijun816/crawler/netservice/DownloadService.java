package cn.lijun816.crawler.netservice;

import cn.lijun816.crawler.domain.CrawlerContent;
import cn.lijun816.crawler.repository.CrawlerContentRepository;
import lombok.Data;
import org.jsoup.Jsoup;
import org.jsoup.internal.StringUtil;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.io.IOException;
import java.io.Serializable;

import static cn.lijun816.common.constants.NetConstant.AGENT;

/**
 * DownloadService
 *
 * @author 87922 2020/10/11
 */
@Service
public class DownloadService implements Serializable {
    @Resource
    private CrawlerContentRepository crawlerContentRepository;

    public Document downloadHtml(String url) throws IOException {
        return downloadHtml(url, true, 30000);
    }

    public Document downloadHtml(String url, boolean useCache) throws IOException {
        return downloadHtml(url, useCache, 30000);
    }

    public Document downloadHtml(String url, boolean useCache, int timeout) throws IOException {
        if (useCache) {
            CrawlerContent content = crawlerContentRepository.findTop1ByUrl(url);
            if (content != null && Boolean.TRUE.equals(content.getHasDownload())) {
                if (StringUtils.hasText(content.getText())) {
                    return Jsoup.parse(content.getText());
                }
                return null;
            }
        }
        Document document = Jsoup.connect(url).userAgent(AGENT).timeout(timeout).get();
        if (document != null) {
            CrawlerContent content = crawlerContentRepository.findTop1ByUrl(url);
            if (content == null) {
                content = new CrawlerContent();
            }
            content.setHasDownload(true).setText(document.html()).setType("html");
            crawlerContentRepository.save(content);
        }
        return document;
    }


}