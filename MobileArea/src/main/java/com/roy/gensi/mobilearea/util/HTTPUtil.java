package com.roy.gensi.mobilearea.util;

import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;

import java.io.IOException;

/**
 * @author ：楼兰
 * @date ：Created in 2021/5/6
 * @description:
 **/

public class HTTPUtil {
    private static Logger logger = Logger.getLogger(HTTPUtil.class);

    /**
     * 发送httpget请求
     *
     * @param url
     *            要请求的url
     * @return
     * @throws ClientProtocolException
     * @throws IOException
     */
    public static String httpGet(String url) throws ClientProtocolException, IOException {
        // http执行请求对象
        CloseableHttpClient closeableHttpClient = null;

        // http响应对象
        CloseableHttpResponse closeableHttpResponse = null;

        // 返回的html
        String html = null;
        try {
            closeableHttpClient = HttpClients.createDefault();

            // 定义http请求
            HttpGet httpGet = new HttpGet(url);

            // 添加通用请求头
            httpGet.addHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
            httpGet.addHeader("Accept-Encoding", "gzip, deflate, sdch");
            httpGet.addHeader("Accept-Language", "zh-CN,zh;q=0.8");
            httpGet.addHeader("Cache-Control", "no-cache");
            httpGet.addHeader("Connection", "keep-alive");
            httpGet.addHeader("Pragma", "no-cache");
            httpGet.addHeader("Upgrade-Insecure-Requests", "1");
            httpGet.addHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/56.0.2924.87 Safari/537.36");

            // 执行http请求
            closeableHttpResponse = closeableHttpClient.execute(httpGet);

            // 获取响应的html
            HttpEntity httpEntity = closeableHttpResponse.getEntity();
            if (httpEntity != null) {
                html = EntityUtils.toString(httpEntity, "utf-8");
            }

            // 标记响应实体已经被消息，以释放资源
            EntityUtils.consume(httpEntity);
            return html;
        } finally {
            try {
                if (closeableHttpResponse != null) {
                    closeableHttpResponse.close();
                }
                if (closeableHttpClient != null) {
                    closeableHttpClient.close();
                }
            } catch (Exception e) {
                logger.warn("HttpResponse或HttpClient资源释放失败,url:" + url, e);
            }
        }
    }

    /**
     * 用指定的代理ip发送httpget请求
     *
     * @param url
     *            要请求的url
     * @param proxyIp
     *            代理ip地址
     * @param proxyPort
     *            代理端口号
     * @param httpType
     *            http类型：http,https
     * @param timeout
     *            超时时间
     * @return
     * @throws ClientProtocolException
     * @throws IOException
     */
    public static String httpGet(String url, String proxyIp, int proxyPort, String httpType, int timeout) throws ClientProtocolException, IOException {
        // http执行请求对象
        CloseableHttpClient closeableHttpClient = null;

        // http响应对象
        CloseableHttpResponse closeableHttpResponse = null;

        // 返回的html
        String html = null;
        try {
            closeableHttpClient = HttpClients.createDefault();

            // 定义http请求
            HttpGet httpGet = new HttpGet(url);

            // 添加通用请求头
            httpGet.addHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
            httpGet.addHeader("Accept-Encoding", "gzip, deflate, sdch");
            httpGet.addHeader("Accept-Language", "zh-CN,zh;q=0.8");
            httpGet.addHeader("Cache-Control", "no-cache");
            httpGet.addHeader("Connection", "keep-alive");
            httpGet.addHeader("Pragma", "no-cache");
            httpGet.addHeader("Upgrade-Insecure-Requests", "1");
            httpGet.addHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/56.0.2924.87 Safari/537.36");

            // 设置代理
            HttpHost proxy = new HttpHost(proxyIp, proxyPort, httpType);

            // 设置请求配置
            RequestConfig requestConfig = RequestConfig.custom().setProxy(proxy).setConnectionRequestTimeout(2000).setConnectTimeout(timeout).setSocketTimeout(timeout).build();
            httpGet.setConfig(requestConfig);

            // 执行http请求
            closeableHttpResponse = closeableHttpClient.execute(httpGet);

            // 获取响应的html
            HttpEntity httpEntity = closeableHttpResponse.getEntity();
            if (httpEntity != null) {
                html = EntityUtils.toString(httpEntity, "utf-8");
            }

            // 标记响应实体已经被消息，以释放资源
            EntityUtils.consume(httpEntity);
            return html;
        } finally {
            try {
                if (closeableHttpResponse != null) {
                    closeableHttpResponse.close();
                }
                if (closeableHttpClient != null) {
                    closeableHttpClient.close();
                }
            } catch (Exception e) {
                logger.warn("HttpResponse或HttpClient资源释放失败,url:" + url, e);
            }
        }
    }
}
