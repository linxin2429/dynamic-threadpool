package cn.xldeng.config.model.biz.item;

import lombok.Data;

/**
 * @program: threadpool
 * @description:
 * @author: dengxinlin
 * @create: 2022-06-30 16:09
 */
@Data
public class ItemUpdateReqDTO {

    private String tenantId;

    private String itemId;

    private String itemName;

    private String itemDesc;

    private String owner;
}