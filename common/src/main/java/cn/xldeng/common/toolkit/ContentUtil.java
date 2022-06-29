package cn.xldeng.common.toolkit;

import cn.xldeng.common.model.PoolParameter;

/**
 * @program: threadpool
 * @description:
 * @author: dengxinlin
 * @create: 2022-06-29 21:46
 */
public class ContentUtil {

    public static String getPoolContent(PoolParameter parameter){
        return String.valueOf(parameter.getCoreSize()) +
                parameter.getMaxSize() +
                parameter.getQueueType() +
                parameter.getCapacity() +
                parameter.getKeepAliveTime() +
                parameter.getIsAlarm() +
                parameter.getCapacityAlarm() +
                parameter.getLivenessAlarm();
    }
}