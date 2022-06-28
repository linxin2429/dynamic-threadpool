package cn.xldeng.starter.core;

import cn.xldeng.starter.wrap.DynamicThreadPoolWrap;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @program: threadpool
 * @description: 线程池全局管理
 * @author: dengxinlin
 * @create: 2022-06-27 07:32
 */
public class GlobalThreadPoolManage {

    private static final Map<String, DynamicThreadPoolWrap> EXECUTOR_MAP = new ConcurrentHashMap<>();

    public static DynamicThreadPoolWrap getExecutorService(String name) {
        return EXECUTOR_MAP.get(name);
    }

    public static void register(String name, DynamicThreadPoolWrap executor) {
        EXECUTOR_MAP.put(name, executor);
    }
}