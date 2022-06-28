package cn.xldeng.server.toolkit;

import cn.hutool.crypto.digest.DigestUtil;
import cn.xldeng.server.model.ConfigAllInfo;

/**
 * @program: threadpool
 * @description:
 * @author: dengxinlin
 * @create: 2022-06-28 22:25
 */
public class Md5ConfigUtil {
    public String getTpContentMd5(ConfigAllInfo config) {
        String targetStr = String.valueOf(config.getCoreSize()) +
                config.getMaxSize() +
                config.getQueueType() +
                config.getCapacity() +
                config.getKeepAliveTime() +
                config.getIsAlarm() +
                config.getCapacityAlarm() +
                config.getLivenessAlarm();
        return DigestUtil.md5Hex(targetStr);
    }
}