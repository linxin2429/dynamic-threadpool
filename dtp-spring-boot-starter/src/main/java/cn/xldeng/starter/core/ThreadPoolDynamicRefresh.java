package cn.xldeng.starter.core;

import cn.xldeng.common.model.PoolParameterInfo;
import cn.xldeng.starter.handler.ThreadPoolChangeHandler;
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
        PoolParameterInfo parameter = JSON.parseObject(content, PoolParameterInfo.class);
        String tpId = parameter.getTpId();
        Integer coreSize = parameter.getCoreSize(), maxSize = parameter.getMaxSize(),
                queueType = parameter.getQueueType(), capacity = parameter.getCapacity(),
                keepAliveTime = parameter.getKeepAliveTime();
        refreshDynamicPool(tpId, coreSize, maxSize, queueType, capacity, keepAliveTime);
    }

    public static void refreshDynamicPool(String threadPoolId, Integer coreSize, Integer maxSize, Integer queueType, Integer capacity, Integer keepAliveTime) {
        ThreadPoolExecutor executor = GlobalThreadPoolManage.getExecutorService(threadPoolId).getPool();
        printLog("[🔥] Original thread pool. ",
                executor.getCorePoolSize(), executor.getMaximumPoolSize(), queueType, executor.getQueue().remainingCapacity(), executor.getKeepAliveTime(TimeUnit.MILLISECONDS));

        ThreadPoolChangeHandler.changePool(executor, coreSize, maxSize, queueType, capacity, keepAliveTime);
        ThreadPoolExecutor afterExecutor = GlobalThreadPoolManage.getExecutorService(threadPoolId).getPool();

        printLog("[🚀] Changed thread pool. ",
                afterExecutor.getCorePoolSize(), afterExecutor.getMaximumPoolSize(), queueType, afterExecutor.getQueue().remainingCapacity(), afterExecutor.getKeepAliveTime(TimeUnit.MILLISECONDS));
    }

    private static void printLog(String prefixMsg, Integer coreSize, Integer maxSize, Integer queueType, Integer capacity, Long keepAliveTime) {
        log.info("{} coreSize :: {}, maxSize :: {}, queueType :: {}, capacity :: {}, keepAliveTime :: {}", prefixMsg, coreSize, maxSize, queueType, capacity, keepAliveTime);
    }
}