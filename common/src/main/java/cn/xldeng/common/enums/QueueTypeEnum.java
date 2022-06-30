package cn.xldeng.common.enums;

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
    Linked_Blocking_QUEUE(2),

    /**
     * {@link java.util.concurrent.LinkedBlockingDeque}
     */
    Linked_Blocking_Deque(3),

    /**
     * {@link java.util.concurrent.SynchronousQueue}
     */
    SynchronousQueue(4),

    /**
     * {@link java.util.concurrent.LinkedTransferQueue}
     */
    LINKED_TRANSFER_QUEUE(5),

    /**
     * {@link java.util.concurrent.PriorityBlockingQueue}
     */
    PriorityBlockingQueue(6),

    /**
     * {@link "io.dynamic.threadpool.starter.core.ResizableCapacityLinkedBlockIngQueue"}
     */
    Resizable_LINKED_Blocking_QUEUE(9);
    public Integer type;

    QueueTypeEnum(Integer type) {
        this.type = type;
    }
}