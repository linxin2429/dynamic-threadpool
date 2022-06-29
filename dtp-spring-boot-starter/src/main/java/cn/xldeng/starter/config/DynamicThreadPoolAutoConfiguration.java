package cn.xldeng.starter.config;

import cn.xldeng.starter.adapter.ThreadPoolConfigAdapter;
import cn.xldeng.starter.core.ConfigService;
import cn.xldeng.starter.core.ThreadPoolConfigService;
import cn.xldeng.starter.core.ThreadPoolRunListener;
import cn.xldeng.starter.operation.ThreadPoolOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
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
@ConditionalOnProperty(prefix = DynamicThreadPoolProperties.PREFIX, name = "enabled", havingValue = "true", matchIfMissing = true)
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
        return new ThreadPoolOperation();
    }
}