package com.atguigu.servicecmn;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * @author Guanghao Wei
 * @create 2023-03-07 15:38
 */
@SpringBootApplication(scanBasePackages = {"com.atguigu.yygh.common","com.atguigu.servicecmn"})
@EnableSwagger2
@MapperScan("com.atguigu.servicecmn.mapper")
@EnableCaching
@EnableDiscoveryClient
@EnableFeignClients
public class ServiceCmnMain8205 {
    public static void main(String[] args) {
        SpringApplication.run(ServiceCmnMain8205.class, args);
    }
}
