package com.atguigu.servicehosp;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * @author Guanghao Wei
 * @create 2023-03-06 15:49
 */
@SpringBootApplication(scanBasePackages = {"com.atguigu.yygh.common","com.atguigu.servicehosp"})
@EnableSwagger2
@MapperScan("com.atguigu.servicehosp.mapper")
@EnableDiscoveryClient
@EnableFeignClients
public class ServiceHospMain8201 {
    public static void main(String[] args) {
        SpringApplication.run(ServiceHospMain8201.class, args);
    }
}
