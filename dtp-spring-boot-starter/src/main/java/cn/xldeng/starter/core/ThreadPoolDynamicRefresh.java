package cn.xldeng.starter.core;

import cn.xldeng.common.model.PoolParameterInfo;
import cn.xldeng.starter.tookit.thread.QueueTypeEnum;
import cn.xldeng.starter.tookit.thread.RejectedTypeEnum;
import cn.xldeng.starter.tookit.thread.ResizableCapacityLinkedBlockingQueue;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;

import java.util.Objects;
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
                keepAliveTime = parameter.getKeepAliveTime(), rejectedType = parameter.getRejectedType();
        refreshDynamicPool(tpId, coreSize, maxSize, queueType, capacity, keepAliveTime, rejectedType);
    }

    public static void refreshDynamicPool(String threadPoolId, Integer coreSize, Integer maxSize, Integer queueType, Integer capacity, Integer keepAliveTime, Integer rejectedType) {
        ThreadPoolExecutor executor = GlobalThreadPoolManage.getExecutorService(threadPoolId).getPool();
        printLog("[🔥] Original thread pool. ",
                executor.getCorePoolSize(),
                executor.getMaximumPoolSize(),
                queueType,
                (executor.getQueue().remainingCapacity() + executor.getQueue().size()),
                executor.getKeepAliveTime(TimeUnit.MILLISECONDS),
                rejectedType);

        changePoolInfo(executor, coreSize, maxSize, queueType, capacity, keepAliveTime, rejectedType);
        ThreadPoolExecutor afterExecutor = GlobalThreadPoolManage.getExecutorService(threadPoolId).getPool();

        printLog("[🚀] Changed thread pool. ",
                afterExecutor.getCorePoolSize(),
                afterExecutor.getMaximumPoolSize(),
                queueType,
                (afterExecutor.getQueue().remainingCapacity() + afterExecutor.getQueue().size()),
                afterExecutor.getKeepAliveTime(TimeUnit.MILLISECONDS),
                rejectedType);
    }

    private static void printLog(String prefixMsg, Integer coreSize, Integer maxSize, Integer queueType, Integer capacity, Long keepAliveTime, Integer rejectedType) {
        log.info("{} coreSize :: {}, maxSize :: {}, queueType :: {}, capacity :: {}, keepAliveTime :: {}, rejectedType:: {}", prefixMsg, coreSize, maxSize, queueType, capacity, keepAliveTime, rejectedType);
    }

    public static void changePoolInfo(ThreadPoolExecutor executor, Integer coreSize, Integer maxSize, Integer queueType, Integer capacity, Integer keepAliveTime, Integer rejectedType) {
        if (coreSize != null) {
            executor.setCorePoolSize(coreSize);
        }
        if (maxSize != null) {
            executor.setMaximumPoolSize(maxSize);
        }
        if (capacity != null && Objects.equals(QueueTypeEnum.RESIZABLE_LINKED_BLOCKING_QUEUE.type, queueType)) {
            if (executor.getQueue() instanceof ResizableCapacityLinkedBlockingQueue) {
                ResizableCapacityLinkedBlockingQueue queue = (ResizableCapacityLinkedBlockingQueue) executor.getQueue();
                queue.setCapacity(capacity);
            } else {
                log.warn("[Pool change] The queue length cannot be modified. Queue type mismatch.");
            }
        }
        if (keepAliveTime != null) {
            executor.setKeepAliveTime(keepAliveTime, TimeUnit.SECONDS);
        }
        if (rejectedType != null) {
            executor.setRejectedExecutionHandler(RejectedTypeEnum.createPolicy(queueType));
        }
    }
}