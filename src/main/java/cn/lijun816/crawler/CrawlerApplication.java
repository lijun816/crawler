package cn.lijun816.crawler;

import cn.lijun816.crawler.processer.MainPageProcessor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan({"cn.lijun816.**", "cn.lijun816.*"})
public class CrawlerApplication implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(CrawlerApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {

    }
}
