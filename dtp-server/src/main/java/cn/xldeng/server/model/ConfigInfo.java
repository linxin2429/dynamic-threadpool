package cn.xldeng.server.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @program: threadpool
 * @description: 配置信息
 * @author: dengxinlin
 * @create: 2022-06-27 07:27
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class ConfigInfo extends ConfigInfoBase {

    private static final long serialVersionUID = -3323888073525729155L;

    //private String namespace;
}