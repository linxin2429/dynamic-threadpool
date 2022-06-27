package cn.xldeng.server.model;

import lombok.Data;

import java.io.Serializable;

/**
 * @program: threadpool
 * @description: 基础配置信息
 * @author: dengxinlin
 * @create: 2022-06-27 07:27
 */
@Data
public class ConfigInfoBase implements Serializable {
    private static final long serialVersionUID = 2443779416309879749L;

    /**
     * Data ID
     */
    private String dataId;

    /**
     * Group ID
     */
    private String groupId;

    /**
     * 内容
     */
    private String content;

    /**
     * MD5
     */
    private String md5;
}