package cn.xldeng.server.notify;

import cn.xldeng.server.event.Event;
import cn.xldeng.server.event.SlowEvent;
import cn.xldeng.server.notify.listener.SmartSubscriber;
import cn.xldeng.server.notify.listener.Subscriber;
import cn.xldeng.server.toolkit.ClassUtil;
import cn.xldeng.server.toolkit.MapUtil;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BiFunction;

/**
 * @program: threadpool
 * @description:
 * @author: dengxinlin
 * @create: 2022-06-29 16:31
 */
@Slf4j
public class NotifyCenter {
    private static final NotifyCenter INSTANCE = new NotifyCenter();

    public static int ringBufferSize = 16384;

    public static int shareBufferSize = 1024;

    private static Class<? extends EventPublisher> clazz = null;

    private static EventPublisher eventPublisher = new DefaultPublisher();

    private DefaultSharePublisher sharePublisher;

    private static BiFunction<Class<? extends Event>, Integer, EventPublisher> publisherFactory = null;

    private final Map<String, EventPublisher> publisherMap = new ConcurrentHashMap<>(16);

    static {
        publisherFactory = (cls, buffer) -> {
            try {
                EventPublisher publisher = eventPublisher;
                publisher.init(cls, buffer);
                return publisher;
            } catch (Throwable ex) {
                log.error("Service class newInstance has error : {}",ex.getMessage(), ex);
                throw new RuntimeException(ex);
            }
        };

        INSTANCE.sharePublisher = new DefaultSharePublisher();
        INSTANCE.sharePublisher.init(SlowEvent.class, shareBufferSize);
    }

    public static void registerSubscriber(final Subscriber consumer) {
        if (consumer instanceof SmartSubscriber) {
            for (Class<? extends Event> subscribeType : ((SmartSubscriber) consumer).subscribeTypes()) {
                if (ClassUtil.isAssignableFrom(SlowEvent.class, subscribeType)) {
                    INSTANCE.sharePublisher.addSubscriber(consumer, subscribeType);
                } else {
                    addSubscriber(consumer, subscribeType);
                }
            }
            return;
        }
        final Class<? extends Event> subscribeType = consumer.subscribeType();
        if (ClassUtil.isAssignableFrom(SlowEvent.class, subscribeType)) {
            INSTANCE.sharePublisher.addSubscriber(consumer, subscribeType);
            return;
        }
        addSubscriber(consumer, subscribeType);
    }

    private static void addSubscriber(final Subscriber comsumer, Class<? extends Event> subscribeType) {
        final String topic = ClassUtil.getCanonicalName(subscribeType);
        synchronized (NotifyCenter.class) {
            MapUtil.computeIfAbsent(INSTANCE.publisherMap, topic, publisherFactory, subscribeType, ringBufferSize);
        }
        EventPublisher publisher = INSTANCE.publisherMap.get(topic);
        publisher.addSubscriber(comsumer);
    }

    public static boolean publishEvent(final Event event) {
        try {
            return publishEvent(event.getClass(), event);
        } catch (Throwable ex) {
            log.error("There was an exception to the message publishing : {}",ex.getMessage(), ex);
            return false;
        }
    }

    private static boolean publishEvent(final Class<? extends Event> eventType, final Event event) {
        if (ClassUtil.isAssignableFrom(SlowEvent.class, eventType)) {
            return INSTANCE.sharePublisher.publish(event);
        }

        final String topic = ClassUtil.getCanonicalName(eventType);

        EventPublisher publisher = INSTANCE.publisherMap.get(topic);
        if (publisher != null) {
            return publisher.publish(event);
        }
        log.warn("There are no [{}] publishers for this event, please register", topic);
        return false;
    }

    public static EventPublisher registerToPublisher(final Class<? extends Event> eventType, final int queueMaxSize){
        if (ClassUtil.isAssignableFrom(SlowEvent.class,eventType)){
            return INSTANCE.sharePublisher;
        }

        final String topic = ClassUtil.getCanonicalName(eventType);
        synchronized (NotifyCenter.class) {
            MapUtil.computeIfAbsent(INSTANCE.publisherMap, topic, publisherFactory, eventType, queueMaxSize);
        }
        return INSTANCE.publisherMap.get(topic);
    }
}