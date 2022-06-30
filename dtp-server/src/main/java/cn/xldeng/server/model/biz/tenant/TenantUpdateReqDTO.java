package cn.xldeng.server.model.biz.tenant;

import lombok.Data;

/**
 * @program: threadpool
 * @description:
 * @author: dengxinlin
 * @create: 2022-06-30 16:09
 */
@Data
public class TenantUpdateReqDTO {
    /**
     * 租户 ID
     */
    private String namespaceId;

    /**
     * 租户名称
     */
    private String tenantName;

    /**
     * 租户简介
     */
    private String tenantDesc;

    /**
     * 负责人
     */
    private String owner;
}