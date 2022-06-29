package cn.xldeng.server.service;

import cn.xldeng.common.config.ApplicationContextHolder;
import cn.xldeng.common.toolkit.Md5Util;
import cn.xldeng.server.constant.Constants;
import cn.xldeng.server.model.CacheItem;
import cn.xldeng.server.model.ConfigAllInfo;
import org.springframework.util.StringUtils;

import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @program: threadpool
 * @description:
 * @author: dengxinlin
 * @create: 2022-06-30 00:08
 */
public class ConfigCacheService {

    static ConfigService configService = null;

    private static final ConcurrentHashMap<String, CacheItem> CACHE = new ConcurrentHashMap<>();

    public static boolean isUpdateData(String groupKey, String md5, String ip) {
        String contentMd5 = ConfigCacheService.getContentMd5(groupKey, ip);
        return Objects.equals(contentMd5, md5);
    }

    private static String getContentMd5(String groupKey, String ip) {
        CacheItem cacheItem = CACHE.get(groupKey);
        if (cacheItem != null) {
            return cacheItem.md5;
        }

        if (configService == null) {
            configService = ApplicationContextHolder.getBean(ConfigService.class);
        }
        String[] split = groupKey.split("\\+");

        ConfigAllInfo config = configService.findConfigAllInfo(split[0], split[1], split[2]);
        if (config != null && !StringUtils.isEmpty(config.getTpId())) {
            String md5 = Md5Util.getTpContentMd5(config);
            cacheItem = new CacheItem(groupKey, md5);
            CACHE.put(groupKey, cacheItem);
        }
        return (cacheItem != null) ? cacheItem.md5 : Constants.NULL;
    }
}