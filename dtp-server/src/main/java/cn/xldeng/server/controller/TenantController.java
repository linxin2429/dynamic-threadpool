package cn.xldeng.server.controller;

import cn.xldeng.common.web.base.Result;
import cn.xldeng.common.web.base.Results;
import cn.xldeng.server.constant.Constants;
import cn.xldeng.server.model.biz.tenant.TenantQueryReqDTO;
import cn.xldeng.server.model.biz.tenant.TenantRespDTO;
import cn.xldeng.server.model.biz.tenant.TenantSaveReqDTO;
import cn.xldeng.server.model.biz.tenant.TenantUpdateReqDTO;
import cn.xldeng.server.service.biz.TenantService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @program: threadpool
 * @description:
 * @author: dengxinlin
 * @create: 2022-06-30 16:55
 */
@RestController
@RequestMapping(Constants.BASE_PATH)
public class TenantController {
    @Resource
    private TenantService tenantService;

    @PostMapping("/namespace/query/page")
    public Result<IPage<TenantRespDTO>> queryNameSpacePage(@RequestBody TenantQueryReqDTO reqDTO) {
        return Results.success(tenantService.queryNameSpacePage(reqDTO));
    }

    @GetMapping("/namespace/query/{namespaceId}")
    public Result<TenantRespDTO> queryNameSpace(@PathVariable("namespaceId") String namespaceId) {
        return Results.success(tenantService.getNameSpaceById(namespaceId));
    }

    @PostMapping("/namespace/save")
    public Result saveNameSpace(@RequestBody TenantSaveReqDTO reqDTO) {
        tenantService.saveNameSpace(reqDTO);
        return Results.success();
    }

    @PostMapping("/namespace/update")
    public Result updateNameSpace(@RequestBody TenantUpdateReqDTO reqDTO) {
        tenantService.updateNameSpace(reqDTO);
        return Results.success();
    }

    @DeleteMapping("/namespace/delete/{namespaceId}")
    public Result deleteNameSpace(@PathVariable("namespaceId") String namespaceId) {
        tenantService.deleteNameSpaceById(namespaceId);
        return Results.success();
    }
}