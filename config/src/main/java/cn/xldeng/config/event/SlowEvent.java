package cn.xldeng.config.event;

/**
 * @program: threadpool
 * @description:
 * @author: dengxinlin
 * @create: 2022-06-29 16:31
 */
public abstract class SlowEvent extends Event {
    private static final long serialVersionUID = -2025039582518061368L;

    @Override
    public long sequence() {
        return 0;
    }
}