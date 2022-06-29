package cn.xldeng.starter.listener;

import cn.xldeng.common.web.base.Result;
import cn.xldeng.starter.common.CommonThreadPool;
import cn.xldeng.starter.common.Constants;
import cn.xldeng.common.config.ApplicationContextHolder;
import cn.xldeng.starter.config.DynamicThreadPoolProperties;
import cn.xldeng.starter.core.GlobalThreadPoolManage;
import cn.xldeng.common.model.PoolParameterInfo;
import cn.xldeng.starter.remote.HttpAgent;
import cn.xldeng.starter.remote.ServerHttpAgent;
import cn.xldeng.starter.tookit.BlockingQueueUtil;
import cn.xldeng.starter.tookit.HttpClientUtil;
import cn.xldeng.starter.wrap.DynamicThreadPoolWrap;
import com.alibaba.fastjson.JSON;
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
            Map<String, String > queryStrMap = new HashMap<>(16);
            queryStrMap.put("tpId", val.getTpId());
            queryStrMap.put("itemId", dynamicThreadPoolProperties.getItemId());
            queryStrMap.put("namespace", dynamicThreadPoolProperties.getNamespace());

            PoolParameterInfo ppi;
            HttpAgent httpAgent = new ServerHttpAgent(dynamicThreadPoolProperties);
            Result result = httpAgent.httpGet(Constants.CONFIG_CONTROLLER_PATH, null, queryStrMap, 3000L);
            if (result.isSuccess() && (ppi = JSON.toJavaObject((JSON) result.getData(),PoolParameterInfo.class)) != null){
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