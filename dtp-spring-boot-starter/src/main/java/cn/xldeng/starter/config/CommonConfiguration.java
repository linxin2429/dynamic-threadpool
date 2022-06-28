package cn.xldeng.starter.config;

import cn.xldeng.starter.core.ThreadPoolRunListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @program: threadpool
 * @description: 公告配置
 * @author: dengxinlin
 * @create: 2022-06-27 07:32
 */
@Configuration
public class CommonConfiguration {
    @Bean
    public ApplicationContexHolder applicationContexHolder() {
        return new ApplicationContexHolder();
    }

    @Bean
    public ThreadPoolRunListener threadPoolRunListener() {
        return new ThreadPoolRunListener();
    }
}