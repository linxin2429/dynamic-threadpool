package cn.xldeng.server.model.biz.item;

import lombok.Data;

import java.util.Date;

/**
 * @program: threadpool
 * @description:
 * @author: dengxinlin
 * @create: 2022-06-30 16:08
 */
@Data
public class ItemRespDTO {
    private Integer id;

    private String tenantId;

    private String itemId;

    private String itemName;

    private String itemDesc;

    private String owner;

    private Date gmtCreate;

    private Date gmtModified;
}