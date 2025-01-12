package com.code.payapi;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

//@EnableSwagger2
@MapperScan("com.code.baseservice.dao")
@ComponentScan({"com.code.baseservice", "com.code.payapi"})
@SpringBootApplication
@EnableScheduling
public class PayapiApplication {

    public static void main(String[] args) {
        SpringApplication.run(PayapiApplication.class, args);
    }

}
