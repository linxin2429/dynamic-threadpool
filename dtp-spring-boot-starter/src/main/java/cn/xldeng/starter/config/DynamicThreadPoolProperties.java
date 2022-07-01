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
     * 命名空间
     */
    private String namespace;
    /**
     * 项目 Id
     */
    private String itemId;

    /**
     * 是否开启动态线程池
     */
    private boolean enabled;

    /**
     * 是否开启 banner
     */
    private boolean banner;
}