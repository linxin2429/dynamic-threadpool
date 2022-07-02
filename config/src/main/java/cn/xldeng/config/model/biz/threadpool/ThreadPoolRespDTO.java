package cn.xldeng.config.model.biz.threadpool;

import lombok.Data;

import java.util.Date;

/**
 * @program: threadpool
 * @description:
 * @author: dengxinlin
 * @create: 2022-06-30 17:11
 */
@Data
public class ThreadPoolRespDTO {
    private Integer id;

    private String tenantId;

    private String itemId;

    private String itemName;

    private String itemDesc;

    private String owner;

    private Date gmtCreate;

    private Date gmtModified;
}