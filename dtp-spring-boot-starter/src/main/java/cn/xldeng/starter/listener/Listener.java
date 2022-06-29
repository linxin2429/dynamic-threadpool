package cn.xldeng.starter.listener;

import java.util.concurrent.Executor;

/**
 * @program: threadpool
 * @description: 监听器
 * @author: dengxinlin
 * @create: 2022-06-28 23:02
 */
public interface Listener {
    /**
     * 获取执行器
     *
     * @return Executor
     */
    Executor getExecutor();

    /**
     * 接受配置信息
     *
     * @param configInfo 配置信息
     */
    void receiveConfigInfo(String configInfo);
}