package cn.xldeng.config.model.biz.threadpool;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.Data;

/**
 * @program: threadpool
 * @description:
 * @author: dengxinlin
 * @create: 2022-06-30 17:10
 */
@Data
public class ThreadPoolQueryReqDTO extends Page {
    private String tenantId;

    private String itemId;

    private String tpId;

    private String tpName;
}