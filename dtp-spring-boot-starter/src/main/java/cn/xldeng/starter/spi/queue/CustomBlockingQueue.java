package cn.xldeng.starter.spi.queue;

import java.util.concurrent.BlockingQueue;

/**
 * 定制阻塞队列
 *
 * @author dengxinlin
 * @date 2022/07/04 09:55:13
 */
public interface CustomBlockingQueue {

    /**
     * 获取类型
     *
     * @return {@link Integer}
     */
    Integer getType();


    /**
     * 产生阻塞队列
     *
     * @return {@link BlockingQueue}<{@link Runnable}>
     */
    BlockingQueue<Runnable> generateBlockingQueue();
}