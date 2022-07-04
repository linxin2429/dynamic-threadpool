package cn.xldeng.consol.controller;

import cn.xldeng.common.constant.Constants;
import cn.xldeng.common.web.base.Result;
import cn.xldeng.common.web.base.Results;
import cn.xldeng.config.model.biz.threadpool.ThreadPoolQueryReqDTO;
import cn.xldeng.config.model.biz.threadpool.ThreadPoolRespDTO;
import cn.xldeng.config.model.biz.threadpool.ThreadPoolSaveOrUpdateReqDTO;
import cn.xldeng.config.service.biz.ThreadPoolService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @program: threadpool
 * @description:
 * @author: dengxinlin
 * @create: 2022-06-30 17:21
 */
@RestController
@RequestMapping(Constants.CONFIG_CONTROLLER_PATH + "/thread")
public class ThreadPoolController {
    @Resource
    private ThreadPoolService threadPoolService;

    @PostMapping("/pool/query/page")
    public Result<IPage<ThreadPoolRespDTO>> queryNameSpacePage(@RequestBody ThreadPoolQueryReqDTO reqDTO) {
        return Results.success(threadPoolService.queryThreadPoolPage(reqDTO));
    }

    @PostMapping("/pool/query")
    public Result<ThreadPoolRespDTO> queryNameSpace(@RequestBody ThreadPoolQueryReqDTO reqDTO) {
        return Results.success(threadPoolService.getThreadPool(reqDTO));
    }

    @PostMapping("/pool/save_or_update")
    public Result saveOrUpdateThreadPoolConfig(@RequestBody ThreadPoolSaveOrUpdateReqDTO reqDTO) {
        threadPoolService.saveOrUpdateThreadPoolConfig(reqDTO);
        return Results.success();
    }
}