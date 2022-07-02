package cn.xldeng.config.service.biz.impl;

import cn.xldeng.common.toolkit.ContentUtil;
import cn.xldeng.common.toolkit.Md5Util;
import cn.xldeng.config.event.LocalDataChangeEvent;
import cn.xldeng.config.mapper.ConfigInfoMapper;
import cn.xldeng.config.model.ConfigAllInfo;
import cn.xldeng.config.service.ConfigChangePublisher;
import cn.xldeng.config.service.biz.ConfigService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.toolkit.SqlHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @program: threadpool
 * @description:
 * @author: dengxinlin
 * @create: 2022-06-27 07:28
 */
@Slf4j
@Service
public class ConfigServiceImpl implements ConfigService {

    @Resource
    private ConfigInfoMapper configInfoMapper;


    @Override
    public ConfigAllInfo findConfigAllInfo(String tpId, String itemId, String tenantId) {
        LambdaQueryWrapper<ConfigAllInfo> wrapper = Wrappers.lambdaQuery(ConfigAllInfo.class)
                .eq(!StringUtils.isBlank(tpId), ConfigAllInfo::getTpId, tpId)
                .eq(!StringUtils.isBlank(itemId), ConfigAllInfo::getItemId, itemId)
                .eq(!StringUtils.isBlank(tenantId), ConfigAllInfo::getTenantId, tenantId);
        return configInfoMapper.selectOne(wrapper);
    }

    @Override
    public void insertOrUpdate(ConfigAllInfo configAllInfo) {
        try {
            addConfigInfo(configAllInfo);
        } catch (Exception ex) {
            updateConfigInfo(configAllInfo);
        }
        ConfigChangePublisher.notifyConfigChange(new LocalDataChangeEvent(ContentUtil.getGroupKey(configAllInfo)));
    }

    private Integer addConfigInfo(ConfigAllInfo config) {
        config.setContent(ContentUtil.getPoolContent(config));
        config.setMd5(Md5Util.getTpContentMd5(config));
        try {
            if (SqlHelper.retBool(configInfoMapper.insert(config))) {
                return config.getId();
            }
        } catch (Exception ex) {
            log.error("[db-error] message :: {}", ex.getMessage(), ex);
            throw ex;
        }
        return null;
    }

    private void updateConfigInfo(ConfigAllInfo config) {
        LambdaUpdateWrapper<ConfigAllInfo> wrapper = Wrappers.lambdaUpdate(ConfigAllInfo.class)
                .eq(ConfigAllInfo::getTpId, config.getTpId())
                .eq(ConfigAllInfo::getItemId, config.getItemId())
                .eq(ConfigAllInfo::getTenantId, config.getTenantId());
        config.setGmtCreate(null);
        config.setContent(ContentUtil.getPoolContent(config));
        config.setMd5(Md5Util.getTpContentMd5(config));
        try {
            configInfoMapper.update(config, wrapper);
        } catch (Exception ex) {
            log.error("[db-error] message :: {}", ex.getMessage(), ex);
            throw ex;
        }
    }


}