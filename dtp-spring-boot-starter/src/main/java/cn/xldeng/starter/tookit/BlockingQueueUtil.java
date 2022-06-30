package cn.xldeng.starter.tookit;

import cn.xldeng.common.enums.QueueTypeEnum;
import cn.xldeng.starter.core.ResizableCapacityLinkedBlockingQueue;

import java.util.Objects;
import java.util.concurrent.*;

/**
 * @program: threadpool
 * @description:
 * @author: dengxinlin
 * @create: 2022-06-27 07:35
 */
public class BlockingQueueUtil {

    public static BlockingQueue<Runnable> createBlockingQueue(Integer type, Integer capacity) {
        BlockingQueue<Runnable> blockingQueue = null;
        if (Objects.equals(type, QueueTypeEnum.ARRAY_BLOCKING_QUEUE.type)) {
            blockingQueue = new ArrayBlockingQueue<>(capacity);
        } else if (Objects.equals(type, QueueTypeEnum.LINKED_BLOCKING_QUEUE.type)) {
            blockingQueue = new LinkedBlockingQueue<>(capacity);
        } else if (Objects.equals(type, QueueTypeEnum.LINKED_BLOCKING_DEQUE.type)) {
            blockingQueue = new LinkedBlockingDeque<>(capacity);
        } else if (Objects.equals(type, QueueTypeEnum.SYNCHRONOUS_QUEUE.type)) {
            blockingQueue = new SynchronousQueue<>();
        } else if (Objects.equals(type, QueueTypeEnum.LINKED_TRANSFER_QUEUE.type)) {
            blockingQueue = new LinkedTransferQueue<>();
        } else if (Objects.equals(type, QueueTypeEnum.PRIORITY_BLOCKING_QUEUE.type)) {
            blockingQueue = new PriorityBlockingQueue<>(capacity);
        } else if (Objects.equals(type, QueueTypeEnum.RESIZABLE_LINKED_BLOCKING_QUEUE.type)) {
            blockingQueue = new ResizableCapacityLinkedBlockingQueue(capacity);
        }else {
            throw new IllegalArgumentException("未找到类型匹配的阻塞队列.");
        }
        return blockingQueue;
    }
}