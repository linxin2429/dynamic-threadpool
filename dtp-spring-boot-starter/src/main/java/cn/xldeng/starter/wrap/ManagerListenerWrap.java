package cn.xldeng.starter.wrap;

import cn.xldeng.starter.listener.Listener;
import lombok.Getter;
import lombok.Setter;

/**
 * @program: threadpool
 * @description:
 * @author: dengxinlin
 * @create: 2022-06-28 23:02
 */
@Getter
@Setter
public class ManagerListenerWrap {
    final Listener listener;

    String lastCallMd5;

    public ManagerListenerWrap(String lastCallMd5, Listener listener ) {
        this.listener = listener;
        this.lastCallMd5 = lastCallMd5;
    }
}