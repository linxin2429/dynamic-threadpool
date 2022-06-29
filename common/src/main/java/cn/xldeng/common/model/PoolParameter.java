package cn.xldeng.common.model;

/**
 * @program: threadpool
 * @description:
 * @author: dengxinlin
 * @create: 2022-06-29 21:44
 */
public interface PoolParameter {
    String getNamespace();

    String getItemId();

    String getTpId();

    Integer getCoreSize();

    Integer getMaxSize();

    Integer getQueueType();

    Integer getCapacity();

    Integer getKeepAliveTime();

    Integer getIsAlarm();

    Integer getCapacityAlarm();

    Integer getLivenessAlarm();
}