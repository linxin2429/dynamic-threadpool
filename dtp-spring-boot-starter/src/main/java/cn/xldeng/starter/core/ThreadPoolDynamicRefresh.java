package cn.xldeng.starter.core;

import cn.xldeng.starter.model.PoolParameterInfo;
import cn.xldeng.starter.wrap.DynamicThreadPoolWrap;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @program: threadpool
 * @description: 线程池动态监听
 * @author: dengxinlin
 * @create: 2022-06-27 07:34
 */
@Slf4j
public class ThreadPoolDynamicRefresh {

    public static void refreshDynamicPool(String content) {
        log.info("[🔥] Start refreshing configuration. content :: {}", content);
        PoolParameterInfo parameter = JSON.parseObject(content, PoolParameterInfo.class);
        refreshDynamicPool(parameter.getTpId(), parameter.getCoreSize(), parameter.getMaxSize(), parameter.getCapacity(), parameter.getKeepAliveTime());
    }

    public static void refreshDynamicPool(String threadPoolId, Integer coreSize, Integer maxSize, Integer capacity, Integer keepAliveTime) {
        DynamicThreadPoolWrap wrap = GlobalThreadPoolManage.getExecutorService(threadPoolId);
        ThreadPoolExecutor executor = wrap.getPool();
        if (coreSize != null) {
            executor.setCorePoolSize(coreSize);
        }
        if (maxSize != null) {
            executor.setMaximumPoolSize(maxSize);
        }
        if (capacity != null) {
            ResizableCapacityLinkedBlockingQueue queue = (ResizableCapacityLinkedBlockingQueue) executor.getQueue();
            queue.setCapacity(capacity);
        }
        if (keepAliveTime != null) {
            executor.setKeepAliveTime(keepAliveTime, TimeUnit.SECONDS);
        }

    }
}