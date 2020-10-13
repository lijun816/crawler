package cn.lijun816.crawler.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * DownloadReq
 *
 * @author lijun 2020/10/13
 */
@Data
public class DownloadReq implements Serializable {
    private Map<String, String> headers;
    private String url;
    private String title;
    private boolean useCache = true;
    private int timeout = 30000;
    private String type = "html";

    public DownloadReq putHeader(String k, String v) {
        if (headers == null) {
            headers = new HashMap<>();
        }
        headers.put(k, v);
        return this;
    }

    public DownloadReq(String url) {
        this.url = url;
    }
}