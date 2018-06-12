package com.wangyin.autotest.utils;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.client.LaxRedirectStrategy;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 通用的httpClient封装.
 *
 * @author chenyun.chris
 * @since 1.0
 */
public class HttpClientUtils {

    private static final RequestConfig DEFAULT_REQUEST_CONFIG = RequestConfig.custom()
            .setSocketTimeout(30000)
            .setConnectTimeout(5000)
            .setConnectionRequestTimeout(5000)
            .setStaleConnectionCheckEnabled(true)
            .build();

    private static final LaxRedirectStrategy LAX_REDIRECT_STRATEGY = new LaxRedirectStrategy();

    private static String post(String url, Map<String, String> header, HttpEntity httpEntity) throws IOException {
        CloseableHttpClient client = null;

        try {
            client = HttpClients.custom()
                    .setDefaultRequestConfig(DEFAULT_REQUEST_CONFIG)
                    .setRedirectStrategy(LAX_REDIRECT_STRATEGY)
                    .setUserAgent("Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1)")
                    .build();

            HttpPost post = new HttpPost(url);

            post.setEntity(httpEntity);

            for(String key : header.keySet()) {
                post.addHeader(key, header.get(key));
            }

            CloseableHttpResponse response = client.execute(post);

            StringBuilder responseStr = new StringBuilder("StatusCode:");
            responseStr.append(response.getStatusLine().getStatusCode()).append("\n");
            responseStr.append(EntityUtils.toString(response.getEntity()));

            return responseStr.toString();

        } finally {

            if(client != null) {
                client.close();
            }

        }
    }

    public static String get(String url, Map<String, String> header) throws IOException {
        CloseableHttpClient client = null;

        try {
            client = HttpClients.custom()
                    .setDefaultRequestConfig(DEFAULT_REQUEST_CONFIG)
                    .setRedirectStrategy(LAX_REDIRECT_STRATEGY)
                    .setUserAgent("Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1)")
                    .build();

            HttpGet get = new HttpGet(url);

            for(String key : header.keySet()) {
                get.addHeader(key, header.get(key));
            }

            CloseableHttpResponse response = client.execute(get);

            StringBuilder responseStr = new StringBuilder("StatusCode:");
            responseStr.append(response.getStatusLine().getStatusCode()).append("\n");
            responseStr.append(EntityUtils.toString(response.getEntity()));

            return responseStr.toString();

        } finally {

            if(client != null) {
                client.close();
            }

        }
    }

    public static String textPost(String url, Map<String, String> header,
                                  String charset, String text, String mimeType) throws IOException {
        HttpEntity reqEntity = new StringEntity(text, ContentType.create(mimeType, charset));
        return post(url, header, reqEntity);
    }

    public static String formPost(String url, Map<String, String> header,
                                  String charset, Map<String, String> form) throws IOException {

        List<NameValuePair> formParams = new ArrayList<NameValuePair>();

        for(String key : form.keySet()) {
            formParams.add(new BasicNameValuePair(key, form.get(key)));
        }

        HttpEntity reqEntity = new UrlEncodedFormEntity(formParams, charset);
        return post(url, header, reqEntity);
    }


}
