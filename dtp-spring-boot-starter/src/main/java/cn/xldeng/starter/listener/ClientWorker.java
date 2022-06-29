package cn.xldeng.starter.listener;

import cn.xldeng.starter.core.CacheData;
import cn.xldeng.starter.http.HttpAgent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @program: threadpool
 * @description: 客户端监听
 * @author: dengxinlin
 * @create: 2022-06-28 18:04
 */
@Slf4j
public class ClientWorker {
    private double currentLongingTaskCount = 0;

    private final HttpAgent httpAgent;

    private final ScheduledExecutorService executor;

    private final ScheduledExecutorService executorService;

    private final ConcurrentHashMap<String, CacheData> cacheMap = new ConcurrentHashMap<>(16);

    @SuppressWarnings("all")
    public ClientWorker(HttpAgent httpAgent) {
        this.httpAgent = httpAgent;

        this.executor = Executors.newScheduledThreadPool(1, r -> {
            Thread t = new Thread(r);
            t.setName("cn.xldeng.threadPool.client.worker.executor");
            t.setDaemon(true);
            return t;
        });

        int threadSize = Runtime.getRuntime().availableProcessors();
        this.executorService = Executors.newScheduledThreadPool(threadSize, r -> {
            Thread t = new Thread(r);
            t.setName("cn.xldeng.threadPool.client.worker.longPolling.executor");
            t.setDaemon(true);
            return t;
        });
        this.executor.scheduleWithFixedDelay(() -> {
            try {
                checkConfigInfo();
            } catch (Throwable e) {
                log.error("[sub-check] rotate check error", e);
            }
        }, 1L, 10L, TimeUnit.MILLISECONDS);
    }

    /**
     * 检查配置信息
     */
    public void checkConfigInfo() {
        int listenerSize = cacheMap.size();
        double perTaskConfigSize = 3000D;
        int longingTaskCount = (int) Math.ceil(listenerSize / perTaskConfigSize);
        if (longingTaskCount > currentLongingTaskCount) {
            for (int i = (int) currentLongingTaskCount; i < longingTaskCount; i++) {
                executorService.execute(new LongPollingRunnable(i));
            }
            currentLongingTaskCount = longingTaskCount;
        }
    }

    /**
     * 长轮训任务
     */
    class LongPollingRunnable implements Runnable {

        private final int taskId;

        public LongPollingRunnable(int taskId) {
            this.taskId = taskId;
        }

        @Override
        public void run() {
            List<CacheData> cacheDataList = new ArrayList<>();
            List<String> changedTpIds = checkUpdateTpIds(cacheDataList);
            if (!CollectionUtils.isEmpty(cacheDataList)) {
                log.info("[dynamic threadPool] tpIds changed :: {}", changedTpIds);
            }
            for (String each : changedTpIds) {
                String[] keys = each.split(",");
                String tenant = keys[0];
                String itemId = keys[1];
                String tpId = keys[2];
                try {
                    String content = getServerConfig(tenant, itemId, tpId, 3000L);
                    CacheData cacheData = cacheMap.get(tpId);
                    cacheData.setContent(content);
                    cacheDataList.add(cacheData);
                    log.info("[data-received] tenant :: {}, itemId :: {}, tpId :: {}, md5 :: {}, content :: {}", tenant, itemId, tpId, cacheData.getMd5(), content);
                } catch (Exception ex) {
                    //ignore
                }
            }
            for (CacheData each : cacheDataList) {
                each.checkListenerMd5();
            }
        }
    }

    /**
     * 检查修改的线程池 ID
     *
     * @param cacheDataList cacheDataList
     * @return changedTpIds
     */
    public List<String> checkUpdateTpIds(List<CacheData> cacheDataList) {
        return null;
    }

    public String getServerConfig(String tenant, String itemId, String tpId, long readTimeout) {
        return null;
    }

    public void addTenantListeners(String tpId, List<? extends Listener> listeners) {
        CacheData cacheData = addCacheDataIfAbsent(tpId);
        for (Listener listener : listeners) {
            cacheData.addListener(listener);
        }
    }

    public CacheData addCacheDataIfAbsent(String tpId) {
        CacheData cacheData = cacheMap.get(tpId);
        if (cacheData != null) {
            return cacheData;
        }
        cacheData = new CacheData(tpId);
        return cacheMap.putIfAbsent(tpId, cacheData);
    }
}