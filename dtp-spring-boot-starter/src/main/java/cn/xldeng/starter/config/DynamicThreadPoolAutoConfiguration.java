package cn.xldeng.starter.config;

import cn.xldeng.common.config.CommonConfiguration;
import cn.xldeng.starter.adapter.ThreadPoolConfigAdapter;
import cn.xldeng.starter.controller.PoolRunStateController;
import cn.xldeng.starter.core.ConfigService;
import cn.xldeng.starter.core.ThreadPoolConfigService;
import cn.xldeng.starter.enable.DynamicThreadPoolMarkerConfiguration;
import cn.xldeng.starter.listener.ThreadPoolRunListener;
import cn.xldeng.starter.operation.ThreadPoolOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @program: threadpool
 * @description:
 * @author: dengxinlin
 * @create: 2022-06-29 01:01
 */
@Slf4j
@Configuration
@AllArgsConstructor
@EnableConfigurationProperties(DynamicThreadPoolProperties.class)
@ConditionalOnBean(DynamicThreadPoolMarkerConfiguration.Marker.class)
@ImportAutoConfiguration({OkHttpClientConfig.class, CommonConfiguration.class})
public class DynamicThreadPoolAutoConfiguration {

    private final DynamicThreadPoolProperties poolProperties;

    @Bean
    public ConfigService configService() {
        return new ThreadPoolConfigService(poolProperties);
    }

    @Bean
    public ThreadPoolRunListener threadPoolRunListener() {
        return new ThreadPoolRunListener(poolProperties);
    }

    @Bean
    public ThreadPoolConfigAdapter threadPoolConfigAdapter() {
        return new ThreadPoolConfigAdapter();
    }

    @Bean
    public ThreadPoolOperation threadPoolOperation() {
        return new ThreadPoolOperation(poolProperties);
    }

    @Bean
    public PoolRunStateController poolRunStateController() {
        return new PoolRunStateController();
    }
}