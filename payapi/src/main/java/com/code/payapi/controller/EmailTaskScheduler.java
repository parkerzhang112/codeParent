package com.code.payapi.controller;

import com.code.baseservice.service.EmailService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class EmailTaskScheduler {

    private final EmailService emailService;

    public EmailTaskScheduler(EmailService emailService) {
        this.emailService = emailService;
    }

    // 每隔5分钟检查一次邮件
    @Scheduled(fixedRate = 300000)
    public void monitorEmail() {
        emailService.checkEmails();
    }
}
