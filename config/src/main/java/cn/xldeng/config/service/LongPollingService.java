package cn.xldeng.config.service;

import cn.xldeng.common.web.base.Results;
import cn.xldeng.config.event.Event;
import cn.xldeng.config.event.LocalDataChangeEvent;
import cn.xldeng.config.notify.NotifyCenter;
import cn.xldeng.config.notify.listener.Subscriber;
import cn.xldeng.config.toolkit.ConfigExecutor;
import cn.xldeng.config.toolkit.Md5ConfigUtil;
import cn.xldeng.config.toolkit.RequestUtil;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.servlet.AsyncContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

/**
 * @program: threadpool
 * @description:
 * @author: dengxinlin
 * @create: 2022-06-29 16:33
 */
@Service
@Slf4j
public class LongPollingService {

    private static final int FIXED_POLLING_INTERVAL_MS = 10000;

    public static final String LONG_POLLING_HEADER = "Long-Pulling-Timeout";

    public static final String LONG_POLLING_NO_HANG_UP_HEADER = "Long-Pulling-Timeout-No-Hangup";

    public static final String CLIENT_APPNAME_HEADER = "Client-AppName";

    private Map<String, Long> retainIps = new ConcurrentHashMap();

    final Queue<ClientLongPolling> allSubs;

    public LongPollingService() {
        allSubs = new ConcurrentLinkedQueue<>();

        ConfigExecutor.scheduleLongPolling(new StatTask(), 0L, 10L, TimeUnit.SECONDS);

        NotifyCenter.registerToPublisher(LocalDataChangeEvent.class, NotifyCenter.ringBufferSize);

        NotifyCenter.registerSubscriber(new Subscriber() {
            @Override
            public void onEvent(Event event) {
                if (isFixedPolling()) {
                    //Ignore
                } else {
                    if (event instanceof LocalDataChangeEvent) {
                        LocalDataChangeEvent evt = (LocalDataChangeEvent) event;
                        ConfigExecutor.executeLongPolling(new DataChangeTask(evt.groupKey));
                    }
                }
            }

            @Override
            public Class<? extends Event> subscribeType() {
                return LocalDataChangeEvent.class;
            }
        });
    }

    class StatTask implements Runnable {

        @Override
        public void run() {
            log.info("[long-pulling] client count " + allSubs.size());
        }
    }

    class DataChangeTask implements Runnable {
        final String groupKey;


        public DataChangeTask(String groupKey) {
            this.groupKey = groupKey;
        }

        @Override
        public void run() {
            try {
                for (Iterator<ClientLongPolling> iter = allSubs.iterator(); iter.hasNext(); ) {
                    ClientLongPolling clientSub = iter.next();
                    if (clientSub.clientMd5Map.containsKey(groupKey)) {
                        getRetainIps().put(clientSub.ip, System.currentTimeMillis());
                        ConfigCacheService.updateMd5(groupKey, ConfigCacheService.getContentMd5(groupKey), System.currentTimeMillis());
                        iter.remove();
                        clientSub.sendResponse(Collections.singletonList(groupKey));
                    }

                }
            } catch (Exception ex) {
                log.error("Data change error :: {}", ex.getMessage(), ex);
            }
        }
    }

    public static boolean isSupportLongPolling(HttpServletRequest request) {
        return null != request.getHeader(LONG_POLLING_HEADER);
    }

    private static boolean isFixedPolling() {
        return SwitchService.getSwitchBoolean(SwitchService.FIXED_POLLING, false);
    }

    private static int getFixedPollingInterval() {
        return SwitchService.getSwitchInteger(SwitchService.FIXED_POLLING_INTERVAL, FIXED_POLLING_INTERVAL_MS);
    }

    public void addLongPollingClient(HttpServletRequest req,
                                     HttpServletResponse rsp,
                                     Map<String, String> clientMd5Map,
                                     int probeRequestSize) {
        String str = req.getHeader(LONG_POLLING_HEADER);
        String appName = req.getHeader(CLIENT_APPNAME_HEADER);
        int delayTime = SwitchService.getSwitchInteger(SwitchService.FIXED_DELAY_TIME, 500);

        long timeout = Math.max(10000, Long.parseLong(str) - delayTime);
        if (isFixedPolling()) {
            timeout = Math.max(10000, getFixedPollingInterval());
        } else {
            List<String> changedGroups = Md5ConfigUtil.compareMd5(req, clientMd5Map);
            if (changedGroups.size() > 0) {
                generateResponse(rsp, changedGroups);
                return;
            }
        }

        String ip = RequestUtil.getRemoteIp(req);

        final AsyncContext asyncContext = req.startAsync();
        asyncContext.setTimeout(0L);

        ConfigExecutor.executeLongPolling(new ClientLongPolling(asyncContext, clientMd5Map, ip, probeRequestSize, timeout, appName));
    }

    class ClientLongPolling implements Runnable {
        final AsyncContext asyncContext;

        final Map<String, String> clientMd5Map;

        final long createTime;

        final String ip;

        final String appName;

        final int probeRequestSize;

        final long timeoutTime;

        Future<?> asyncTimeoutFuture;

        public ClientLongPolling(AsyncContext asyncContext,
                                 Map<String, String> clientMd5Map,
                                 String ip,
                                 int probeRequestSize,
                                 long timeout,
                                 String appName) {
            this.asyncContext = asyncContext;
            this.clientMd5Map = clientMd5Map;
            this.ip = ip;
            this.probeRequestSize = probeRequestSize;
            this.timeoutTime = timeout;
            this.appName = appName;
            this.createTime = System.currentTimeMillis();
        }

        @Override
        public void run() {
            ConfigExecutor.scheduleLongPolling(() -> {
                try {
                    getRetainIps().put(ClientLongPolling.this.ip, System.currentTimeMillis());
                    allSubs.remove(ClientLongPolling.this);
                    if (isFixedPolling()) {
                        List<String> changedGroups = Md5ConfigUtil.compareMd5((HttpServletRequest) asyncContext.getRequest(), clientMd5Map);
                        if (changedGroups.size() > 0) {
                            sendResponse(changedGroups);
                        } else {
                            sendResponse(null);
                        }
                    } else {
                        sendResponse(null);
                    }
                } catch (Exception ex) {
                    log.error("Long polling error :: {}", ex.getMessage(), ex);
                }
            }, timeoutTime, TimeUnit.MILLISECONDS);
            allSubs.add(this);
        }

        private void sendResponse(List<String> changedGroups) {
            if (null != asyncContext) {
                asyncTimeoutFuture.cancel(false);
            }
            generateResponse(changedGroups);
        }

        private void generateResponse(List<String> changedGroups) {
            if (null == changedGroups) {
                asyncContext.complete();
                return;
            }
            HttpServletResponse response = (HttpServletResponse) asyncContext.getResponse();
            try {
                String respStr = Md5ConfigUtil.compareMd5ResultString(changedGroups);
                String resultStr = JSON.toJSONString(Results.success(respStr));

                // Disable cache.
                response.setHeader("Pragma", "no-cache");
                response.setDateHeader("Expires", 0);
                response.setHeader("Cache-Control", "no-cache,no-store");
                response.setStatus(HttpServletResponse.SC_OK);
                response.getWriter().println(resultStr);
                asyncContext.complete();
            } catch (Exception ex) {
                log.error(ex.toString(), ex);
                asyncContext.complete();
            }
        }
    }

    public Map<String, Long> getRetainIps() {
        return retainIps;
    }

    /**
     * 回写响应
     *
     * @param response      response
     * @param changedGroups changedGroups
     */
    private void generateResponse(HttpServletResponse response, List<String> changedGroups) {
        if (!CollectionUtils.isEmpty(changedGroups)) {
            try {
                final String changedGroupKeStr = Md5ConfigUtil.compareMd5ResultString(changedGroups);
                final String respString = JSON.toJSONString(Results.success(changedGroupKeStr));
                response.setHeader("Pragma", "no-cache");
                response.setDateHeader("Expires", 0);
                response.setHeader("Cache-Control", "no-cache,no-store");
                response.setStatus(HttpServletResponse.SC_OK);
                response.getWriter().println(respString);
            } catch (Exception ex) {
                log.error(ex.toString(), ex);
            }
        }
    }
}