package cn.xldeng.server.service.biz;

import cn.xldeng.server.model.biz.threadpool.ThreadPoolQueryReqDTO;
import cn.xldeng.server.model.biz.threadpool.ThreadPoolRespDTO;
import cn.xldeng.server.model.biz.threadpool.ThreadPoolSaveOrUpdateReqDTO;
import com.baomidou.mybatisplus.core.metadata.IPage;

import java.util.List;

/**
 * @program: threadpool
 * @description:
 * @author: dengxinlin
 * @create: 2022-06-30 17:13
 */
public interface ThreadPoolService {
    /**
     * 分页查询线程池
     *
     * @param reqDTO
     * @return
     */
    IPage<ThreadPoolRespDTO> queryThreadPoolPage(ThreadPoolQueryReqDTO reqDTO);

    /**
     * 查询线程池配置
     *
     * @param reqDTO
     * @return
     */
    ThreadPoolRespDTO getThreadPool(ThreadPoolQueryReqDTO reqDTO);

    /**
     * 新增或修改线程池配置
     *
     * @param reqDTO
     */
    void saveOrUpdateThreadPoolConfig(ThreadPoolSaveOrUpdateReqDTO reqDTO);

    List<ThreadPoolRespDTO> getThreadPoolByItemId(String itemId);
}
