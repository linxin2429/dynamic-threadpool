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
    /**
     * 命名空间
     */
    private String namespace;

    /**
     * 项目 ID
     */
    private String itemId;

    /**
     * 线程池 ID
     */
    private String tpId;
}