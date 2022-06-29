package cn.xldeng.common.config;

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
    public ApplicationContextHolder applicationContexHolder() {
        return new ApplicationContextHolder();
    }


}