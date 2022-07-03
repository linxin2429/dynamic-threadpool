package cn.xldeng.starter.listener;

import cn.xldeng.common.constant.Constants;
import cn.xldeng.common.model.PoolParameter;
import cn.xldeng.common.toolkit.ContentUtil;
import cn.xldeng.common.toolkit.GroupKey;
import cn.xldeng.common.web.base.Result;
import cn.xldeng.starter.core.CacheData;
import cn.xldeng.starter.remote.HttpAgent;
import com.alibaba.fastjson.JSON;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.*;
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

    private long timeout;

    private boolean isHealthServer = true;

    private final HttpAgent httpAgent;

    private final ScheduledExecutorService executor;

    private final ScheduledExecutorService executorService;

    private final ConcurrentHashMap<String, CacheData> cacheMap = new ConcurrentHashMap<>(16);

    @SuppressWarnings("all")
    public ClientWorker(HttpAgent httpAgent) {
        this.httpAgent = httpAgent;
        this.timeout = Constants.CONFIG_LONG_POLL_TIMEOUT;

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
                log.error("[sub-check] rotate check error :{}", e.getMessage(), e);
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
                executorService.execute(new LongPollingRunnable());
            }
            currentLongingTaskCount = longingTaskCount;
        }
    }

    /**
     * 长轮训任务
     */
    class LongPollingRunnable implements Runnable {

        @SneakyThrows
        private void checkStatus(){
            if (!isHealthServer){
                log.error("[Check config] Error. exception message, Thread sleep 30 s.");
                Thread.sleep(30000);
            }
        }

        @Override
        @SneakyThrows
        public void run() {
            checkStatus();

            List<CacheData> cacheDataList = new ArrayList<>();
            List<CacheData> queryCacheDataList = new ArrayList<>(cacheMap.values());

            List<String> changedTpIds = checkUpdateDataIds(queryCacheDataList);
            if (!CollectionUtils.isEmpty(changedTpIds)) {
                /*
                 * 考虑是否加入日志
                 * log.info("[dynamic threadPool] tpIds changed :: {}", changedTpIds);
                 */
                for (String each : changedTpIds) {
                    String[] keys = StringUtils.split(each, Constants.GROUP_KEY_DELIMITER);
                    String namespace = keys[0];
                    String itemId = keys[1];
                    String tpId = keys[2];
                    try {
                        String content = getServerConfig(namespace, itemId, tpId, 3000L);
                        CacheData cacheData = cacheMap.get(tpId);
                        String poolContent = ContentUtil.getPoolContent(JSON.parseObject(content, PoolParameter.class));
                        cacheData.setContent(poolContent);
                        cacheDataList.add(cacheData);
                        /*
                         * 考虑是否加入日志
                         * log.info("[data-received] namespace :: {}, itemId :: {}, tpId :: {}, md5 :: {}", namespace, itemId, tpId, cacheData.getMd5());
                         */
                    } catch (Exception ex) {
                        //ignore
                    }
                }
                for (CacheData each : cacheDataList) {
                    each.checkListenerMd5();
                }
            }
            executorService.execute(this);
        }
    }

    private List<String> checkUpdateDataIds(List<CacheData> cacheDataList) {
        StringBuilder sb = new StringBuilder();
        for (CacheData cacheData : cacheDataList) {
            sb.append(cacheData.tpId).append(Constants.WORD_SEPARATOR);
            sb.append(cacheData.itemId).append(Constants.WORD_SEPARATOR);
            sb.append(cacheData.getMd5()).append(Constants.WORD_SEPARATOR);
            sb.append(cacheData.tenantId).append(Constants.LINE_SEPARATOR);
        }

        return checkUpdateTpIds(sb.toString());
    }

    /**
     * 检查修改的线程池 ID
     *
     * @param probeUpdateString probeUpdateString
     * @return changedTpIds
     */
    public List<String> checkUpdateTpIds(String probeUpdateString) {
        Map<String, String> params = new HashMap<>(2);
        params.put(Constants.PROBE_MODIFY_REQUEST, probeUpdateString);
        Map<String, String> headers = new HashMap<>(2);
        headers.put(Constants.LONG_PULLING_TIMEOUT, "" + timeout);

        if (StringUtils.isEmpty(probeUpdateString)) {
            return Collections.emptyList();
        }

        try {
            long readTimeout = timeout + (long) Math.round(timeout >> 1);
            Result result = httpAgent.httpPost(Constants.LISTENER_PATH, headers, params, readTimeout);
            if (result == null || result.isFail()) {
                setHealthServer(false);
                log.warn("[check-update] get changed dataId error, code: {}", result == null ? "error" : result.getCode());
            } else {
                setHealthServer(true);
                return parseUpdateDataIdResponse(result.getData().toString());
            }
        } catch (Exception ex) {
            setHealthServer(false);
            log.error("[check-update] get changed dataId exception.", ex);
        }
        return Collections.emptyList();
    }

    /**
     * Http 响应中获取变更的配置项
     * //fixme 为什么解析出response又要转成string的形式，为什么不直接封装成一个对象
     *
     * @param response response
     * @return configs
     */
    public List<String> parseUpdateDataIdResponse(String response) {
        if (StringUtils.isEmpty(response)) {
            return Collections.emptyList();
        }

        try {
            response = URLDecoder.decode(response, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            log.error("[polling-resp] decode modifiedDataIdsString error", e);
        }

        List<String> updateList = new LinkedList<>();
        for (String dataIdAndGroup : response.split(Constants.LINE_SEPARATOR)) {
            if (!StringUtils.isEmpty(dataIdAndGroup)) {
                String[] keyAttr = dataIdAndGroup.split(Constants.WORD_SEPARATOR);
                String dataId = keyAttr[0];
                String group = keyAttr[1];
                if (keyAttr.length == 2) {
                    updateList.add(GroupKey.getKey(dataId, group));
                    log.info("[polling-resp] config changed. dataId={}, group={}", dataId, group);
                } else if (keyAttr.length == 3) {
                    String tenant = keyAttr[2];
                    updateList.add(GroupKey.getKey(dataId, group, tenant));
                    log.info("[polling-resp] config changed. dataId={}, group={}, tenant={}", dataId, group, tenant);
                } else {
                    log.error("[polling-resp] invalid dataIdAndGroup error {}", dataIdAndGroup);
                }
            }
        }
        return updateList;
    }

    /**
     * 获取服务端配置
     *
     * @param namespace   namespace
     * @param itemId      itemId
     * @param tpId        tpId
     * @param readTimeout readTimeout
     * @return response
     */
    public String getServerConfig(String namespace, String itemId, String tpId, long readTimeout) {
        Map<String, String> params = new HashMap<>(3);
        params.put("namespace", namespace);
        params.put("itemId", itemId);
        params.put("tpId", tpId);

        Result result = httpAgent.httpGet(Constants.CONFIG_CONTROLLER_PATH, null, params, readTimeout);
        if (result.isSuccess()) {
            return result.getData().toString();
        }
        log.error("[sub-server-error] namespace :: {}, itemId :: {}, tpId :: {}, result code :: {}",
                namespace, itemId, tpId, result.getCode());
        return Constants.NULL;
    }

    public void addTenantListeners(String namespace, String itemId, String tpId, List<? extends Listener> listeners) {
        CacheData cacheData = addCacheDataIfAbsent(namespace, itemId, tpId);
        for (Listener listener : listeners) {
            cacheData.addListener(listener);
        }
    }

    public CacheData addCacheDataIfAbsent(String namespace, String itemId, String tpId) {
        CacheData cacheData = cacheMap.get(tpId);
        if (cacheData != null) {
            return cacheData;
        }

        cacheData = new CacheData(namespace, itemId, tpId);
        CacheData lastCacheData = cacheMap.putIfAbsent(tpId, cacheData);
        if (lastCacheData == null) {
            String serverConfig = getServerConfig(namespace, itemId, tpId, 3000L);
            PoolParameter poolInfo = JSON.parseObject(serverConfig, PoolParameter.class);
            cacheData.setContent(ContentUtil.getPoolContent(poolInfo));

            int taskId = cacheMap.size() / Constants.CONFIG_LONG_POLL_TIMEOUT;
            cacheData.setTaskId(taskId);
            lastCacheData = cacheData;
        }
        return lastCacheData;
    }

    public boolean isHealthServer() {
        return this.isHealthServer;
    }

    private void setHealthServer(boolean isHealthServer) {
        this.isHealthServer = isHealthServer;
    }
}