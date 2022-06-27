package cn.xldeng.server.model;

import lombok.Data;

/**
 * @program: threadpool
 * @description: 配置全部信息
 * @author: dengxinlin
 * @create: 2022-06-27 07:27
 */
@Data
public class ConfigAllInfo extends ConfigInfo {

    private static final long serialVersionUID = -2069435444024108298L;

    private String createUser;

    private String desc;

    private Long createTime;

    private Long modifyTime;
}