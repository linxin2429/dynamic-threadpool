package cn.xldeng.starter.tookit.thread;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;

/**
 * 拒绝策略
 *
 * @author dengxinlin
 * @date 2022/07/02 00:29:55
 */
@Slf4j
public class RejectedPolicies {
    /**
     * 发生拒绝事件时, 添加新任务并运行最早的任务
     *
     * @return 拒绝对策
     */
    public static RejectedExecutionHandler runsOldestTaskPolicy() {
        return (r, executor) -> {
            if (executor.isShutdown()) {
                return;
            }
            BlockingQueue<Runnable> workQueue = executor.getQueue();
            Runnable firstWrok = workQueue.poll();
            boolean newTask = workQueue.offer(r);
            if (firstWrok != null) {
                firstWrok.run();
            }
            if (!newTask) {
                executor.execute(r);
            }
        };
    }

    public static RejectedExecutionHandler syncPutQueuePolicy() {
        return (r, executor) -> {
            if (executor.isShutdown()) {
                return;
            }
            try {
                executor.getQueue().put(r);
            } catch (InterruptedException e) {
                log.error("线程池添加队列任务失败", e);
            }
        };
    }
}