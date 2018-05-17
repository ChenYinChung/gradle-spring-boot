package com.sb.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * @Auther: sammy
 * <p>
 * IOC 設置Controller ,註冊自行開的Inteceptor
 *
 * 實作在 ExecutionIntervalInterceptor
 * ExecutionInterval Annotation 必需要controller 才有效
 */

@Configuration
public class ExectionIntervalInterceptorRegister extends WebMvcConfigurerAdapter {


    @Bean
    public ExecutionIntervalInterceptor executionIntervalInterceptor() {
        return new ExecutionIntervalInterceptor();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(executionIntervalInterceptor());
    }
}