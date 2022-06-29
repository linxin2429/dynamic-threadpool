package cn.xldeng.common.web.base;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @program: threadpool
 * @description:
 * @author: dengxinlin
 * @create: 2022-06-29 11:58
 */
@Data
@Accessors(chain = true)
public class Result<T> implements Serializable {

    private static final long serialVersionUID = -4390579491217625136L;

    public static final String SUCCESS_CODE = "0";

    private String code;

    private String message;

    private T data;

    public boolean isSuccess() {
        return SUCCESS_CODE.equals(code);
    }

    public boolean isFail() {
        return !isSuccess();
    }
}