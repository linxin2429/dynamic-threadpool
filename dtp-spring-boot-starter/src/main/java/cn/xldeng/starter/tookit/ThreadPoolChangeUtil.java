package cn.xldeng.starter.tookit;

import cn.xldeng.common.enums.QueueTypeEnum;
import cn.xldeng.starter.core.ResizableCapacityLinkedBlockingQueue;

import java.util.Objects;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @program: threadpool
 * @description:
 * @author: dengxinlin
 * @create: 2022-06-30 15:32
 */
public class ThreadPoolChangeUtil {
    public static void changePool(ThreadPoolExecutor executor,Integer coreSize,Integer maxSize,Integer queueType,Integer capacity,Integer keepAliveTime){
        if (coreSize != null) {
            executor.setCorePoolSize(coreSize);
        }
        if (maxSize != null) {
            executor.setMaximumPoolSize(maxSize);
        }
        if (capacity != null && Objects.equals(QueueTypeEnum.Resizable_LINKED_Blocking_QUEUE.type, queueType)) {
            ResizableCapacityLinkedBlockingQueue queue = (ResizableCapacityLinkedBlockingQueue) executor.getQueue();
            queue.setCapacity(capacity);
        }
        if (keepAliveTime != null) {
            executor.setKeepAliveTime(keepAliveTime, TimeUnit.SECONDS);
        }
    }
}