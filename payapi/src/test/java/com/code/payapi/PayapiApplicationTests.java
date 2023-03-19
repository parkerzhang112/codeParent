package com.code.payapi;

import org.junit.runner.RunWith;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest()
@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
@MapperScan("com.xiagao.baseservice.dao")
@ComponentScan({"com.xiagao.baseservice", "com.xiagao.payapi"})

class PayapiApplicationTests {


}
