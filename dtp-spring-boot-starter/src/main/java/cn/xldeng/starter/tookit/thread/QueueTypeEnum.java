package cn.xldeng.starter.tookit.thread;

import cn.xldeng.starter.core.ResizableCapacityLinkedBlockingQueue;
import cn.xldeng.starter.spi.DynamicTpServiceLoader;
import cn.xldeng.starter.spi.queue.CustomBlockingQueue;

import java.util.Collection;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.*;

/**
 * @program: threadpool
 * @description:
 * @author: dengxinlin
 * @create: 2022-06-30 15:12
 */
public enum QueueTypeEnum {
    /**
     * {@link java.util.concurrent.ArrayBlockingQueue}
     */
    ARRAY_BLOCKING_QUEUE(1),

    /**
     * {@link java.util.concurrent.LinkedBlockingQueue}
     */
    LINKED_BLOCKING_QUEUE(2),

    /**
     * {@link java.util.concurrent.LinkedBlockingDeque}
     */
    LINKED_BLOCKING_DEQUE(3),

    /**
     * {@link java.util.concurrent.SynchronousQueue}
     */
    SYNCHRONOUS_QUEUE(4),

    /**
     * {@link java.util.concurrent.LinkedTransferQueue}
     */
    LINKED_TRANSFER_QUEUE(5),

    /**
     * {@link java.util.concurrent.PriorityBlockingQueue}
     */
    PRIORITY_BLOCKING_QUEUE(6),

    /**
     * {@link "io.dynamic.threadpool.starter.core.ResizableCapacityLinkedBlockIngQueue"}
     */
    RESIZABLE_LINKED_BLOCKING_QUEUE(9);
    public Integer type;

    QueueTypeEnum(Integer type) {
        this.type = type;
    }

    static {
        DynamicTpServiceLoader.register(CustomBlockingQueue.class);
    }

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
        } else {
            throw new IllegalArgumentException("未找到类型匹配的阻塞队列.");
        }

        Collection<CustomBlockingQueue> customBlockingQueues = DynamicTpServiceLoader.getSingeltonServiceInstances(CustomBlockingQueue.class);
        return Optional.ofNullable(blockingQueue).orElseGet(() -> customBlockingQueues.stream()
                .filter(each -> Objects.equals(type, each.getType()))
                .map(CustomBlockingQueue::generateBlockingQueue)
                .findFirst()
                .orElse(new LinkedBlockingQueue<>(capacity)));
    }
}