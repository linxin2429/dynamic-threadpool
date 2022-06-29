package cn.xldeng.starter.adapter;

import cn.hutool.core.thread.ThreadFactoryBuilder;
import cn.xldeng.starter.operation.ThreadPoolOperation;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @program: threadpool
 * @description:
 * @author: dengxinlin
 * @create: 2022-06-29 00:56
 */
public class ThreadPoolConfigAdapter extends ConfigAdapter{
    @Autowired
    private ThreadPoolOperation threadPoolOperation;

    private ExecutorService executorService = new ThreadPoolExecutor(
            2,
            4,
            0,
            TimeUnit.MILLISECONDS,
            new ArrayBlockingQueue<>(1),
            new ThreadFactoryBuilder().setNamePrefix("threadPool-config").build(),
            new ThreadPoolExecutor.DiscardOldestPolicy()
    );

    public void subscribeConfig(List<String> tpIds){
        tpIds.forEach(each->threadPoolOperation.subscribeConfig(each,executorService, this::callbackConfig));
    }
}