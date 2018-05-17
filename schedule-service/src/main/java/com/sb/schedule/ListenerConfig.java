package com.sb.schedule;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by zhuzs on 2017/5/12.
 */
@Configuration
public class ListenerConfig {

    @Bean
    public QuartJobSchedulingListener applicationStartListener(){
        return new QuartJobSchedulingListener();
    }
}