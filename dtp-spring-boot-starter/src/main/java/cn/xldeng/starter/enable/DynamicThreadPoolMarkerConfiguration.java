package cn.xldeng.starter.enable;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 动态线程池标记配置
 *
 * @author dengxinlin
 * @date 2022/07/04 09:01:45
 */
@Configuration(proxyBeanMethods = false)
public class DynamicThreadPoolMarkerConfiguration {

    @Bean
    public Marker dynamicThreadPoolMarkerBean() {
        return new Marker();
    }

    public class Marker {

    }
}