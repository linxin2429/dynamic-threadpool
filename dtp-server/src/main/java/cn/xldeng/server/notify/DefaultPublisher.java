package cn.xldeng.server.notify;

import cn.hutool.core.collection.ConcurrentHashSet;
import cn.xldeng.server.event.Event;
import cn.xldeng.server.notify.listener.Subscriber;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.atomic.AtomicReferenceFieldUpdater;

/**
 * @program: threadpool
 * @description:
 * @author: dengxinlin
 * @create: 2022-06-29 16:30
 */
@Slf4j
public class DefaultPublisher extends Thread implements EventPublisher {
    protected final ConcurrentHashSet<Subscriber> subscribers = new ConcurrentHashSet<>();

    private BlockingQueue<Event> queue;

    private volatile boolean initialized = false;

    private volatile boolean shutdown = false;

    private int queueMaxSize = -1;

    protected volatile Long lastEventSequence = -1L;

    private static final AtomicReferenceFieldUpdater<DefaultPublisher, Long> UPDATER = AtomicReferenceFieldUpdater
            .newUpdater(DefaultPublisher.class, Long.class, "lastEventSequence");


    @Override
    public void init(Class<? extends Event> type, int bufferSize) {
        setDaemon(true);
        setName("dynamic.thread-pool.publisher-" + type.getName());
        this.queueMaxSize = bufferSize;
        this.queue = new ArrayBlockingQueue<>(bufferSize);
        start();
    }

    @Override
    public synchronized void start() {
        if (!initialized) {
            super.start();
            if (queueMaxSize == -1) {
                queueMaxSize = NotifyCenter.ringBufferSize;
            }
            initialized = true;
        }
    }

    @Override
    public void run() {
        openEventHandler();
    }

    private void openEventHandler() {
        try {
            int waitTimes = 60;
            for (; ; ) {
                if (shutdown || hasSubscriber() || waitTimes <= 0) {
                    break;
                }
                try {
                    Thread.sleep(1000L);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
                waitTimes--;
            }

            for (; ; ) {
                if (shutdown) {
                    break;
                }
                final Event event = queue.take();
                receiveEvent(event);
                UPDATER.compareAndSet(this, lastEventSequence, Math.max(lastEventSequence, event.sequence()));
            }
        } catch (Exception e) {
            log.error("Event listener exception : ", e);
        }
    }

    private boolean hasSubscriber() {
        return !CollectionUtils.isEmpty(subscribers);
    }

    void receiveEvent(Event event) {
        for (Subscriber subscriber : subscribers) {
            notifySubscriber(subscriber, event);
        }
    }

    @Override
    public void addSubscriber(Subscriber subscriber) {
        subscribers.add(subscriber);
    }

    @Override
    public boolean publish(Event event) {
        boolean success = this.queue.offer(event);
        if (!success) {
            log.warn("Unable to plug in due to interruption, synchronize sending time, event : {}", event);
            receiveEvent(event);
            return true;
        }
        return true;
    }

    @Override
    public void notifySubscriber(Subscriber subscriber, Event event) {
        final Runnable job = ()->subscriber.onEvent(event);
        final Executor executor = subscriber.executor();
        if (executor != null ){
            executor.execute(job);
        }else {
            try {
                job.run();
            }catch (Throwable e){
                log.error("Event callback exception : ", e);
            }
        }
    }
}