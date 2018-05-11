package com.sb.controller;

import com.sb.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserController {
    protected final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    UserService userService;

    @RequestMapping(value = "/add/{name}/{age}", method = RequestMethod.GET)
    public String addUser(@PathVariable("name") String name, @PathVariable("age") int age) {
        logger.info("add {} , {}", name, age);
        try {
            userService.addUser(name, age);
        } catch (Exception e) {
            logger.error("error", e);

            return "Not OK";
        }
        return "OK";
    }
}
