package cn.xldeng.server.service;

import cn.xldeng.server.model.biz.tenant.TenantQueryReqDTO;
import cn.xldeng.server.model.biz.tenant.TenantRespDTO;
import cn.xldeng.server.model.biz.tenant.TenantSaveReqDTO;
import cn.xldeng.server.model.biz.tenant.TenantUpdateReqDTO;
import com.baomidou.mybatisplus.core.metadata.IPage;

/**
 * @program: threadpool
 * @description:
 * @author: dengxinlin
 * @create: 2022-06-30 16:39
 */
public interface TenantService {

    /**
     * 根据 Id 获取业务线
     *
     * @param namespaceId
     * @return
     */
    TenantRespDTO getNameSpaceById(String namespaceId);

    /**
     * 分页查询业务线
     *
     * @param reqDTO
     * @return
     */
    IPage<TenantRespDTO> queryNameSpacePage(TenantQueryReqDTO reqDTO);

    /**
     * 新增业务线
     *
     * @param reqDTO
     */
    void saveNameSpace(TenantSaveReqDTO reqDTO);

    /**
     * 修改业务线
     *
     * @param reqDTO
     */
    void updateNameSpace(TenantUpdateReqDTO reqDTO);

    /**
     * 根据 Id 删除业务线
     *
     * @param namespaceId
     */
    void deleteNameSpaceById(String namespaceId);
}