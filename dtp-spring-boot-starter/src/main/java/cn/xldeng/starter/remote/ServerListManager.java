package cn.xldeng.starter.remote;

import cn.xldeng.starter.config.DynamicThreadPoolProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import java.util.*;

/**
 * @program: threadpool
 * @description:
 * @author: dengxinlin
 * @create: 2022-06-29 01:22
 */
@Slf4j
public class ServerListManager {

    private static final String HTTPS = "https://";

    private static final String HTTP = "http://";

    private String serverAddrsStr;

    volatile List<String> serverUrls = new ArrayList<>();

    private volatile String currentServerAddr;

    private Iterator<String> iterator;

    private final DynamicThreadPoolProperties properties;

    public ServerListManager(DynamicThreadPoolProperties properties) {
        this.properties = properties;
        this.serverAddrsStr = properties.getServerAddr();

        if (!StringUtils.isEmpty(serverAddrsStr)) {
            List<String> serverAddrs = new ArrayList<>();
            String[] serverAddrArr = this.serverAddrsStr.split(",");
            for (String serverAddr : serverAddrArr) {
                if (serverAddr.startsWith(HTTPS) || serverAddr.startsWith(HTTP)) {
                    this.currentServerAddr = serverAddr;
                    serverAddrs.add(serverAddr);
                }
            }
            this.serverUrls = serverAddrs;
        }
    }

    public String getCurrentServerAddr() {
        if (StringUtils.isEmpty(currentServerAddr)) {
            iterator = iterator();
            currentServerAddr = iterator.next();
        }
        return currentServerAddr;
    }

    Iterator<String> iterator() {
        if (serverUrls.isEmpty()) {
            log.error("[iterator-serverlist] No server address defined!");
        }
        return new ServerAddressIterator(serverUrls);
    }

    private static class ServerAddressIterator implements Iterator<String> {

        final List<RandomizedServerAddress> sorted;

        final Iterator<RandomizedServerAddress> iter;

        public ServerAddressIterator(List<String> source) {
            sorted = new ArrayList<>();
            for (String address : source) {
                sorted.add(new RandomizedServerAddress(address));
            }
            Collections.sort(sorted);
            iter = sorted.iterator();
        }

        @Override

        public boolean hasNext() {
            return false;
        }

        @Override
        public String next() {
            return null;
        }

        static class RandomizedServerAddress implements Comparable<RandomizedServerAddress> {

            static Random random = new Random();

            String serverIp;

            int priority = 0;

            int seed;

            public RandomizedServerAddress(String ip) {
                try {
                    this.serverIp = ip;
                    this.seed = random.nextInt(Integer.MAX_VALUE);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }

            @Override
            public int compareTo(RandomizedServerAddress o) {
                if (this.priority != o.priority) {
                    return o.priority - this.priority;
                } else {
                    return o.seed - this.seed;
                }
            }
        }
    }
}