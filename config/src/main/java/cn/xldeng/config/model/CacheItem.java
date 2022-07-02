package cn.xldeng.config.model;

import cn.xldeng.common.constant.Constants;
import cn.xldeng.config.toolkit.SimpleReadWriteLock;
import cn.xldeng.config.toolkit.SingletonRepository;
import lombok.Getter;
import lombok.Setter;

/**
 * @program: threadpool
 * @description:
 * @author: dengxinlin
 * @create: 2022-06-29 22:59
 */
@Getter
@Setter
public class CacheItem {

    final String groupKey;

    public volatile String md5 = Constants.NULL;

    public volatile long lastModifiedTs;

    public SimpleReadWriteLock rwLock = new SimpleReadWriteLock();

    public CacheItem(String groupKey) {
        this.groupKey = SingletonRepository.DataIdGroupIdCache.getSingleton(groupKey);
    }

    public CacheItem(String groupKey, String md5) {
        this.md5 = md5;
        this.groupKey = SingletonRepository.DataIdGroupIdCache.getSingleton(groupKey);
    }
}