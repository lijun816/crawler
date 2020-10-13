package cn.lijun816.crawler.domain;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

/**
 * CrawlerContent
 *
 * @author 87922 2020/10/11
 */
@Entity(name = "crawler_content")
@Data
public class CrawlerContent implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String url;

    private String title;

    @Column(columnDefinition = "CLOB")
    private String text;

    private String type;

    private Boolean hasDownload;

    private String filePath;

}