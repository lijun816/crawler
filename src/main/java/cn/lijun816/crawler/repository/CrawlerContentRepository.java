package cn.lijun816.crawler.repository;

import cn.lijun816.crawler.domain.CrawlerContent;
import lombok.Data;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.io.Serializable;

/**
 * CrawlerContentRep
 *
 * @author 87922 2020/10/11
 */
@Repository
public interface CrawlerContentRepository extends JpaRepository<CrawlerContent, Integer> {

    CrawlerContent findTop1ByUrl(String url);

    CrawlerContent findTop1ByTitle(String title);
}