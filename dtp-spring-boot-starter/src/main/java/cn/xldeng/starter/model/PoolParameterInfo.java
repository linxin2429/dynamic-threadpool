package cn.xldeng.starter.model;

import lombok.Data;

import java.io.Serializable;

/**
 * @program: threadpool
 * @description: 线程池参数
 * @author: dengxinlin
 * @create: 2022-06-27 07:34
 */
@Data
public class PoolParameterInfo implements Serializable {

    private static final long serialVersionUID = -8173622483126429123L;

    /**
     * 租户 Or 命名空间
     */
    private String namespace;

    /**
     * 项目 Id
     */
    private String itemId;

    /**
     * 线程池 Id
     */
    private String tpId;

    /**
     * 内容
     */
    private String content;

    /**
     * 核心线程数
     */
    private Integer coreSize;

    /**
     * 最大线程数
     */
    private Integer maxSize;

    /**
     * 队列类型
     */
    private Integer queueType;

    /**
     * 队列长度
     */
    private Integer capacity;

    /**
     * 线程存活时长
     */
    private Integer keepAliveTime;
}