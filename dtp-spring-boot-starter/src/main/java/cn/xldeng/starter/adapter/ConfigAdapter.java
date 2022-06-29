package cn.xldeng.starter.adapter;

import cn.xldeng.starter.core.ThreadPoolDynamicRefresh;

/**
 * @program: threadpool
 * @description:
 * @author: dengxinlin
 * @create: 2022-06-29 00:55
 */
public class ConfigAdapter {
    /**
     * 回调修改线程池配置
     *
     * @param config config
     */
    public void callbackConfig(String config) {
        ThreadPoolDynamicRefresh.refreshDynamicPool(config);
    }
}