package cn.lijun816.crawler.netservice;

import cn.lijun816.common.util.FileNameUtil;
import cn.lijun816.crawler.domain.CrawlerContent;
import cn.lijun816.crawler.dto.DownloadReq;
import cn.lijun816.crawler.repository.CrawlerContentRepository;
import com.google.common.io.ByteStreams;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.internal.StringUtil;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StreamUtils;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.io.*;
import java.net.Authenticator;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.Map;

import static cn.lijun816.common.constants.NetConstant.AGENT;

/**
 * DownloadService
 *
 * @author 87922 2020/10/11
 */
@Service
@Slf4j
public class DownloadService implements Serializable {
    @Resource
    private CrawlerContentRepository crawlerContentRepository;

    public Document downloadHtml(DownloadReq req) throws IOException {
        String url = req.getUrl();
        Map<String, String> headers = req.getHeaders();
        boolean useCache = req.isUseCache();
        int timeout = req.getTimeout();
        if (useCache) {
            CrawlerContent content = crawlerContentRepository.findTop1ByUrl(url);
            if (content != null && Boolean.TRUE.equals(content.getHasDownload())) {
                if (StringUtils.hasText(content.getText())) {
                    return Jsoup.parse(content.getText());
                }
                return null;
            }
        }
        Connection connect = Jsoup.connect(url);
        if (!ObjectUtils.isEmpty(headers)) {
            connect.headers(headers);
        }
        Document document = connect.userAgent(AGENT).timeout(timeout).get();
        if (document != null) {
            CrawlerContent content = crawlerContentRepository.findTop1ByUrl(url);
            if (content == null) {
                content = new CrawlerContent();
                content.setUrl(url);
            }
            content.setHasDownload(true).setText(document.html()).setType(req.getType());
            crawlerContentRepository.save(content);
        }
        return document;
    }

    public void downloadMedia(DownloadReq req) throws IOException {
        String url = req.getUrl();
        log.info("downloadMedia:判断正在下载:{}", url);
        Map<String, String> headers = req.getHeaders();
        boolean useCache = req.isUseCache();
        int timeout = req.getTimeout();
        String title = req.getTitle();
        if (useCache) {
            CrawlerContent content = crawlerContentRepository.findTop1ByUrl(url);
            if (content != null && Boolean.TRUE.equals(content.getHasDownload())) {
                String filePath = content.getFilePath();
                if (Files.exists(Paths.get(filePath))) {
                    log.info("downloadMedia:找到缓存不再下载:{}", url);
                    return;
                }

            }
            if (StringUtils.hasText(title)) {
                content = crawlerContentRepository.findTop1ByTitle(title);
                if (content != null && Boolean.TRUE.equals(content.getHasDownload())) {
                    String filePath = content.getFilePath();
                    if (Files.exists(Paths.get(filePath))) {
                        log.info("downloadMedia:找到缓存不再下载:{}", url);
                        return;
                    }
                }
            }
        }
        Connection connect = Jsoup.connect(url);
        if (!ObjectUtils.isEmpty(headers)) {
            connect.headers(headers);
        }
        log.info("downloadMedia:真正下载:{}", url);
        Connection.Response response = connect.userAgent(AGENT).ignoreContentType(true).maxBodySize(0)
                .timeout(300000).method(Connection.Method.GET).execute();
        if (response != null) {
            BufferedInputStream bufferedInputStream = response.bodyStream();
            Path target2 = Paths.get("E:\\data\\" + FileNameUtil.filenameFilter(title) + ".mp3");
            File file = target2.toFile();
            if (!file.exists()) {
                File parentFile = file.getParentFile();
                if (!parentFile.exists()) {
                    parentFile.mkdirs();
                }
                file.createNewFile();
            }
            FileOutputStream to = new FileOutputStream(file);
            ByteStreams.copy(bufferedInputStream, to);
            to.close();
            log.info("downloadMedia:下载完成:{}", url);
            CrawlerContent content = crawlerContentRepository.findTop1ByUrl(url);
            if (content == null) {
                if (StringUtils.hasText(title)) {
                    content = crawlerContentRepository.findTop1ByTitle(title);
                }
                if (content == null) {
                    content = new CrawlerContent();
                    content.setUrl(url);
                }
            }
            content.setHasDownload(true).setTitle(title)
                    .setFilePath(target2.toAbsolutePath().toString())
                    .setType(req.getType());
            crawlerContentRepository.save(content);
        }
    }

}