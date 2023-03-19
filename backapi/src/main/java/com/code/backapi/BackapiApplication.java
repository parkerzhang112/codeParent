package com.code.backapi;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@EnableSwagger2
@MapperScan("com.xiagao.baseservice.dao")
@ComponentScan({"com.xiagao.baseservice", "com.xiagao.backapi"})
@SpringBootApplication
public class BackapiApplication {
    public static void main(String[] args) {
        SpringApplication.run(BackapiApplication.class, args);
    }
}
