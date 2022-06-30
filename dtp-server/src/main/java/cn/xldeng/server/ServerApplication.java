package cn.xldeng.server;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @program: threadpool
 * @description:
 * @author: dengxinlin
 * @create: 2022-06-27 01:28
 */
@SpringBootApplication(
        scanBasePackages = {
                "cn.xldeng.server", "cn.xldeng.common.config"
        }
)
@MapperScan("cn.xldeng.server.mapper")
public class ServerApplication {
    public static void main(String[] args) {
        SpringApplication.run(ServerApplication.class, args);
    }
}