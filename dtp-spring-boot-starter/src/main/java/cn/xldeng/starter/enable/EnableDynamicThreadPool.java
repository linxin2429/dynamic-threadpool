package cn.xldeng.starter.enable;

import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * 启用动态线程池
 *
 * @author dengxinlin
 * @date 2022/07/04 09:03:51
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import(DynamicThreadPoolMarkerConfiguration.class)
public @interface EnableDynamicThreadPool {

}
