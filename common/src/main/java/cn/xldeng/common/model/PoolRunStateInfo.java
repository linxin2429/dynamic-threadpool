package cn.xldeng.common.model;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * 线程池运行状态信息
 *
 * @author dengxinlin
 * @date 2022/07/03 07:17:27
 */
@Getter
@Setter
public class PoolRunStateInfo implements Serializable {

    private static final long serialVersionUID = 3918314588763340469L;

    /**
     * 当前负载
     */
    private String currentLoad;

    /**
     * 峰值负载
     */
    private String peakLoad;

    /**
     * 线程池 ID
     */
    private String tpId;

    /**
     * 核心线程数
     */
    private Integer coreSize;

    /**
     * 最大线程数
     */
    private Integer maximumSize;

    /**
     * 线程池当前线程数
     */
    private Integer poolSize;

    /**
     * 活跃线程数
     */
    private Integer activeSize;

    /**
     * 线程池中同时进入的最大线程数
     */
    private Integer largestPoolSize;

    /**
     * 队列类型
     */
    private String queueType;

    /**
     * 队列容量
     */
    private Integer queueCapacity;

    /**
     * 队列元素个数
     */
    private Integer queueSize;

    /**
     * 队列剩余容量
     */
    private Integer queueRemainingCapacity;

    /**
     * 线程池中执行任务总数量
     */
    private Long completedTaskCount;

    /**
     * 拒绝策略发生次数
     */
    private Integer rejectCount;

    /**
     * Host
     */
    private String host;
}