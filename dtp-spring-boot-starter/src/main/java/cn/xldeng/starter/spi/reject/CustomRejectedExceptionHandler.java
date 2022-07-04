package cn.xldeng.starter.spi.reject;

import java.util.concurrent.RejectedExecutionHandler;

/**
 * 自定义拒绝策略
 *
 * @author dengxinlin
 * @date 2022/07/04 09:32:06
 */
public interface CustomRejectedExceptionHandler {

    /**
     * 获取类型
     *
     * @return {@link Integer}
     */
    Integer getType();

    /**
     * 生成拒绝策略
     *
     * @return {@link RejectedExecutionHandler}
     */
    RejectedExecutionHandler generateRejected();


}
