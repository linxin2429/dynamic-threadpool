package cn.xldeng.server.service;

import cn.xldeng.server.event.ConfigDataChangeEvent;
import cn.xldeng.server.notify.NotifyCenter;

/**
 * @program: threadpool
 * @description:
 * @author: dengxinlin
 * @create: 2022-06-30 00:16
 */
public class ConfigChangePublisher {
    public static void notifyConfigChange(ConfigDataChangeEvent event) {
        NotifyCenter.publishEvent(event);
    }
}