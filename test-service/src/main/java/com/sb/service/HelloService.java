package com.sb.service;

import org.springframework.stereotype.Service;

@Service
public class HelloService {

    public String getUser(String name){
        return "Hello ---"+name;
    }
}
