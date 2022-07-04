package cn.xldeng.starter.spi.reject;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.concurrent.RejectedExecutionHandler;

/**
 * 自定义拒绝策略
 *
 * @author dengxinlin
 * @date 2022/07/04 09:32:06
 */
public interface CustomRejectedExceptionHandler {

    /**
     * 生成拒绝策略
     *
     * @return {@link RejectedExceptionHandlerWrap}
     */
    RejectedExceptionHandlerWrap generateRejected();

    @Getter
    @Setter
    @AllArgsConstructor
    class RejectedExceptionHandlerWrap {
        private Integer type;

        private RejectedExecutionHandler rejectedExecutionHandler;
    }
}
