package cn.xldeng.config.service.biz.impl;

import cn.xldeng.config.mapper.ConfigInfoMapper;
import cn.xldeng.config.model.ConfigAllInfo;
import cn.xldeng.config.model.biz.threadpool.ThreadPoolQueryReqDTO;
import cn.xldeng.config.model.biz.threadpool.ThreadPoolRespDTO;
import cn.xldeng.config.model.biz.threadpool.ThreadPoolSaveOrUpdateReqDTO;
import cn.xldeng.config.service.biz.ConfigService;
import cn.xldeng.config.service.biz.ThreadPoolService;
import cn.xldeng.config.toolkit.BeanUtil;
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
                .eq(!StringUtils.isBlank(reqDTO.getNamespace()), ConfigAllInfo::getNamespace, reqDTO.getNamespace())
                .eq(!StringUtils.isBlank(reqDTO.getItemId()), ConfigAllInfo::getItemId, reqDTO.getItemId())
                .eq(!StringUtils.isBlank(reqDTO.getTpId()), ConfigAllInfo::getTpId, reqDTO.getTpId());
        return configInfoMapper.selectPage(reqDTO, wrapper).convert(each -> BeanUtil.convert(each, ThreadPoolRespDTO.class));
    }

    @Override
    public ThreadPoolRespDTO getThreadPool(ThreadPoolQueryReqDTO reqDTO) {
        ConfigAllInfo configAllInfo = configService.findConfigAllInfo(reqDTO.getTpId(), reqDTO.getItemId(), reqDTO.getNamespace());
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