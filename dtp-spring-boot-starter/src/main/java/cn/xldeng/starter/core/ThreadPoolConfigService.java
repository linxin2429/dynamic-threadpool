package cn.xldeng.starter.core;

import cn.xldeng.starter.config.DynamicThreadPoolProperties;
import cn.xldeng.starter.listener.ClientWorker;
import cn.xldeng.starter.listener.Listener;
import cn.xldeng.starter.remote.HttpAgent;
import cn.xldeng.starter.remote.ServerHttpAgent;

import java.util.Collections;

/**
 * @program: threadpool
 * @description:
 * @author: dengxinlin
 * @create: 2022-06-29 00:19
 */
public class ThreadPoolConfigService implements ConfigService {

    private final HttpAgent httpAgent;

    private final ClientWorker clientWorker;

    public ThreadPoolConfigService(DynamicThreadPoolProperties properties) {
        httpAgent = new ServerHttpAgent(properties);
        this.clientWorker = new ClientWorker(httpAgent);
    }

    @Override
    public void addListener(String tenantId, String itemId, String tpId, Listener listener) {
        clientWorker.addTenantListeners(tenantId, itemId, tpId, Collections.singletonList(listener));
    }

    @Override
    public String getServerStatus() {
        if (clientWorker.isHealthServer()) {
            return "UP";
        } else {
            return "DOWN";
        }
    }
}