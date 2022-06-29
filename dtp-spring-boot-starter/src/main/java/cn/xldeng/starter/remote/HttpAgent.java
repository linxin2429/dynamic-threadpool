package cn.xldeng.starter.remote;

import cn.xldeng.common.web.base.Result;

import java.util.Map;

/**
 * @program: threadpool
 * @description:
 * @author: dengxinlin
 * @create: 2022-06-29 01:20
 */
public interface HttpAgent {
    /**
     * 开始获取 NacosIp 集合
     */
    void start();

    /**
     * 获取命名空间
     *
     * @return nameSpace
     */
    String getNameSpace();

    /**
     * 获取编码集
     *
     * @return encode
     */
    String getEncode();

    /**
     * 发起 Http Get 请求
     *
     * @param path          path
     * @param headers       header
     * @param paramValues   paramValues
     * @param readTimeoutMs readTimeoutMs
     * @return response
     */
    Result httpGet(String path, Map<String, String> headers, Map<String, String> paramValues,
                   long readTimeoutMs);

    /**
     * 发起 Http Post 请求
     *
     * @param path          path
     * @param headers       headers
     * @param paramValues   paramValues
     * @param readTimeoutMs readTimeoutMs
     * @return response
     */
    Result httpPost(String path, Map<String, String> headers, Map<String, String> paramValues,
                    long readTimeoutMs);

    /**
     * 发起 Http Delete 请求
     *
     * @param path          path
     * @param headers       headers
     * @param paramValues   paramValues
     * @param readTimeoutMs readTimeoutMs
     * @return response
     */
    Result httpDelete(String path, Map<String, String> headers, Map<String, String> paramValues,
                      long readTimeoutMs);
}