package cn.xldeng.starter.tookit;

import com.alibaba.fastjson.JSON;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.nio.charset.StandardCharsets;
import java.util.Map;

/**
 * @program: threadpool
 * @description:
 * @author: dengxinlin
 * @create: 2022-06-27 07:35
 */
@Slf4j
public class HttpClientUtil {

    @Autowired
    private OkHttpClient okHttpClient;

    private MediaType jsonMediaType = MediaType.parse("application/json; charset=utf-8");

    private static final int HTTP_OK_CODE = 200;

    /**
     * Get 请求
     *
     * @param url
     * @return
     */
    @SneakyThrows
    public String get(String url) {
        try {
            return new String(doGet(url), StandardCharsets.UTF_8);
        } catch (Exception e) {
            log.error("httpGet 调用失败. {}", url, e);
            throw e;
        }
    }

    /**
     * Get 请求, 支持添加查询字符串
     *
     * @param url
     * @param queryString 查询字符串
     * @return
     */
    public String get(String url, Map<String, Object> queryString) {
        String fullUrl = queryString(url, queryString);
        return get(fullUrl);
    }

    /**
     * 获取 Json 后直接反序列化
     *
     * @param url
     * @param clazz
     * @return
     */
    public <T> T restApiGet(String url, Class<T> clazz) {
        String resp = get(url);
        return JSON.parseObject(resp, clazz);
    }

    /**
     * Get 请求, 支持查询字符串
     *
     * @param url
     * @param queryString
     * @param clazz
     * @param <T>
     * @return
     */
    public <T> T restApiGet(String url, Map<String, Object> queryString, Class<T> clazz) {
        String fullUrl = queryString(url, queryString);
        String resp = get(fullUrl);
        return JSON.parseObject(resp, clazz);
    }

    /**
     * Rest 接口 Post 调用
     *
     * @param url  url
     * @param body request body
     * @return
     */
    public String restApiPost(String url, Object body) {
        try {
            return doPost(url, body);
        } catch (Exception e) {
            log.error("httpPost 调用失败. {}", url, e);
            throw e;
        }
    }

    /**
     * Rest 接口 Post 调用
     * 对返回值直接反序列化
     *
     * @param url  url
     * @param body request body
     * @return
     */
    public <T> T restApiPost(String url, Object body, Class<T> clazz) {
        String resp = restApiPost(url, body);
        return JSON.parseObject(resp, clazz);
    }

    /**
     * 根据查询字符串构造完整的 Url
     *
     * @param url         url
     * @param queryString 查询字符串
     * @return
     */
    public String queryString(String url, Map<String, Object> queryString) {
        if (null == queryString) {
            return url;
        }
        StringBuilder builder = new StringBuilder(url);
        boolean isFirst = true;
        for (Map.Entry<String, Object> entry : queryString.entrySet()) {
            String key = entry.getKey();
            if (key != null && entry.getValue() != null) {
                if (isFirst) {
                    isFirst = false;
                    builder.append("?");
                } else {
                    builder.append("&");
                }
                builder.append(key)
                        .append("=")
                        .append(entry.getValue());
            }
        }
        return builder.toString();
    }

    @SneakyThrows
    private String doPost(String url, Object body) {
        String jsonBody = JSON.toJSONString(body);
        RequestBody requestBody = RequestBody.create(jsonMediaType, jsonBody);
        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();
        Response response = okHttpClient.newCall(request).execute();
        if (response.code() != HTTP_OK_CODE) {
            String msg = String.format("HttpPost 响应 code 异常. [code] %s [url] %s [body] %s", response.code(), url, jsonBody);
            throw new RuntimeException(msg);
        }
        assert response.body() != null;
        return response.body().string();
    }

    @SneakyThrows
    private byte[] doGet(String url) {
        Request request = new Request.Builder().get().url(url).build();
        Response response = okHttpClient.newCall(request).execute();
        if (response.code() != HTTP_OK_CODE) {
            String msg = String.format("HttpGet 响应 code 异常. [code] %s [url] %s", response.code(), url);
            throw new RuntimeException(msg);
        }
        assert response.body() != null;
        return response.body().bytes();
    }
}