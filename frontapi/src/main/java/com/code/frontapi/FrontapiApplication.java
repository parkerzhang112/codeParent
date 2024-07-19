package com.code.frontapi;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.ComponentScan;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@EnableSwagger2
@SpringBootApplication
@MapperScan("com.code.baseservice.dao")
@ComponentScan({"com.code.baseservice", "com.code.frontapi"})
@ServletComponentScan
public class FrontapiApplication {

    public static void main(String[] args) {
        SpringApplication.run(FrontapiApplication.class, args);
    }

}
