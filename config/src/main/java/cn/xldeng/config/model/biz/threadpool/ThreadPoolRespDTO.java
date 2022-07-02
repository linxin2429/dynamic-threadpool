package cn.xldeng.config.model.biz.threadpool;

import lombok.Data;

import java.util.Date;

/**
 * @program: threadpool
 * @description:
 * @author: dengxinlin
 * @create: 2022-06-30 17:11
 */
@Data
public class ThreadPoolRespDTO {
    /**
     * 命名空间
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

    /**
     * 是否告警
     */
    private Integer isAlarm;

    /**
     * 容量告警
     */
    private Integer capacityAlarm;

    /**
     * 活跃度告警
     */
    private Integer livenessAlarm;

    /**
     * 创建时间
     */
    private Date gmtCreate;

    /**
     * 修改时间
     */
    private Date gmtModified;
}