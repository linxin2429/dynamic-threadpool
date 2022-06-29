package cn.xldeng.starter.core;

import cn.xldeng.starter.listener.Listener;

/**
 * @program: threadpool
 * @description:
 * @author: dengxinlin
 * @create: 2022-06-29 00:17
 */
public interface ConfigService {
    /**
     * 添加监听器, 如果服务端发生变更, 客户端会使用监听器进行回调
     *
     * @param tpId     tpId
     * @param listener listener
     */
    void addListener(String tpId, Listener listener);
}
