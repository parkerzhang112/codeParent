package com.code.baseservice.base.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.mail.Session;
import java.util.Properties;

@Configuration
public class EmailConfig {

    @Bean
    public Session mailSession() {


        Properties props = new Properties();
        props.setProperty("mail.store.protocol", "imaps");
        props.setProperty("mail.imap.host", "imap.163.com");
        props.setProperty("mail.imap.port", "993");
        return Session.getInstance(props);
    }
}
