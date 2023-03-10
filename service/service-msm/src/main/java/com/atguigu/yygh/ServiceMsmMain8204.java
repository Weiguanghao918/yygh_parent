package com.atguigu.yygh;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * @author Guanghao Wei
 * @create 2023-03-10 15:14
 */
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
@EnableSwagger2
@EnableDiscoveryClient
@EnableFeignClients
public class ServiceMsmMain8204 {
    public static void main(String[] args) {
        SpringApplication.run(ServiceMsmMain8204.class, args);
    }
}
