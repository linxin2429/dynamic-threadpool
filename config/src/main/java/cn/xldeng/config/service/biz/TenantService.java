package cn.xldeng.config.service.biz;

import cn.xldeng.config.model.biz.tenant.TenantQueryReqDTO;
import cn.xldeng.config.model.biz.tenant.TenantRespDTO;
import cn.xldeng.config.model.biz.tenant.TenantSaveReqDTO;
import cn.xldeng.config.model.biz.tenant.TenantUpdateReqDTO;
import com.baomidou.mybatisplus.core.metadata.IPage;

/**
 * @program: threadpool
 * @description:
 * @author: dengxinlin
 * @create: 2022-06-30 16:39
 */
public interface TenantService {

    /**
     * 根据 Id 获取租户
     *
     * @param tenantId
     * @return
     */
    TenantRespDTO getTenantById(String tenantId);

    /**
     * 分页查询租户
     *
     * @param reqDTO
     * @return
     */
    IPage<TenantRespDTO> queryTenantPage(TenantQueryReqDTO reqDTO);

    /**
     * 新增租户
     *
     * @param reqDTO
     */
    void saveTenant(TenantSaveReqDTO reqDTO);

    /**
     * 修改租户
     *
     * @param reqDTO
     */
    void updateTenant(TenantUpdateReqDTO reqDTO);

    /**
     * 根据 Id 删除租户
     *
     * @param tenantId
     */
    void deleteTenantById(String tenantId);
}