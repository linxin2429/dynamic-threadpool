package cn.xldeng.starter.core;

import cn.xldeng.common.config.ApplicationContextHolder;
import cn.xldeng.starter.tookit.thread.QueueTypeEnum;
import cn.xldeng.starter.tookit.thread.ThreadPoolBuilder;
import cn.xldeng.starter.wrap.DynamicThreadPoolWrap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @program: threadpool
 * @description:
 * @author: dengxinlin
 * @create: 2022-06-29 00:56
 */
public class ThreadPoolConfigAdapter extends ConfigAdapter {
    @Autowired
    private ThreadPoolOperation threadPoolOperation;

    private final ExecutorService executorService = ThreadPoolBuilder.builder()
            .poolThreadSize(2, 4)
            .keepAliveTime(0L, TimeUnit.MILLISECONDS)
            .workQueue(QueueTypeEnum.ARRAY_BLOCKING_QUEUE, 1)
            .threadFactory("threadPool-config")
            .rejected(new ThreadPoolExecutor.DiscardOldestPolicy())
            .build();


    @Order(1025)
    @PostConstruct
    public void subscribeConfig() {
        Map<String, DynamicThreadPoolWrap> executorMap = ApplicationContextHolder.getBeansOfType(DynamicThreadPoolWrap.class);
        List<String> tpIdList = new ArrayList<>();
        executorMap.forEach((key, val) -> tpIdList.add(val.getTpId()));
        tpIdList.forEach(each -> threadPoolOperation.subscribeConfig(each, executorService, this::callbackConfig));
    }
}