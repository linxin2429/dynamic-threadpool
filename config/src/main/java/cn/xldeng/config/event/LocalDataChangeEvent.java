package cn.xldeng.config.event;

/**
 * @program: threadpool
 * @description:
 * @author: dengxinlin
 * @create: 2022-06-29 16:30
 */
public class LocalDataChangeEvent extends Event {
    private static final long serialVersionUID = 6467584761461884799L;
    public final String groupKey;

    public LocalDataChangeEvent(String groupKey) {
        this.groupKey = groupKey;
    }
}