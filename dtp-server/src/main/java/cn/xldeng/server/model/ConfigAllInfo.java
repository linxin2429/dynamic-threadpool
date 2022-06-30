package cn.xldeng.server.model;

import cn.xldeng.common.model.PoolParameter;
import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

/**
 * @program: threadpool
 * @description: 配置全部信息
 * @author: dengxinlin
 * @create: 2022-06-27 07:27
 */
@Data
public class ConfigAllInfo extends ConfigInfo implements PoolParameter {

    private static final long serialVersionUID = -2069435444024108298L;

    @JSONField(serialize = false)
    private String createUser;

    @JSONField(serialize = false)
    private String desc;

    @JSONField(serialize = false)
    private Long createTime;

    @JSONField(serialize = false)
    private Long modifyTime;
}