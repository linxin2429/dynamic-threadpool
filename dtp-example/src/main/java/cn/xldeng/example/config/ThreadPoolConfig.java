package cn.xldeng.example.config;

import cn.xldeng.common.enums.QueueTypeEnum;
import cn.xldeng.starter.core.GlobalThreadPoolManage;
import cn.xldeng.starter.tookit.BlockingQueueUtil;
import cn.xldeng.starter.tookit.thread.ThreadPoolBuilder;
import cn.xldeng.starter.wrap.CustomThreadPoolExecutor;
import cn.xldeng.starter.wrap.DynamicThreadPoolWrap;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @program: threadpool
 * @description:
 * @author: dengxinlin
 * @create: 2022-06-28 17:50
 */
@Slf4j
@Configuration
public class ThreadPoolConfig {

    private final String messageConsumePrefix = "message-consume";

    private final String messageProducePrefix = "message-produce";

    private final String customPoolPrefix = "custom-pool";

    @Bean
    public DynamicThreadPoolWrap messageCenterConsumeThreadPool() {
        return new DynamicThreadPoolWrap(messageConsumePrefix);
    }

    @Bean
    public DynamicThreadPoolWrap messageCenterProduceThreadPool() {
        return new DynamicThreadPoolWrap(messageProducePrefix);
    }

    //@Bean
    //public DynamicThreadPoolWrap customPool() {
    //    return new DynamicThreadPoolWrap(customPoolPrefix);
    //}

    @PostConstruct
    @SuppressWarnings("all")
    public void testExecutTask() {
        log.info("测试线程池运行时状态接口, 30s 后开始触发拒绝策略...");
        ThreadPoolExecutor poolExecutor = ThreadPoolBuilder.builder()
                .isCustomPool(true)
                .poolThreadSize(5, 10)
                .keepAliveTime(9999, TimeUnit.SECONDS)
                .workQueue(BlockingQueueUtil.createBlockingQueue(QueueTypeEnum.RESIZABLE_LINKED_BLOCKING_QUEUE.type, 1))
                .threadFactory(customPoolPrefix)
                .rejected(new CustomThreadPoolExecutor.AbortPolicy())
                .build();
        System.out.println(poolExecutor);
        ScheduledExecutorService scheduledThreadPool = Executors.newSingleThreadScheduledExecutor();
        scheduledThreadPool.scheduleAtFixedRate(() -> {
            DynamicThreadPoolWrap executorService = GlobalThreadPoolManage.getExecutorService(messageConsumePrefix);
            ThreadPoolExecutor pool = executorService.getPool();
            try {
                pool.execute(() -> {
                    log.info("线程池名称 :: {}, 正在执行即将进入阻塞...", Thread.currentThread().getName());
                    try {
                        int maxRandom = 10;
                        int temp = 2;
                        Random random = new Random();
                        if (random.nextInt(maxRandom) % temp == 0) {
                            Thread.sleep(10241024);
                        } else {
                            Thread.sleep(3000);
                        }
                    } catch (InterruptedException e) {

                    }
                });
            } catch (Exception ex) {
                // ignore
            }
        }, 5, 2, TimeUnit.SECONDS);

    }
}