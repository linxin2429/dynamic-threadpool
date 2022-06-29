package cn.xldeng.server.notify.listener;

import cn.xldeng.server.event.Event;

import java.util.List;

/**
 * @program: threadpool
 * @description:
 * @author: dengxinlin
 * @create: 2022-06-29 16:32
 */
public abstract class SmartSubscriber extends Subscriber {

    public abstract List<Class<? extends Event>> subscribeTypes();
}