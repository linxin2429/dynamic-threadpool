package cn.xldeng.server.service.biz.impl;

import cn.xldeng.server.mapper.ConfigInfoMapper;
import cn.xldeng.server.model.ConfigAllInfo;
import cn.xldeng.server.model.biz.threadpool.ThreadPoolQueryReqDTO;
import cn.xldeng.server.model.biz.threadpool.ThreadPoolRespDTO;
import cn.xldeng.server.model.biz.threadpool.ThreadPoolSaveOrUpdateReqDTO;
import cn.xldeng.server.service.biz.ConfigService;
import cn.xldeng.server.service.biz.ThreadPoolService;
import cn.xldeng.server.toolkit.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @program: threadpool
 * @description:
 * @author: dengxinlin
 * @create: 2022-06-30 17:14
 */
@Service
public class ThreadPoolServiceImpl implements ThreadPoolService {
    @Resource
    private ConfigInfoMapper configInfoMapper;

    @Resource
    private ConfigService configService;

    @Override
    public IPage<ThreadPoolRespDTO> queryThreadPoolPage(ThreadPoolQueryReqDTO reqDTO) {
        LambdaQueryWrapper<ConfigAllInfo> wrapper = Wrappers.lambdaQuery(ConfigAllInfo.class)
                .eq(!StringUtils.isBlank(reqDTO.getTenantId()), ConfigAllInfo::getNamespace, reqDTO.getTenantId())
                .eq(!StringUtils.isBlank(reqDTO.getItemId()), ConfigAllInfo::getItemId, reqDTO.getItemId())
                .eq(!StringUtils.isBlank(reqDTO.getTpId()), ConfigAllInfo::getTpId, reqDTO.getTpId());
        return configInfoMapper.selectPage(reqDTO, wrapper).convert(each -> BeanUtil.convert(each, ThreadPoolRespDTO.class));
    }

    @Override
    public ThreadPoolRespDTO getThreadPool(ThreadPoolQueryReqDTO reqDTO) {
        ConfigAllInfo configAllInfo = configService.findConfigAllInfo(reqDTO.getTpId(), reqDTO.getItemId(), reqDTO.getTenantId());
        return BeanUtil.convert(configAllInfo, ThreadPoolRespDTO.class);
    }

    @Override
    public void saveOrUpdateThreadPoolConfig(ThreadPoolSaveOrUpdateReqDTO reqDTO) {
        configService.insertOrUpdate(BeanUtil.convert(reqDTO, ConfigAllInfo.class));
    }

    @Override
    public List<ThreadPoolRespDTO> getThreadPoolByItemId(String itemId) {
        List<ConfigAllInfo> selectList = configInfoMapper
                .selectList(Wrappers.lambdaUpdate(ConfigAllInfo.class).eq(ConfigAllInfo::getItemId, itemId));
        return BeanUtil.convert(selectList, ThreadPoolRespDTO.class);
    }
}