package cn.xldeng.starter.tookit.thread;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 快速执行任务线程池, 参考 Dubbo 线程模型 EagerThreadPoolExecutor
 *
 * @author dengxinlin
 * @date 2022/07/01 22:05:58
 */
@Slf4j
public class FastThreadPoolExecutor extends ThreadPoolExecutorTemplate {
    public FastThreadPoolExecutor(int corePoolSize,
                                  int maximumPoolSize,
                                  long keepAliveTime,
                                  TimeUnit unit,
                                  BlockingQueue<Runnable> workQueue,
                                  ThreadFactory threadFactory,
                                  RejectedExecutionHandler handler) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, threadFactory, handler);
    }

    private final AtomicInteger submittedTaskCount = new AtomicInteger(0);

    public int getSubmittedTaskCount() {
        return submittedTaskCount.get();
    }

    @Override
    protected void afterExecute(Runnable r, Throwable t) {
        submittedTaskCount.decrementAndGet();
    }

    @Override
    public void execute(Runnable command) {
        submittedTaskCount.incrementAndGet();
        try {
            super.execute(command);
        } catch (RejectedExecutionException ex) {
            final TaskQueue<Runnable> queue = (TaskQueue<Runnable>) super.getQueue();
            try {
                if (!queue.retryOffer(command, 0, TimeUnit.MILLISECONDS)) {
                    submittedTaskCount.decrementAndGet();
                    throw new RejectedExecutionException("队列容量已满足。", ex);
                }
            } catch (InterruptedException e) {
                submittedTaskCount.decrementAndGet();
                throw new RejectedExecutionException(e);
            }
        } catch (Exception t) {
            submittedTaskCount.decrementAndGet();
            throw t;
        }
    }
}