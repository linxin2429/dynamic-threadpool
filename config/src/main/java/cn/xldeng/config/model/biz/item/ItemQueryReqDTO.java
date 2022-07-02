package cn.xldeng.config.model.biz.item;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.Data;

/**
 * @program: threadpool
 * @description:
 * @author: dengxinlin
 * @create: 2022-06-30 16:08
 */
@Data
public class ItemQueryReqDTO extends Page {
    private String tenantId;

    private String itemId;

    private String itemName;

    private String owner;
}