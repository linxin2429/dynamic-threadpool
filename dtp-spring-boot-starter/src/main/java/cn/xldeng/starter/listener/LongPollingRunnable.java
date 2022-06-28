package cn.xldeng.starter.listener;

/**
 * @program: threadpool
 * @description: 长轮询执行
 * @author: dengxinlin
 * @create: 2022-06-28 18:05
 */
public class LongPollingRunnable implements Runnable{

    private final int taskId;

    public LongPollingRunnable(int taskId) {
        this.taskId = taskId;
    }

    @Override
    public void run() {

    }
}