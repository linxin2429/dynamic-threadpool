package cn.xldeng.starter.listener;

import cn.xldeng.common.config.ApplicationContextHolder;
import cn.xldeng.common.constant.Constants;
import cn.xldeng.common.model.PoolParameterInfo;
import cn.xldeng.common.web.base.Result;
import cn.xldeng.starter.config.CommonThreadPool;
import cn.xldeng.starter.config.DynamicThreadPoolBanner;
import cn.xldeng.starter.config.DynamicThreadPoolProperties;
import cn.xldeng.starter.core.GlobalThreadPoolManage;
import cn.xldeng.starter.remote.HttpAgent;
import cn.xldeng.starter.remote.ServerHttpAgent;
import cn.xldeng.starter.tookit.HttpClientUtil;
import cn.xldeng.starter.tookit.thread.QueueTypeEnum;
import cn.xldeng.starter.tookit.thread.ThreadPoolBuilder;
import cn.xldeng.starter.wrap.CustomThreadPoolExecutor;
import cn.xldeng.starter.wrap.DynamicThreadPoolWrap;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;

import javax.annotation.PostConstruct;
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
@Slf4j
public class ThreadPoolRunListener {

    @Resource
    private HttpClientUtil httpClientUtil;

    private final DynamicThreadPoolProperties dynamicThreadPoolProperties;

    public ThreadPoolRunListener(DynamicThreadPoolProperties properties) {
        this.dynamicThreadPoolProperties = properties;
    }

    @Order(1024)
    @PostConstruct
    public void run() {
        DynamicThreadPoolBanner.printBanner(dynamicThreadPoolProperties.isBanner());
        Map<String, DynamicThreadPoolWrap> executorMap =
                ApplicationContextHolder.getBeansOfType(DynamicThreadPoolWrap.class);
        executorMap.forEach((key, val) -> {
            String tpId = val.getTpId();
            Map<String, String> queryStrMap = new HashMap<>(3);
            queryStrMap.put("tpId", tpId);
            queryStrMap.put("itemId", dynamicThreadPoolProperties.getItemId());
            queryStrMap.put("namespace", dynamicThreadPoolProperties.getTenantId());

            PoolParameterInfo ppi = new PoolParameterInfo();
            HttpAgent httpAgent = new ServerHttpAgent(dynamicThreadPoolProperties);
            Result result;
            try {
                result = httpAgent.httpGet(Constants.CONFIG_CONTROLLER_PATH, null, queryStrMap, 3000L);
                if (result.isSuccess() && result.getData() != null && (ppi = JSON.toJavaObject((JSON) result.getData(), PoolParameterInfo.class)) != null) {
                    TimeUnit unit = TimeUnit.SECONDS;
                    BlockingQueue<Runnable> workQueue = QueueTypeEnum.createBlockingQueue(ppi.getQueueType(), ppi.getCapacity());
                    ThreadPoolExecutor poolExecutor = ThreadPoolBuilder.builder()
                            .isCustomPool(true)
                            .poolThreadSize(ppi.getCoreSize(), ppi.getMaxSize())
                            .keepAliveTime(ppi.getKeepAliveTime(), TimeUnit.SECONDS)
                            .workQueue(workQueue)
                            .threadFactory(tpId)
                            .rejected(new CustomThreadPoolExecutor.AbortPolicy())
                            .build();
                    val.setPool(poolExecutor);
                } else if (val.getPool() == null) {
                    val.setPool(CommonThreadPool.getInstance(tpId));
                }
            } catch (Exception ex) {
                log.error("[Init pool] Failed to initialize thread pool configuration. error message :: {}", ex.getMessage());
                val.setPool(CommonThreadPool.getInstance(tpId));
            }
            GlobalThreadPoolManage.register(val.getTpId(), ppi, val);
        });
    }
}