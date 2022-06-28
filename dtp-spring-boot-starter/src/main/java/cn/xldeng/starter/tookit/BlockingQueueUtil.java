package cn.xldeng.starter.tookit;

import cn.xldeng.starter.core.ResizableCapacityLinkedBlockingQueue;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;

/**
 * @program: threadpool
 * @description:
 * @author: dengxinlin
 * @create: 2022-06-27 07:35
 */
public class BlockingQueueUtil {

    public static BlockingQueue<Runnable> createBlockingQueue(Integer type, Integer capacity) {
        BlockingQueue<Runnable> blockingQueue = null;
        switch (type) {
            case 1:
                blockingQueue = new ArrayBlockingQueue<>(capacity);
                break;
            case 2:
                blockingQueue = new LinkedBlockingDeque<>(capacity);
                break;
            case 3:
                blockingQueue = new ResizableCapacityLinkedBlockingQueue(capacity);
                break;
            default:
                break;
        }
        return blockingQueue;
    }
}