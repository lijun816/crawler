package cn.lijun816.crawler;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.processor.example.GithubRepoPageProcessor;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class CrawlerApplication implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(CrawlerApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        Spider.create(new GithubRepoPageProcessor()).addUrl("http://www.baidu.com").thread(5).run();
    }
}
