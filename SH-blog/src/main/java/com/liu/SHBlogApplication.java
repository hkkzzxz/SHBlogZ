package com.liu;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.scheduling.annotation.EnableScheduling;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@MapperScan("com.liu.mapper")
@EnableConfigurationProperties
@EnableScheduling
@EnableSwagger2
public class SHBlogApplication {
    public static void main(String[] args) {
        SpringApplication.run(SHBlogApplication.class,args);
    }

}
