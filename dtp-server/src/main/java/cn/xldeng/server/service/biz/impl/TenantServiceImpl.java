package cn.xldeng.server.service.biz.impl;

import cn.xldeng.server.enums.DelEnum;
import cn.xldeng.server.mapper.TenantInfoMapper;
import cn.xldeng.server.model.TenantInfo;
import cn.xldeng.server.model.biz.item.ItemQueryReqDTO;
import cn.xldeng.server.model.biz.item.ItemRespDTO;
import cn.xldeng.server.model.biz.tenant.TenantQueryReqDTO;
import cn.xldeng.server.model.biz.tenant.TenantRespDTO;
import cn.xldeng.server.model.biz.tenant.TenantSaveReqDTO;
import cn.xldeng.server.model.biz.tenant.TenantUpdateReqDTO;
import cn.xldeng.server.service.biz.ItemService;
import cn.xldeng.server.service.biz.TenantService;
import cn.xldeng.server.toolkit.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.toolkit.SqlHelper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @program: threadpool
 * @description:
 * @author: dengxinlin
 * @create: 2022-06-30 16:50
 */
@Service
public class TenantServiceImpl implements TenantService {
    @Resource
    private ItemService itemService;

    @Resource
    private TenantInfoMapper tenantInfoMapper;

    @Override
    public TenantRespDTO getNameSpaceById(String namespaceId) {
        LambdaQueryWrapper<TenantInfo> queryWrapper = Wrappers
                .lambdaQuery(TenantInfo.class).eq(TenantInfo::getTenantId, namespaceId);
        TenantInfo tenantInfo = tenantInfoMapper.selectOne(queryWrapper);

        TenantRespDTO result = BeanUtil.convert(tenantInfo, TenantRespDTO.class);
        return result;
    }

    @Override
    public IPage<TenantRespDTO> queryNameSpacePage(TenantQueryReqDTO reqDTO) {
        LambdaQueryWrapper<TenantInfo> wrapper = Wrappers.lambdaQuery(TenantInfo.class)
                .eq(!StringUtils.isEmpty(reqDTO.getTenantId()), TenantInfo::getTenantId, reqDTO.getTenantId())
                .eq(!StringUtils.isEmpty(reqDTO.getTenantName()), TenantInfo::getTenantName, reqDTO.getTenantName())
                .eq(!StringUtils.isEmpty(reqDTO.getOwner()), TenantInfo::getOwner, reqDTO.getOwner());
        Page resultPage = tenantInfoMapper.selectPage(reqDTO, wrapper);

        return resultPage.convert(each -> BeanUtil.convert(each, TenantRespDTO.class));
    }

    @Override
    public void saveNameSpace(TenantSaveReqDTO reqDTO) {
        TenantInfo tenantInfo = BeanUtil.convert(reqDTO, TenantInfo.class);
        int insertResult = tenantInfoMapper.insert(tenantInfo);

        boolean retBool = SqlHelper.retBool(insertResult);
        if (!retBool) {
            throw new RuntimeException("插入失败.");
        }
    }

    @Override
    public void updateNameSpace(TenantUpdateReqDTO reqDTO) {
        TenantInfo tenantInfo = BeanUtil.convert(reqDTO, TenantInfo.class);
        int updateResult = tenantInfoMapper.update(tenantInfo, Wrappers
                .lambdaUpdate(TenantInfo.class).eq(TenantInfo::getTenantId, reqDTO.getNamespaceId()));
        boolean retBool = SqlHelper.retBool(updateResult);
        if (!retBool) {
            throw new RuntimeException("修改失败.");
        }
    }

    @Override
    public void deleteNameSpaceById(String namespaceId) {
        TenantInfo tenantInfo = new TenantInfo();
        tenantInfo.setDelFlag(DelEnum.DELETE.getIntCode());

        ItemQueryReqDTO reqDTO = new ItemQueryReqDTO();
        reqDTO.setTenantId(namespaceId);
        List<ItemRespDTO> itemList = itemService.queryItem(reqDTO);
        if (CollectionUtils.isNotEmpty(itemList)) {
            throw new RuntimeException("业务线包含项目引用, 删除失败.");
        }
        int updateResult = tenantInfoMapper.update(tenantInfo,
                Wrappers.lambdaUpdate(TenantInfo.class).eq(TenantInfo::getTenantId, namespaceId));
        boolean retBool = SqlHelper.retBool(updateResult);
        if (!retBool) {
            throw new RuntimeException("删除失败.");
        }
    }
}