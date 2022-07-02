package cn.xldeng.starter.remote;

import cn.xldeng.common.config.ApplicationContextHolder;
import cn.xldeng.common.web.base.Result;
import cn.xldeng.starter.config.DynamicThreadPoolProperties;
import cn.xldeng.starter.tookit.HttpClientUtil;

import java.util.Map;

/**
 * @program: threadpool
 * @description:
 * @author: dengxinlin
 * @create: 2022-06-29 01:21
 */
public class ServerHttpAgent implements HttpAgent {

    private final DynamicThreadPoolProperties dynamicThreadPoolProperties;

    private final ServerListManager serverListManager;

    private HttpClientUtil httpClientUtil = ApplicationContextHolder.getBean(HttpClientUtil.class);

    public ServerHttpAgent(DynamicThreadPoolProperties dynamicThreadPoolProperties) {
        this.dynamicThreadPoolProperties = dynamicThreadPoolProperties;
        this.serverListManager = new ServerListManager(dynamicThreadPoolProperties);
    }

    @Override
    public void start() {

    }

    @Override
    public String getTenantId() {
        return dynamicThreadPoolProperties.getTenantId();
    }

    @Override
    public String getEncode() {
        return null;
    }

    @Override
    public Result httpGet(String path, Map<String, String> headers, Map<String, String> paramValues, long readTimeoutMs) {
        return httpClientUtil.restApiGetByThreadPool(buildUrl(path), headers, paramValues, readTimeoutMs, Result.class);
    }

    @Override
    public Result httpPost(String path, Map<String, String> headers, Map<String, String> paramValues, long readTimeoutMs) {
        return httpClientUtil.restApiPostByThreadPool(buildUrl(path), headers, paramValues, readTimeoutMs, Result.class);
    }

    @Override
    public Result httpDelete(String path, Map<String, String> headers, Map<String, String> paramValues, long readTimeoutMs) {
        return null;
    }

    private String buildUrl(String path) {
        return serverListManager.getCurrentServerAddr() + path;
    }
}