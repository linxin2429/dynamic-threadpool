package cn.xldeng.common.web.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @program: threadpool
 * @description:
 * @author: dengxinlin
 * @create: 2022-06-29 12:12
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class ServiceException extends RuntimeException {

    private static final long serialVersionUID = -7388872015011590642L;

    private String detail;

    public ServiceException(String message) {
        super(message);
    }

    public ServiceException(String message, String detail) {
        super(message);
        this.detail = detail;
    }

    public ServiceException(String message, Throwable cause) {
        super(message, cause);
        this.detail = cause.getMessage();
    }

    public ServiceException(String message, String detail, Throwable cause) {
        super(message, cause);
        this.detail = detail;
    }

    @Override
    public String toString() {
        return "ServiceException{" +
                "message='" + getMessage() + "'," +
                "detail='" + detail + "'" +
                '}';
    }
}