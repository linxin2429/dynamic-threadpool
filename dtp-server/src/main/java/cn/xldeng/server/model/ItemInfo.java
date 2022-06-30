package cn.xldeng.server.model;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

/**
 * @program: threadpool
 * @description:
 * @author: dengxinlin
 * @create: 2022-06-30 16:10
 */
@Data
@TableName("item")
public class ItemInfo {
    private Integer id;

    private String tenantId;

    private String itemId;

    private String itemName;

    private String itemDesc;

    private String owner;

    private Date gmtCreate;

    private Date gmtModified;
}