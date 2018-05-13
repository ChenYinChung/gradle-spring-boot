package com.sb.controller;

import com.sb.service.HelloService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

//此類別為Spring Controller元件
@RestController
public class HelloController {
    private Logger logger = LoggerFactory.getLogger(HelloController.class);

    @Autowired
    HelloService helloService;

    //透過 @RequestMapping 指定從/hello會被對應到此hello()方法
    @RequestMapping("/hello")
    //透過 @ResponseBody 告知Spring，此函數的回傳值是HTTP Response的本文
    public @ResponseBody String hello(){
        logger.info("test {}","Just test");
        return "Hello World!";
    }

    //加入{}對URL進行參數化
    @RequestMapping(value="/hello/{name}",method = RequestMethod.GET)
//加入 @PathVariable 讓Spring自動將對應的URL參數轉換為此方法的參數
    public String hello(@PathVariable("name") String name){
        logger.info("login {}", name);
        return helloService.getUser(name);
    }

    @RequestMapping(value="/hello/list",method= RequestMethod.POST)
    public List<String> list(){
        List<String> list = new ArrayList<>();

        list.add("test 1");
        list.add("test 2");
        list.add("test 3");
        list.add("test 4");
        list.add("test 5");


        return list;
    }

}