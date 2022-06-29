package cn.xldeng.starter.core;

import cn.xldeng.starter.common.CommonThreadPool;
import cn.xldeng.starter.config.ApplicationContextHolder;
import cn.xldeng.starter.config.DynamicThreadPoolProperties;
import cn.xldeng.starter.model.PoolParameterInfo;
import cn.xldeng.starter.tookit.BlockingQueueUtil;
import cn.xldeng.starter.tookit.HttpClientUtil;
import cn.xldeng.starter.wrap.DynamicThreadPoolWrap;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @program: threadpool
 * @description:
 * @author: dengxinlin
 * @create: 2022-06-27 07:33
 */
public class ThreadPoolRunListener implements ApplicationRunner {

    @Resource
    private HttpClientUtil httpClientUtil;

    private final DynamicThreadPoolProperties dynamicThreadPoolProperties;

    public ThreadPoolRunListener(DynamicThreadPoolProperties properties) {
        this.dynamicThreadPoolProperties = properties;
    }

    @Override
    public void run(ApplicationArguments args) {
        Map<String, DynamicThreadPoolWrap> executorMap =
                ApplicationContextHolder.getBeansOfType(DynamicThreadPoolWrap.class);
        executorMap.forEach((key, val) -> {
            Map<String, Object> queryStrMap = new HashMap<>(16);
            queryStrMap.put("tpId", val.getTpId());
            queryStrMap.put("itemId", dynamicThreadPoolProperties.getItemId());
            queryStrMap.put("tenant", dynamicThreadPoolProperties.getTenant());

            PoolParameterInfo ppi = httpClientUtil.restApiGet(buildUrl(), queryStrMap, PoolParameterInfo.class);
            if (ppi != null) {
                TimeUnit unit = TimeUnit.SECONDS;
                BlockingQueue<Runnable> workQueue = BlockingQueueUtil.createBlockingQueue(ppi.getQueueType(), ppi.getCapacity());
                ThreadPoolExecutor resultTpe = new ThreadPoolExecutor(ppi.getCoreSize(),
                        ppi.getMaxSize(),
                        ppi.getKeepAliveTime(),
                        unit,
                        workQueue
                );
                val.setPool(resultTpe);
            } else if (val.getPool() == null) {
                val.setPool(CommonThreadPool.getInstance(val.getTpId()));
            }
            GlobalThreadPoolManage.register(val.getTpId(), val);
        });
    }

    private String buildUrl() {
        return "http://127.0.0.1:6691/v1/cs/configs";
    }

}