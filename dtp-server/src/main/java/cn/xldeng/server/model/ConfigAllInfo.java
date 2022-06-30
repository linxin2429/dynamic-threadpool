package cn.xldeng.server.model;

import cn.xldeng.common.model.PoolParameter;
import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

/**
 * @program: threadpool
 * @description: 配置全部信息
 * @author: dengxinlin
 * @create: 2022-06-27 07:27
 */
@Data
@TableName("config")
public class ConfigAllInfo extends ConfigInfo implements PoolParameter {

    private static final long serialVersionUID = -2069435444024108298L;

    @JSONField(serialize = false)
    @TableField(exist = false,fill = FieldFill.UPDATE)
    private String desc;

    @JSONField(serialize = false)
    private Date gmtCreate;

    @JSONField(serialize = false)
    private Date gmtModified;
}