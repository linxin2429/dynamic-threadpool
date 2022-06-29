package cn.xldeng.starter.http;

import cn.xldeng.starter.config.DynamicThreadPoolProperties;

import java.util.Map;

/**
 * @program: threadpool
 * @description:
 * @author: dengxinlin
 * @create: 2022-06-29 01:21
 */
public class ServerHttpAgent implements HttpAgent{

    private final DynamicThreadPoolProperties dynamicThreadPoolProperties;

    public ServerHttpAgent(DynamicThreadPoolProperties dynamicThreadPoolProperties) {
        this.dynamicThreadPoolProperties = dynamicThreadPoolProperties;
    }

    @Override
    public void start() {

    }

    @Override
    public String getNameSpace() {
        return null;
    }

    @Override
    public String getEncode() {
        return null;
    }

    @Override
    public String httpGet(String path, Map<String, String> headers, Map<String, String> paramValues, String encoding, long readTimeoutMs) {
        return null;
    }

    @Override
    public String httpPost(String path, Map<String, String> headers, Map<String, String> paramValues, String encoding, long readTimeoutMs) {
        return null;
    }

    @Override
    public String httpDelete(String path, Map<String, String> headers, Map<String, String> paramValues, String encoding, long readTimeoutMs) {
        return null;
    }
}