package cn.xldeng.starter.wrap;

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
    private String tenant;

    private String itemId;

    private String tpId;

    private ThreadPoolExecutor pool;

    /**
     * 首选服务端线程池, 为空使用默认线程池 {@link cn.xldeng.starter.common.CommonThreadPool#getInstance(String)}
     *
     * @param threadPoolId
     */
    public DynamicThreadPoolWrap(String tenant, String itemId, String threadPoolId) {
        this(tenant, itemId, threadPoolId, null);
    }

    /**
     * 首选服务端线程池, 为空使用 threadPoolExecutor
     *
     * @param threadPoolId
     * @param threadPoolExecutor
     */
    public DynamicThreadPoolWrap(String tenant, String itemId, String threadPoolId, ThreadPoolExecutor threadPoolExecutor) {
        this.tenant = tenant;
        this.itemId = itemId;
        this.tpId = threadPoolId;
        this.pool = threadPoolExecutor;
    }

    /**
     * 提交任务
     *
     * @param command
     */
    public void execute(Runnable command) {
        pool.execute(command);
    }

    /**
     * 提交任务
     *
     * @param task
     * @return
     */
    public Future<?> submit(Runnable task) {
        return pool.submit(task);
    }

    /**
     * 提交任务
     *
     * @param task
     * @return
     */
    public <T> Future<T> submit(Callable<T> task) {
        return pool.submit(task);
    }

}