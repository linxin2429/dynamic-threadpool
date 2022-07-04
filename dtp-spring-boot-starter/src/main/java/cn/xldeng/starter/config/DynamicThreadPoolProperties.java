package cn.xldeng.starter.config;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @program: threadpool
 * @description:
 * @author: dengxinlin
 * @create: 2022-06-28 23:12
 */
@Slf4j
@Getter
@Setter
@ConfigurationProperties(prefix = DynamicThreadPoolProperties.PREFIX)
public class DynamicThreadPoolProperties {
    public static final String PREFIX = "spring.dynamic.thread-pool";
    /**
     * 服务地址
     */
    private String serverAddr;
    /**
     * 租户 ID
     */
    private String tenantId;
    /**
     * 项目 ID
     */
    private String itemId;

    /**
     * 是否开启 banner
     */
    private boolean banner = true;
}