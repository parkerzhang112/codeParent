package com.code.frontapi.config;

import com.code.frontapi.annotation.AuthHandlerInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class AuthWebMvcConfigurer implements WebMvcConfigurer {

    @Autowired
    private AuthHandlerInterceptor authHandlerInterceptor;

    /**
     * 配置拦截器，排除 /index/** 路径
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(authHandlerInterceptor)
                .addPathPatterns("/**")          // 拦截所有路径
                .excludePathPatterns("/index/**"); // 排除 /index/** 路径

        System.out.println("Interceptor registered"); // 添加日志
    }
}
