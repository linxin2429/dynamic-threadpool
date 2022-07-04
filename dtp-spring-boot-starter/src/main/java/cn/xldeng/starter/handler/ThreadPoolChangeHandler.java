package cn.xldeng.starter.handler;

import cn.xldeng.starter.core.ResizableCapacityLinkedBlockingQueue;
import cn.xldeng.starter.tookit.thread.QueueTypeEnum;
import lombok.extern.slf4j.Slf4j;

import java.util.Objects;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @program: threadpool
 * @description:
 * @author: dengxinlin
 * @create: 2022-06-30 15:32
 */
@Slf4j
public class ThreadPoolChangeHandler {

    public static void changePool(ThreadPoolExecutor executor, Integer coreSize, Integer maxSize, Integer queueType, Integer capacity, Integer keepAliveTime) {
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
            }else {
                log.warn("[Pool change] The queue length cannot be modified. Queue type mismatch.");
            }
        }
        if (keepAliveTime != null) {
            executor.setKeepAliveTime(keepAliveTime, TimeUnit.SECONDS);
        }
    }
}