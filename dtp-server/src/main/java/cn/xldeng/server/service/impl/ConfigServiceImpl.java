package cn.xldeng.server.service.impl;

import cn.xldeng.server.mapper.RowMapperManager;
import cn.xldeng.server.model.ConfigAllInfo;
import cn.xldeng.server.service.ConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

/**
 * @program: threadpool
 * @description:
 * @author: dengxinlin
 * @create: 2022-06-27 07:28
 */
@Service
public class ConfigServiceImpl implements ConfigService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public ConfigAllInfo findConfigAllInfo(String tpId, String itemId, String tenant) {
        ConfigAllInfo configAllInfo = jdbcTemplate.queryForObject(
                "select * from config where tp_id = ? and item_id = ? and tenant_id = ?",
                new Object[]{tpId, itemId, tenant},
                RowMapperManager.CONFIG_ALL_INFO_ROW_MAPPER
        );

        return configAllInfo;
    }
}