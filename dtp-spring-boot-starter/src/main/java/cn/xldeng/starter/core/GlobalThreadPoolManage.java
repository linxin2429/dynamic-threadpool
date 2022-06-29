package cn.xldeng.starter.core;

import cn.xldeng.common.model.PoolParameter;
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

    private static final Map<String, PoolParameter> POOL_PARAMETER = new ConcurrentHashMap();

    private static final Map<String, DynamicThreadPoolWrap> EXECUTOR_MAP = new ConcurrentHashMap<>();

    public static DynamicThreadPoolWrap getExecutorService(String tpId) {
        return EXECUTOR_MAP.get(tpId);
    }
    public static PoolParameter getPoolParameter(String tpId) {
        return POOL_PARAMETER.get(tpId);
    }

    public static void register(String tpId, PoolParameter poolParameter, DynamicThreadPoolWrap executor) {
        registerPool(tpId, executor);
        registerPoolParameter(tpId, poolParameter);
    }
    public static void registerPool(String tpId, DynamicThreadPoolWrap executor) {
        EXECUTOR_MAP.put(tpId, executor);
    }

    public static void registerPoolParameter(String tpId, PoolParameter poolParameter) {
        POOL_PARAMETER.put(tpId, poolParameter);
    }
}