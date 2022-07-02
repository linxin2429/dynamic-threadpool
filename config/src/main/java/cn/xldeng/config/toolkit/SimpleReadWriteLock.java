package cn.xldeng.config.toolkit;

/**
 * @program: threadpool
 * @description:
 * @author: dengxinlin
 * @create: 2022-06-29 21:55
 */
public class SimpleReadWriteLock {
    /**
     * Zero means no lock; Negative Numbers mean write locks; Positive Numbers mean read locks, and the numeric value
     * represents the number of read locks.
     */
    private int status = 0;

    public synchronized boolean tryReadLock() {
        if (isWriteLocked()) {
            return false;
        } else {
            status++;
            return true;
        }
    }

    public synchronized void releaseReadLock() {
        status--;
    }

    public synchronized boolean tryWriteLock() {
        if (!isFree()) {
            return false;
        } else {
            status = -1;
            return true;
        }
    }

    public synchronized void releaseWriteLock() {
        status = 0;
    }

    private boolean isFree() {
        return status == 0;
    }

    private boolean isWriteLocked() {
        return status < 0;
    }
}