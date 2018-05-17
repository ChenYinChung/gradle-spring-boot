package com.sb.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;


@Configuration
public class SchedulerConfig {
    @Autowired
    ApplicationContext applicationContext;


}
