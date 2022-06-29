package cn.xldeng.common.model;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * @program: threadpool
 * @description:
 * @author: dengxinlin
 * @create: 2022-06-29 11:53
 */
@Getter
@Setter
public class GlobalRemotePoolInfo implements Serializable {

    private static final long serialVersionUID = -8602569354742773818L;

    /**
     * 命名空间
     */
    private String tenantId;

    /**
     * 项目 ID
     */
    private String itemId;

    /**
     * 线程池标识
     */
    private String tpId;

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
     * MD5
     */
    private String md5;

    /**
     * 内容
     */
    private String content;
}