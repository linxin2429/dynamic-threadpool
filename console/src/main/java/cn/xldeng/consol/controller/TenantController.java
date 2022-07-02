package cn.xldeng.consol.controller;

import cn.xldeng.common.constant.Constants;
import cn.xldeng.common.web.base.Result;
import cn.xldeng.common.web.base.Results;
import cn.xldeng.config.model.biz.tenant.TenantQueryReqDTO;
import cn.xldeng.config.model.biz.tenant.TenantRespDTO;
import cn.xldeng.config.model.biz.tenant.TenantSaveReqDTO;
import cn.xldeng.config.model.biz.tenant.TenantUpdateReqDTO;
import cn.xldeng.config.service.biz.TenantService;
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
@RequestMapping(Constants.BASE_PATH + "/tenant")
public class TenantController {
    @Resource
    private TenantService tenantService;

    @PostMapping("/query/page")
    public Result<IPage<TenantRespDTO>> queryNameSpacePage(@RequestBody TenantQueryReqDTO reqDTO) {
        return Results.success(tenantService.queryTenantPage(reqDTO));
    }

    @GetMapping("/query/{tenantId}")
    public Result<TenantRespDTO> queryNameSpace(@PathVariable("tenantId") String tenantId) {
        return Results.success(tenantService.getTenantById(tenantId));
    }

    @PostMapping("/save")
    public Result saveNameSpace(@RequestBody TenantSaveReqDTO reqDTO) {
        tenantService.saveTenant(reqDTO);
        return Results.success();
    }

    @PostMapping("/update")
    public Result updateNameSpace(@RequestBody TenantUpdateReqDTO reqDTO) {
        tenantService.updateTenant(reqDTO);
        return Results.success();
    }

    @DeleteMapping("/delete/{tenantId}")
    public Result deleteNameSpace(@PathVariable("tenantId") String tenantId) {
        tenantService.deleteTenantById(tenantId);
        return Results.success();
    }
}