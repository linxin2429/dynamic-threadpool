package cn.xldeng.starter.core;

import cn.xldeng.starter.common.CommonThreadPool;
import cn.xldeng.starter.config.ApplicationContexHolder;
import cn.xldeng.starter.model.PoolParameterInfo;
import cn.xldeng.starter.tookit.BlockingQueueUtil;
import cn.xldeng.starter.tookit.HttpClientUtil;
import cn.xldeng.starter.wrap.DynamicThreadPoolWrap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;

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

    @Autowired
    private HttpClientUtil httpClientUtil;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        Map<String, DynamicThreadPoolWrap> executorMap =
                ApplicationContexHolder.getBeansOfType(DynamicThreadPoolWrap.class);
        executorMap.forEach((key, val) -> {
            Map<String, Object> queryStrMap = new HashMap<>(16);
            queryStrMap.put("tpId", val.getTpId());
            queryStrMap.put("itemId", val.getItemId());
            queryStrMap.put("tenant", val.getTenant());

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
            GlobalThreadPoolManage.register(buildOnlyId(val), val);
        });
    }

    private String buildUrl() {
        return "http://127.0.0.1:6691/v1/cs/configs";
    }

    private String buildOnlyId(DynamicThreadPoolWrap poolWrap) {
        return poolWrap.getTenant() + "_" + poolWrap.getItemId() + "_" + poolWrap.getTpId();
    }
}