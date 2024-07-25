package com.code.frontapi.config;

import com.code.frontapi.annotation.CustomStatementInspector;
import org.hibernate.resource.jdbc.spi.StatementInspector;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class HibernateConfig {

    @Bean
    public StatementInspector hibernateStatementInspector() {
        return new CustomStatementInspector();
    }
}
