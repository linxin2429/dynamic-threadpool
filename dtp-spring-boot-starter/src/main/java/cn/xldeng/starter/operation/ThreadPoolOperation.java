package cn.xldeng.starter.operation;

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
        configService.addListener(tpId, listener);
        return listener;
    }
}