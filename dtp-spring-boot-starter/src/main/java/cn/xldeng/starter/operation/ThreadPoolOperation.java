package cn.xldeng.starter.operation;

import cn.xldeng.starter.config.DynamicThreadPoolProperties;
import cn.xldeng.starter.core.ConfigService;
import cn.xldeng.starter.listener.Listener;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.concurrent.Executor;

/**
 * @program: threadpool
 * @description:
 * @author: dengxinlin
 * @create: 2022-06-28 23:07
 */
public class ThreadPoolOperation {
    @Autowired
    private ConfigService configService;

    private final DynamicThreadPoolProperties properties;

    public ThreadPoolOperation(DynamicThreadPoolProperties properties) {
        this.properties = properties;
    }

    public Listener subscribeConfig(String tpId, Executor executor, ThreadPoolSubscribeCallback threadPoolSubscribeCallback) {
        Listener listener = new Listener() {
            @Override
            public Executor getExecutor() {
                return executor;
            }

            @Override
            public void receiveConfigInfo(String configInfo) {
                threadPoolSubscribeCallback.callback(configInfo);
            }
        };
        configService.addListener(properties.getNamespace(), properties.getItemId(), tpId, listener);
        return listener;
    }
}