package com.atguigu.yygh.common.config;

import com.github.xiaoymin.knife4j.spring.annotations.EnableKnife4j;
import com.google.common.base.Predicate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.RequestHandler;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;


/**
 * @author Guanghao Wei
 * @create 2023-03-06 15:22
 */
@Configuration
@EnableSwagger2
@EnableKnife4j
@Slf4j
public class SwaggerConfig {
    @Bean
    public Docket docket() {

        // 扫描指定接口所在路径
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()
                .apis((Predicate<RequestHandler>) RequestHandlerSelectors.basePackage("com.example.demo.controller"))
                .paths((Predicate<String>) PathSelectors.any())
                .build();
    }

    // swagger 信息
    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("利用swagger2构建的API文档")
                .description("用restful风格写接口")
                .version("1.0")
                .build();
    }
}


