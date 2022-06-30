package cn.xldeng.server.model.biz.tenant;

import lombok.Data;

import java.util.Date;

/**
 * @program: threadpool
 * @description:
 * @author: dengxinlin
 * @create: 2022-06-30 16:09
 */
@Data
public class TenantRespDTO {
    /**
     * ID
     */
    private Integer id;

    /**
     * 租户ID
     */
    private String tenantId;

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

    /**
     * 创建时间
     */
    private Date gmtCreate;

    /**
     * 修改时间
     */
    private Date gmtModified;
}