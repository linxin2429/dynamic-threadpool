package cn.xldeng.starter.config;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.util.Map;

/**
 * @program: threadpool
 * @description: 非 Spring 环境下获取 IOC 容器对象
 * @author: dengxinlin
 * @create: 2022-06-27 07:31
 */
public class ApplicationContextHolder implements ApplicationContextAware {

    private static ApplicationContext CONTEXT;


    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        ApplicationContextHolder.CONTEXT = applicationContext;
    }

    /**
     * 根据名称 & 类型获取 IOC 容器 Bean
     *
     * @param name  名称
     * @param clazz 类型
     * @param <T>   泛型
     * @return bean
     */
    public static <T> T getBean(String name, Class<T> clazz) {
        return CONTEXT.getBean(name, clazz);
    }

    /**
     * 根据类型获取一组 IOC 容器 Bean
     *
     * @param clazz 类型
     * @param <T>   泛型
     * @return beans
     */
    public static <T> Map<String, T> getBeansOfType(Class<T> clazz) {
        return CONTEXT.getBeansOfType(clazz);
    }
}