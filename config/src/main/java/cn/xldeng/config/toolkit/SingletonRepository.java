package cn.xldeng.config.toolkit;

import java.util.concurrent.ConcurrentHashMap;

/**
 * @program: threadpool
 * @description:
 * @author: dengxinlin
 * @create: 2022-06-29 22:00
 */
public class SingletonRepository<T> {
    private final ConcurrentHashMap<T, T> shared;

    public SingletonRepository() {
        // Initializing size 2^16, the container itself use about 50K of memory, avoiding constant expansion
        shared = new ConcurrentHashMap<>(1 << 16);
    }

    public T getSingleton(T obj) {
        T previous = shared.putIfAbsent(obj, obj);
        return (null == previous) ? obj : previous;
    }

    public int size() {
        return shared.size();
    }

    public void remove(Object obj) {
        shared.remove(obj);
    }

    public static class DataIdGroupIdCache {
        public static String getSingleton(String str) {
            return cache.getSingleton(str);
        }

        static SingletonRepository<String> cache = new SingletonRepository<>();
    }
}