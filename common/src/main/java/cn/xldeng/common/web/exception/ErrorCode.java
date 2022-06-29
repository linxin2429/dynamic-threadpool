package cn.xldeng.common.web.exception;

/**
 * @program: threadpool
 * @description:
 * @author: dengxinlin
 * @create: 2022-06-29 12:09
 */
public enum ErrorCode {
    /**
     * 未知错误
     */
    UNKNOWN_ERROR {
        @Override
        public String getCode() {
            return "1";
        }

        @Override
        public String getMessage() {
            return "未知错误";
        }
    },
    /**
     * 参数错误
     */
    VALIDATION_ERROR {
        @Override
        public String getCode() {
            return "2";
        }

        @Override
        public String getMessage() {
            return "参数错误";
        }
    },
    /**
     * 服务异常
     */
    SERVICE_ERROR {
        @Override
        public String getCode() {
            return "3";
        }

        @Override
        public String getMessage() {
            return "服务异常";
        }
    };

    public abstract String getCode();

    public abstract String getMessage();
}
