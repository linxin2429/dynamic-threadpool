package cn.xldeng.starter.tookit.thread;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * 快速执行任务而阻塞队列, 参考 Dubbo 重写队列 TaskQueue
 *
 * @author dengxinlin
 * @date 2022/07/01 23:18:54
 */
public class TaskQueue<R extends Runnable> extends LinkedBlockingQueue<Runnable> {

    private static final long serialVersionUID = 9165131452511747592L;

    private FastThreadPoolExecutor executor;

    public TaskQueue(int capacity) {
        super(capacity);
    }

    public void setExecutor(FastThreadPoolExecutor exec) {
        executor = exec;
    }

    @Override
    public boolean offer(Runnable runnable) {
        int currentPoolThreadSize = executor.getPoolSize();

        // 如果有核心线程正在空闲, 将任务加入阻塞队列, 由核心线程进行处理任务
        if (executor.getSubmittedTaskCount() < currentPoolThreadSize) {
            return super.offer(runnable);
        }

        // 当前线程池线程数量小于最大线程数, 返回false, 根据线程池源码, 会创建非核心线程
        if (currentPoolThreadSize < executor.getMaximumPoolSize()) {
            return false;
        }

        // 如果当前线程池数量大于最大线程数, 任务加入阻塞队列
        return super.offer(runnable);
    }

    public boolean retryOffer(Runnable o, long timeout, TimeUnit unit) throws InterruptedException {
        if (executor.isShutdown()) {
            throw new RejectedExecutionException("执行器已关闭！");
        }
        return super.offer(o, timeout, unit);
    }
}