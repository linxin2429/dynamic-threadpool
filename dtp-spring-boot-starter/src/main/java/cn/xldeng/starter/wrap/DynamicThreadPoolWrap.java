package cn.xldeng.starter.wrap;

import cn.xldeng.starter.common.CommonThreadPool;
import lombok.Data;

import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @program: threadpool
 * @description: 线程池包装
 * @author: dengxinlin
 * @create: 2022-06-27 07:35
 */
@Data
public class DynamicThreadPoolWrap {
    private String tenantId;

    private String itemId;

    private String tpId;

    private ThreadPoolExecutor pool;

    /**
     * 首选服务端线程池, 为空使用默认线程池 {@link cn.xldeng.starter.common.CommonThreadPool#getInstance(String)}
     *
     * @param threadPoolId threadPoolId
     */
    public DynamicThreadPoolWrap(String threadPoolId) {
        this(threadPoolId, CommonThreadPool.getInstance(threadPoolId));
    }

    /**
     * 首选服务端线程池, 为空使用 threadPoolExecutor
     *
     * @param threadPoolId       threadPoolId
     * @param threadPoolExecutor threadPoolExecutor
     */
    public DynamicThreadPoolWrap(String threadPoolId, ThreadPoolExecutor threadPoolExecutor) {
        this.tpId = threadPoolId;
        this.pool = threadPoolExecutor;
    }

    /**
     * 提交任务
     *
     * @param command command
     */
    public void execute(Runnable command) {
        pool.execute(command);
    }

    /**
     * 提交任务
     *
     * @param task task
     * @return Future
     */
    public Future<?> submit(Runnable task) {
        return pool.submit(task);
    }

    /**
     * 提交任务
     *
     * @param task task
     * @return Future
     */
    public <T> Future<T> submit(Callable<T> task) {
        return pool.submit(task);
    }

}