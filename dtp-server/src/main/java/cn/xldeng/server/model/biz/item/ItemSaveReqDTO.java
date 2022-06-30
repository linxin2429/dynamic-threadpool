package cn.xldeng.server.model.biz.item;

import lombok.Data;

/**
 * @program: threadpool
 * @description:
 * @author: dengxinlin
 * @create: 2022-06-30 16:08
 */
@Data
public class ItemSaveReqDTO {
    private String tenantId;

    private String itemId;

    private String itemName;

    private String itemDesc;

    private String owner;
}