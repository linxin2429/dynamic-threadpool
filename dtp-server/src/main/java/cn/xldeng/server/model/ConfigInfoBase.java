package cn.xldeng.server.model;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import java.io.Serializable;

/**
 * @program: threadpool
 * @description: 基础配置信息
 * @author: dengxinlin
 * @create: 2022-06-27 07:27
 */
@Data
public class ConfigInfoBase implements Serializable {
    private static final long serialVersionUID = 2443779416309879749L;

    /**
     * ID
     */
    private Integer id;
    /**
     * namespace
     */
    @TableField(value = "tenant_id")
    private String namespace;
    /**
     * TpId
     */
    private String tpId;

    /**
     * ItemId
     */
    private String itemId;

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
    @JSONField(serialize = false)
    private String md5;

    /**
     * 内容
     */
    @JSONField(serialize = false)
    private String content;
}