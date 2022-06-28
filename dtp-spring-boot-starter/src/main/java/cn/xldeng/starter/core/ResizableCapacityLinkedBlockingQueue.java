package cn.xldeng.starter.core;

import cn.hutool.core.util.ReflectUtil;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.LinkedBlockingQueue;

/**
 * @program: threadpool
 * @description:
 * @author: dengxinlin
 * @create: 2022-06-27 07:33
 */
@Slf4j
public class ResizableCapacityLinkedBlockingQueue extends LinkedBlockingQueue {

    public ResizableCapacityLinkedBlockingQueue(int capacity) {
        super(capacity);
    }

    public boolean setCapacity(Integer capacity) {
        boolean successFlag = true;
        try {
            ReflectUtil.setFieldValue(this, "capacity", capacity);
        } catch (Exception e) {
            // ignore
            log.error("动态修改阻塞队列大小失败.", e);
            successFlag = false;
        }
        return successFlag;
    }
}