package cn.xldeng.config.service;

import cn.xldeng.config.event.LocalDataChangeEvent;
import cn.xldeng.config.notify.NotifyCenter;

/**
 * @program: threadpool
 * @description:
 * @author: dengxinlin
 * @create: 2022-06-30 00:16
 */
public class ConfigChangePublisher {
    public static void notifyConfigChange(LocalDataChangeEvent event) {
        NotifyCenter.publishEvent(event);
    }
}