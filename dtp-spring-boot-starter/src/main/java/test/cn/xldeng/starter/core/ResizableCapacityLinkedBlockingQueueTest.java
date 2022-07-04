package test.cn.xldeng.starter.core;

import cn.xldeng.starter.tookit.thread.ResizableCapacityLinkedBlockingQueue;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * ResizableCapacityLinkedBlockingQueue Tester.
 *
 * @author <Authors name>
 * @version 1.0
 * @since <pre>6�� 27, 2022</pre>
 */
@Slf4j
public class ResizableCapacityLinkedBlockingQueueTest {
    public static void main(String[] args) throws InterruptedException {
        ResizableCapacityLinkedBlockingQueue blockingQueue = new ResizableCapacityLinkedBlockingQueue(5);
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(1,
                3,
                1024,
                TimeUnit.SECONDS,
                blockingQueue);
        Runnable runnable = new Runnable() {
            @Override
            @SneakyThrows
            public void run() {
                Thread.sleep(10000);
            }
        };
        for (int i = 0; i <4; i++) {
            threadPoolExecutor.execute(runnable);
        }
        print(threadPoolExecutor);
        Thread.sleep(1000);
        blockingQueue.setCapacity(1000);
        print(threadPoolExecutor);
    }

    private static void print(ThreadPoolExecutor executor) {
        LinkedBlockingQueue queue = (LinkedBlockingQueue) executor.getQueue();

        log.info("核心线程数 :: {}," +
                        " 活动线程数 :: {}," +
                        " 最大线程数 :: {}," +
                        " 线程池活跃度 :: {}," +
                        " 任务完成数 :: {}," +
                        " 队列大小 :: {}," +
                        " 当前排队线程数 :: {}," +
                        " 队列剩余大小 :: {}," +
                        " 队列使用度 :: {}",
                executor.getCorePoolSize(),
                executor.getActiveCount(),
                executor.getMaximumPoolSize(),
                divide(executor.getActiveCount(), executor.getMaximumPoolSize()),
                executor.getCompletedTaskCount(),
                (queue.size() + queue.remainingCapacity()),
                queue.size(),
                queue.remainingCapacity(),
                divide(queue.size(), queue.size() + queue.remainingCapacity()));

    }

    private static String divide(int num1, int num2) {
        return String.format("%1.2f%%", Double.parseDouble(num1 + "") / Double.parseDouble(num2 + "") * 100);
    }


} 
