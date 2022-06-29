package cn.xldeng.server.service;

import cn.xldeng.server.model.ConfigAllInfo;

/**
 * @program: threadpool
 * @description: 服务端配置接口
 * @author: dengxinlin
 * @create: 2022-06-27 07:28
 */
public interface ConfigService {
    /**
     * 查询配置全部信息
     *
     * @param tpId    tpId
     * @param itemId itemId
     * @param tenant  tenant
     * @return 全部配置信息
     */
    ConfigAllInfo findConfigAllInfo(String tpId, String itemId, String tenant);

    /**
     * 新增或修改
     *
     * @param configAllInfo
     */
    void insertOrUpdate(ConfigAllInfo configAllInfo);
}
