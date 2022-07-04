package cn.xldeng.common.toolkit;

import cn.xldeng.common.constant.Constants;
import cn.xldeng.common.model.PoolParameter;
import cn.xldeng.common.model.PoolParameterInfo;
import com.alibaba.fastjson.JSON;

/**
 * @program: threadpool
 * @description:
 * @author: dengxinlin
 * @create: 2022-06-29 21:46
 */
public class ContentUtil {

    public static String getPoolContent(PoolParameter parameter){
        PoolParameterInfo poolInfo = new PoolParameterInfo();
        poolInfo.setTenantId(parameter.getTenantId());
        poolInfo.setItemId(parameter.getItemId());
        poolInfo.setTpId(parameter.getTpId());
        poolInfo.setCoreSize(parameter.getCoreSize());
        poolInfo.setMaxSize(parameter.getMaxSize());
        poolInfo.setQueueType(parameter.getQueueType());
        poolInfo.setCapacity(parameter.getCapacity());
        poolInfo.setKeepAliveTime(parameter.getKeepAliveTime());
        poolInfo.setIsAlarm(parameter.getIsAlarm());
        poolInfo.setCapacityAlarm(parameter.getCapacityAlarm());
        poolInfo.setLivenessAlarm(parameter.getLivenessAlarm());
        poolInfo.setRejectedType(parameter.getRejectedType());
        return JSON.toJSONString(poolInfo);
    }

    public static String getGroupKey(PoolParameter parameter) {
        return parameter.getTpId() +
                Constants.GROUP_KEY_DELIMITER +
                parameter.getItemId() +
                Constants.GROUP_KEY_DELIMITER +
                parameter.getTenantId();
    }
}