package cn.xldeng.example.config;

import cn.xldeng.starter.wrap.DynamicThreadPoolWrap;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @program: threadpool
 * @description:
 * @author: dengxinlin
 * @create: 2022-06-28 17:50
 */
@Configuration
public class ThreadPoolConfig {
    @Bean
    public DynamicThreadPoolWrap messageCenterConsumeThreadPool() {
        return new DynamicThreadPoolWrap("common", "message", "message-consumer");
    }
}