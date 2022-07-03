package cn.xldeng.starter.tookit.thread;


import cn.xldeng.common.enums.QueueTypeEnum;
import cn.xldeng.common.toolkit.Assert;
import cn.xldeng.starter.builder.Builder;
import cn.xldeng.starter.tookit.BlockingQueueUtil;

import java.math.BigDecimal;
import java.util.concurrent.*;

/**
 * 线程池构建器
 *
 * @author dengxinlin
 * @date 2022/07/01 09:17:23
 */
public class ThreadPoolBuilder implements Builder<ThreadPoolExecutor> {

    private static final long serialVersionUID = 8984202136630808855L;
    /**
     * 是否创建快速消费线程池
     */
    private boolean isFastPool;

    /**
     * 是否自定义线程池
     */
    private boolean isCustomPool;

    /**
     * 核心线程数量
     */
    private int corePoolSize = calculateCoreNum();

    /**
     * 最大线程数量
     */
    private int maxPoolSize = corePoolSize + (corePoolSize >> 1);

    /**
     * 线程存活时间
     */
    private long keepAliveTime = 30000L;

    /**
     * 线程存活时间单位
     */
    private TimeUnit timeUnit = TimeUnit.MILLISECONDS;

    /**
     * 队列最大容量
     */
    private int capacity = 512;

    /**
     * 队列类型枚举
     */
    private QueueTypeEnum queueType;

    /**
     * 阻塞队列
     */
    private BlockingQueue<Runnable> workQueue = new LinkedBlockingQueue<>(capacity);

    /**
     * 线程池任务满时拒绝任务策略
     */
    private RejectedExecutionHandler rejectedExecutionHandler = new ThreadPoolExecutor.AbortPolicy();

    /**
     * 是否守护线程
     */
    private boolean isDaemon = false;

    /**
     * 线程名称前缀
     */
    private String threadNamePrefix;

    /**
     * 线程池核心线程数
     * 计算公式：CPU 核数 / (1 - 阻塞系数 0.8)
     *
     * @return {@link Integer}
     */
    private Integer calculateCoreNum() {
        int cpuCoreNum = Runtime.getRuntime().availableProcessors();
        return new BigDecimal(cpuCoreNum).divide(new BigDecimal("0.2")).intValue();
    }

    public ThreadPoolBuilder isFastPool(Boolean isFastPool) {
        this.isFastPool = isFastPool;
        return this;
    }

    public ThreadPoolBuilder isCustomPool(Boolean isCustomPool) {
        this.isCustomPool = isCustomPool;
        return this;
    }

    public ThreadPoolBuilder threadFactory(String threadNamePrefix) {
        this.threadNamePrefix = threadNamePrefix;
        return this;
    }

    public ThreadPoolBuilder threadFactory(String threadNamePrefix, Boolean isDaemon) {
        this.threadNamePrefix = threadNamePrefix;
        this.isDaemon = isDaemon;
        return this;
    }

    public ThreadPoolBuilder corePoolSize(int corePoolNum) {
        this.corePoolSize = corePoolNum;
        return this;
    }

    public ThreadPoolBuilder maxPoolSize(int maxPoolSize) {
        this.maxPoolSize = maxPoolSize;
        return this;
    }

    public ThreadPoolBuilder poolThreadSize(int corePoolSize, int maxPoolSize) {
        this.corePoolSize = corePoolSize;
        this.maxPoolSize = maxPoolSize;
        return this;
    }

    public ThreadPoolBuilder keepAliveTime(long keepAliveTime) {
        this.keepAliveTime = keepAliveTime;
        return this;
    }

    public ThreadPoolBuilder timeUnit(TimeUnit timeUnit) {
        this.timeUnit = timeUnit;
        return this;
    }

    public ThreadPoolBuilder keepAliveTime(long keepAliveTime, TimeUnit timeUnit) {
        this.keepAliveTime = keepAliveTime;
        this.timeUnit = timeUnit;
        return this;
    }

    public ThreadPoolBuilder capacity(int capacity) {
        this.capacity = capacity;
        return this;
    }

    public ThreadPoolBuilder workQueue(QueueTypeEnum queueType, int capacity) {
        this.queueType = queueType;
        this.capacity = capacity;
        return this;
    }

    public ThreadPoolBuilder rejected(RejectedExecutionHandler rejectedExecutionHandler) {
        this.rejectedExecutionHandler = rejectedExecutionHandler;
        return this;
    }

    /**
     * 工作队列
     * 使用此方式赋值 workQueue, capacity 失效
     *
     * @param queueType 队列类型
     * @return {@link ThreadPoolBuilder}
     */
    public ThreadPoolBuilder workQueue(QueueTypeEnum queueType) {
        this.queueType = queueType;
        return this;
    }

    public ThreadPoolBuilder workQueue(BlockingQueue workQueue) {
        this.workQueue = workQueue;
        return this;
    }

    @Override
    public ThreadPoolExecutor build() {
        if (isCustomPool) {
            return buildCustomPool();
        }
        return isFastPool ? buildFastPool(this) : buildPool(this);
    }

    private ThreadPoolExecutor buildCustomPool() {
        return AbstractBuildThreadPoolTemplate.buildCustomPool(buildInitParam(builder()));
    }

    /**
     * 构建器
     *
     * @return {@link ThreadPoolBuilder}
     */
    public static ThreadPoolBuilder builder() {
        return new ThreadPoolBuilder();
    }

    private static ThreadPoolExecutor buildPool(ThreadPoolBuilder builder) {
        return AbstractBuildThreadPoolTemplate.buildPool(buildInitParam(builder));
    }

    private static AbstractBuildThreadPoolTemplate.ThreadPoolInitParam buildInitParam(ThreadPoolBuilder builder) {
        Assert.notEmpty(builder.threadNamePrefix, "线程名称前缀不可为空或空的字符串.");
        AbstractBuildThreadPoolTemplate.ThreadPoolInitParam initParam =
                new AbstractBuildThreadPoolTemplate.ThreadPoolInitParam(builder.threadNamePrefix, builder.isDaemon);
        initParam.setCorePoolNum(builder.corePoolSize)
                .setMaxPoolNum(builder.maxPoolSize)
                .setKeepAliveTime(builder.keepAliveTime)
                .setCapacity(builder.capacity)
                .setRejectedExecutionHandler(builder.rejectedExecutionHandler)
                .setTimeUnit(builder.timeUnit);
        if (!builder.isFastPool) {
            if (builder.workQueue == null) {
                builder.workQueue = BlockingQueueUtil.createBlockingQueue(builder.queueType.type, builder.capacity);
            }
            initParam.setWorkQueue(builder.workQueue);
        }
        return initParam;
    }

    private static ThreadPoolExecutor buildFastPool(ThreadPoolBuilder builder) {
        return AbstractBuildThreadPoolTemplate.buildFastPool(buildInitParam(builder));
    }
}