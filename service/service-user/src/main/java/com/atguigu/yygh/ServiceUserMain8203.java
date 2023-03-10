package com.atguigu.yygh;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * @author Guanghao Wei
 * @create 2023-03-10 14:23
 */
@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
@EnableSwagger2
@MapperScan("com.atguigu.yygh.mapper")
public class ServiceUserMain8203 {
    public static void main(String[] args) {
        SpringApplication.run(ServiceUserMain8203.class, args);
    }
}
