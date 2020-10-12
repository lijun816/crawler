package cn.lijun816.crawler.controller;

import cn.lijun816.crawler.processer.MainPageProcessor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.io.Serializable;

/**
 * RestController
 *
 * @author lijun 2020/10/12
 */
@RestController
public class OpenRestController {

    @GetMapping("start")
    public String start() throws IOException {
        new MainPageProcessor().crawler();
        return "success";
    }

}