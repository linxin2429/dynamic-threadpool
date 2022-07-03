package cn.xldeng.starter.common;

import cn.xldeng.common.enums.QueueTypeEnum;
import cn.xldeng.starter.tookit.thread.RejectedPolicies;
import cn.xldeng.starter.tookit.thread.ThreadPoolBuilder;

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
        return ThreadPoolBuilder.builder()
                .isCustomPool(true)
                .threadFactory(threadPoolId)
                .poolThreadSize(3, 5)
                .keepAliveTime(10000L, TimeUnit.SECONDS)
                .rejected(RejectedPolicies.runsOldestTaskPolicy())
                .workQueue(QueueTypeEnum.RESIZABLE_LINKED_BLOCKING_QUEUE, 512)
                .build();
    }
}