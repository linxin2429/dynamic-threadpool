package cn.xldeng.starter.common;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @program: threadpool
 * @description: 公共线程池生产者
 * @author: dengxinlin
 * @create: 2022-06-27 07:31
 */
public class CommonThreadPool {
    public static ThreadPoolExecutor getInstance(String threadPoolId) {
        TimeUnit unit = TimeUnit.SECONDS;
        BlockingQueue<Runnable> workQueue = new LinkedBlockingDeque<>(512);
        return new ThreadPoolExecutor(
                3,
                5,
                10000,
                unit,
                workQueue,
                r -> {
                    Thread thread = new Thread(r);
                    thread.setDaemon(false);
                    thread.setName(threadPoolId);
                    return thread;
                }
        );
    }
}