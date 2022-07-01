package cn.xldeng.starter.builder;


import java.io.Serializable;


/**
 * 构建器
 *
 * @author dengxinlin
 * @date 2022/07/01 09:17:16
 */
public interface Builder<T> extends Serializable {
    /**
     * 构建
     *
     * @return 被构建的对象
     */
    T build();
}