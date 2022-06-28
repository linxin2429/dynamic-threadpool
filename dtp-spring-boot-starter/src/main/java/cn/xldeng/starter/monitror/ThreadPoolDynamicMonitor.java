package cn.xldeng.starter.monitror;

import cn.xldeng.starter.core.GlobalThreadPoolManage;
import cn.xldeng.starter.core.ResizableCapacityLinkedBlockingQueue;
import cn.xldeng.starter.wrap.DynamicThreadPoolWrap;

import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @program: threadpool
 * @description: 线程池动态监听
 * @author: dengxinlin
 * @create: 2022-06-27 07:34
 */
public class ThreadPoolDynamicMonitor {

    public void dynamicPool(String threadPoolName, Integer coreSize,Integer maxSize, Integer capacity, Long keepAliveTime) {
        DynamicThreadPoolWrap wrap = GlobalThreadPoolManage.getExecutorService(threadPoolName);
        ThreadPoolExecutor executor = wrap.getPool();
        if (coreSize != null){
            executor.setCorePoolSize(coreSize);
        }
        if (maxSize != null){
            executor.setMaximumPoolSize(maxSize);
        }
        if (capacity != null){
            ResizableCapacityLinkedBlockingQueue queue = (ResizableCapacityLinkedBlockingQueue) executor.getQueue();
            queue.setCapacity(capacity);
        }
        if (keepAliveTime != null){
            executor.setKeepAliveTime(keepAliveTime, TimeUnit.SECONDS);
        }

    }
}