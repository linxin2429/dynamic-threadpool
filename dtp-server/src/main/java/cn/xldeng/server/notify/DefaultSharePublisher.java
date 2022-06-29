package cn.xldeng.server.notify;

import cn.hutool.core.collection.ConcurrentHashSet;
import cn.xldeng.server.event.Event;
import cn.xldeng.server.event.SlowEvent;
import cn.xldeng.server.notify.listener.Subscriber;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @program: threadpool
 * @description:
 * @author: dengxinlin
 * @create: 2022-06-29 16:31
 */
public class DefaultSharePublisher extends DefaultPublisher{
    private final Map<Class<? extends SlowEvent>, Set<Subscriber>> subMappings = new ConcurrentHashMap<>();

    protected final ConcurrentHashSet<Subscriber> subscribers = new ConcurrentHashSet<>();

    private final Lock lock = new ReentrantLock();

    public void addSubscriber(Subscriber subscriber, Class<? extends Event> subscribeType) {
        Class<? extends SlowEvent> subSlowEventType = (Class<? extends SlowEvent>) subscribeType;
        subscribers.add(subscriber);
        lock.lock();
        try {
            Set<Subscriber> sets = subMappings.get(subSlowEventType);
            if (sets == null) {
                Set<Subscriber> newSet = new ConcurrentHashSet<>();
                newSet.add(subscriber);
                subMappings.put(subSlowEventType, newSet);
                return;
            }
            sets.add(subscriber);
        } finally {
            lock.unlock();
        }
    }
}