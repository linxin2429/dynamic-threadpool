package cn.xldeng.config.notify.listener;

import cn.xldeng.config.event.Event;

import java.util.concurrent.Executor;

/**
 * @program: threadpool
 * @description:
 * @author: dengxinlin
 * @create: 2022-06-29 16:32
 */
public abstract class Subscriber<T extends Event> {
    /**
     * Event callback.
     *
     * @param event event
     */
    public abstract void onEvent(T event);

    /**
     * Type of this subscriber's subscription.
     *
     * @return subscriber's type
     */
    public abstract Class<? extends Event> subscribeType();

    public Executor executor() {
        return null;
    }
}