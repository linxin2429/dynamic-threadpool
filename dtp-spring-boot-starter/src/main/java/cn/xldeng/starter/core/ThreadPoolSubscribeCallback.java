package cn.xldeng.starter.core;

/**
 * @program: threadpool
 * @description:
 * @author: dengxinlin
 * @create: 2022-06-29 00:29
 */
public interface ThreadPoolSubscribeCallback {
    /**
     * 回调函数
     *
     * @param config config
     */
    void callback( String config);
}