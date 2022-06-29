package cn.xldeng.server.notify;

import cn.xldeng.server.event.Event;
import cn.xldeng.server.notify.listener.Subscriber;

/**
 * @program: threadpool
 * @description:
 * @author: dengxinlin
 * @create: 2022-06-29 16:31
 */
public interface EventPublisher {

    void init(Class<? extends Event> type, int bufferSize);

    /**
     * 添加订阅
     *
     * @param subscriber 订阅者
     */
    void addSubscriber(Subscriber subscriber);

    boolean publish(Event event);

    void notifySubscriber(Subscriber subscriber, Event event);
}