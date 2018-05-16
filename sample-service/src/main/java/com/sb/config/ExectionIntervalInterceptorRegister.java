package com.sb.config;


import com.sb.annotation.ExecutionIntervalInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

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