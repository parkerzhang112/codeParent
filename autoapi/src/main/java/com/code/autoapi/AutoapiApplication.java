package com.code.autoapi;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.ComponentScan;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@EnableSwagger2
@SpringBootApplication
@MapperScan("com.code.baseservice.dao")
@ComponentScan({"com.code.baseservice", "com.code.autoapi"})
@ServletComponentScan
public class AutoapiApplication {

    public static void main(String[] args) {
        SpringApplication.run(AutoapiApplication.class, args);
    }

}
