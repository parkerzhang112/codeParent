package com.code.autoapi;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.junit4.SpringRunner;


@SpringBootTest()
@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
@MapperScan("com.code.baseservice.dao")
@ComponentScan({"com.code.baseservice", "com.codes.autoapi"})
public class AutoapiApplicationTests {


}
