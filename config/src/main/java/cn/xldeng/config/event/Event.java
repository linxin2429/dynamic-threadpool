package cn.xldeng.config.event;

import java.io.Serializable;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @program: threadpool
 * @description:
 * @author: dengxinlin
 * @create: 2022-06-29 16:31
 */
public abstract class Event implements Serializable {

    private static final long serialVersionUID = 5714774595598837060L;

    private static final AtomicLong SEQUENCE = new AtomicLong(0);

    private final long sequence = SEQUENCE.getAndIncrement();

    /**
     * Event sequence number, which can be used to handle the sequence of events.
     *
     * @return sequence num, It's best to make sure it's monotone.
     */
    public long sequence() {
        return sequence;
    }
}