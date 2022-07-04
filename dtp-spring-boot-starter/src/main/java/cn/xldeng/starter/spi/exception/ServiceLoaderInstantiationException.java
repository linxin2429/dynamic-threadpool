package cn.xldeng.starter.spi.exception;

/**
 * 服务加载程序实例化异常
 *
 * @author dengxinlin
 * @date 2022/07/04 09:17:09
 */
public class ServiceLoaderInstantiationException extends RuntimeException {

    private static final long serialVersionUID = -7545046209615585780L;

    public ServiceLoaderInstantiationException(final Class<?> clazz, final Exception cause) {
        super(String.format("Can not find public default constructor for SPI class `%s`", clazz.getName()), cause);
    }
}