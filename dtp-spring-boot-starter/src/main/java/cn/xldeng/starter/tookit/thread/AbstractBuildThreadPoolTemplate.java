package cn.xldeng.starter.tookit.thread;


import cn.xldeng.common.toolkit.Assert;
import cn.xldeng.starter.wrap.CustomThreadPoolExecutor;
import lombok.Data;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.*;
import java.util.concurrent.locks.AbstractQueuedSynchronizer;

/**
 * 抽象构建线程池模板
 *
 * @author dengxinlin
 * @date 2022/07/01
 */
@Slf4j
public class AbstractBuildThreadPoolTemplate {

    /**
     * 线程池构建初始化参数
     * <p>
     * 此处本身是模版设计方法, 但是考虑创建简洁性, 移除 abstract
     * 异常参考 {@link AbstractQueuedSynchronizer#tryAcquire}
     *
     * @return {@link ThreadPoolInitParam}
     */
    protected static ThreadPoolInitParam initParam() {
        throw new UnsupportedOperationException();
    }

    public static ThreadPoolExecutor buildPool() {
        ThreadPoolInitParam initParam = initParam();
        return buildPool(initParam);
    }

    /**
     * 构建线程池
     *
     * @param initParam 初始化参数
     * @return {@link ThreadPoolExecutor}
     */
    public static ThreadPoolExecutor buildPool(ThreadPoolInitParam initParam) {
        Assert.notNull(initParam);
        return new ThreadPoolExecutorTemplate(initParam.getCorePoolNum(),
                initParam.getMaxPoolNum(),
                initParam.getKeepAliveTime(),
                initParam.getTimeUnit(),
                initParam.getWorkQueue(),
                initParam.getThreadFactory(),
                initParam.getRejectedExecutionHandler());
    }

    public static ThreadPoolExecutor buildFastPool() {
        ThreadPoolInitParam initParam = initParam();
        return buildFastPool(initParam);
    }

    public static ThreadPoolExecutor buildFastPool(ThreadPoolInitParam initParam) {
        TaskQueue<Runnable> taskQueue = new TaskQueue<>(initParam.capacity);
        FastThreadPoolExecutor fastThreadPoolExecutor =
                new FastThreadPoolExecutor(initParam.getCorePoolNum(),
                        initParam.getMaxPoolNum(),
                        initParam.getKeepAliveTime(),
                        initParam.getTimeUnit(),
                        taskQueue,
                        initParam.getThreadFactory(),
                        initParam.getRejectedExecutionHandler());
        taskQueue.setExecutor(fastThreadPoolExecutor);
        return fastThreadPoolExecutor;
    }

    public static CustomThreadPoolExecutor buildCustomPool(ThreadPoolInitParam initParam) {
        Assert.notNull(initParam);
        return new CustomThreadPoolExecutor(
                initParam.getCorePoolNum(),
                initParam.getMaxPoolNum(),
                initParam.getKeepAliveTime(),
                initParam.getTimeUnit(),
                initParam.getWorkQueue(),
                initParam.getThreadFactory(),
                initParam.getRejectedExecutionHandler()
        );
    }

    @Data
    @Accessors(chain = true)
    public static class ThreadPoolInitParam {
        /**
         * 核心线程数量
         */
        private Integer corePoolNum;

        /**
         * 最大线程数量
         */
        private Integer maxPoolNum;

        /**
         * 线程存活时间
         */
        private Long keepAliveTime;

        /**
         * 线程存活时间单位
         */
        private TimeUnit timeUnit;

        /**
         * 队列最大容量
         */
        private Integer capacity;

        /**
         * 阻塞队列
         */
        private BlockingQueue<Runnable> workQueue;

        /**
         * 线程池任务满时拒绝任务策略
         */
        private RejectedExecutionHandler rejectedExecutionHandler;

        /**
         * 创建线程工厂
         */
        private ThreadFactory threadFactory;

        public ThreadPoolInitParam(String threadNamePrefix, boolean isDaemon) {
            this.threadFactory = new ThreadFactoryBuilder()
                    .prefix(threadNamePrefix + "-")
                    .daemon(isDaemon)
                    .build();
        }
    }
}