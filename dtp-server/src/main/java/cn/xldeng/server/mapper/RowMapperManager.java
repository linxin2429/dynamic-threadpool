package cn.xldeng.server.mapper;

import cn.xldeng.server.model.ConfigAllInfo;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @program: threadpool
 * @description:
 * @author: dengxinlin
 * @create: 2022-06-27 07:27
 */
public final class RowMapperManager {

    public static final ConfigAllInfoRowMapper CONFIG_ALL_INFO_ROW_MAPPER = new ConfigAllInfoRowMapper();

    public static class ConfigAllInfoRowMapper implements RowMapper<ConfigAllInfo> {

        @Override
        public ConfigAllInfo mapRow(ResultSet rs, int rowNum) throws SQLException {
            ConfigAllInfo configAllInfo = new ConfigAllInfo();
            configAllInfo.setDataId(rs.getString("data_id"));
            configAllInfo.setGroupId(rs.getString("group_id"));
            configAllInfo.setContent(rs.getString("content"));
            configAllInfo.setMd5(rs.getString("md5"));
            configAllInfo.setTenant(rs.getString("tenant_id"));
            configAllInfo.setCreateTime(rs.getTimestamp("gmt_modified").getTime());
            configAllInfo.setModifyTime(rs.getTimestamp("gmt_modified").getTime());
            return configAllInfo;
        }
    }
}