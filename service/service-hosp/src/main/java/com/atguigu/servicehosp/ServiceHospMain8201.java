package com.atguigu.servicehosp;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * @author Guanghao Wei
 * @create 2023-03-06 15:49
 */
@SpringBootApplication
@EnableSwagger2
@MapperScan("com.atguigu.servicehosp.mapper")
public class ServiceHospMain8201 {
    public static void main(String[] args) {
        SpringApplication.run(ServiceHospMain8201.class, args);
    }
}
