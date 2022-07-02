package cn.xldeng.config.model.biz.item;

import lombok.Data;

/**
 * @program: threadpool
 * @description:
 * @author: dengxinlin
 * @create: 2022-06-30 16:08
 */
@Data
public class ItemSaveReqDTO {
    /**
     * 租户 ID
     */
    private String tenantId;

    /**
     * 项目 ID
     */
    private String itemId;

    /**
     * 项目名称
     */
    private String itemName;

    /**
     * 项目介绍
     */
    private String itemDesc;

    /**
     * 项目负责人
     */
    private String owner;
}