package cn.lijun816.crawler.processer;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.pipeline.JsonFilePipeline;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.processor.example.GithubRepoPageProcessor;
import us.codecraft.webmagic.processor.example.ZhihuPageProcessor;
import us.codecraft.webmagic.selector.Html;
import us.codecraft.webmagic.selector.Selectable;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * TestProcessor
 *
 * @author lijun 2020/10/9
 */
@Slf4j
public class TestProcessor implements PageProcessor {

    private static final Site site = Site.me().setRetryTimes(3).setTimeOut(10000).setSleepTime(1000);

    @Override
    public void process(Page page) {
        Html html = page.getHtml();
        log.info("process::{}", html);
        List<String> href = html.css("div.playlist ul li a", "href").all();
        page.putField("href", href);
    }

    @Override
    public Site getSite() {
        return site;
    }

    public static void main(String[] args) {
        Spider thread = Spider.create(new TestProcessor()).thread(1).addPipeline(new JsonFilePipeline("E:\\webmagic\\"));
        ResultItems resultItems = thread.get("http://www.2uxs.com/youshengxiaoshuo/9808/");
        if (resultItems == null) {
            log.info("main:爬取完成没有任何元素");
            return;
        }
        resultItems.get("href");
        thread.close();
    }

    static class ContentProcessor implements PageProcessor {

        private static final Site site = Site.me().setRetryTimes(3).setTimeOut(10000).setSleepTime(1000);

        @Override
        public void process(Page page) {

        }

        @Override
        public Site getSite() {
            return site;
        }
    }
}