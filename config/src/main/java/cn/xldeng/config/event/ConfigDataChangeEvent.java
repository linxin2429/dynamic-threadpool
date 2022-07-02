package cn.xldeng.config.event;

import org.springframework.util.StringUtils;

/**
 * @program: threadpool
 * @description:
 * @author: dengxinlin
 * @create: 2022-06-29 22:56
 */
public class ConfigDataChangeEvent extends Event {
    private static final long serialVersionUID = 1207047198016465829L;
    public final String tenantId;

    public final String itemId;

    public final String tpId;

    public final long lastModifiedTs;

    public ConfigDataChangeEvent(String tenantId, String itemId, String tpId, long lastModifiedTs) {
        if (StringUtils.isEmpty(tenantId) || StringUtils.isEmpty(itemId) || StringUtils.isEmpty(tpId)) {
            throw new IllegalArgumentException("dataId is null or group is null");
        }
        this.tenantId = tenantId;
        this.itemId = itemId;
        this.tpId = tpId;
        this.lastModifiedTs = lastModifiedTs;
    }
}