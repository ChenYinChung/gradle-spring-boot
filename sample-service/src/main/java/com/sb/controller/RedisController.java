package com.sb.controller;

import com.sb.model.User;
import com.sb.service.RedisService;
import com.sb.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/redis")
public class RedisController {
    protected final Logger logger = LoggerFactory.getLogger(this.getClass());


    @Autowired
    UserService userService;

    @Autowired
    RedisService redisService;

    /**
     * http://localhost:8080/redis/set/sammy
     * @param name
     * @return
     */
    @RequestMapping(value = "/set/{name}", method = RequestMethod.GET)
    public Optional<User> setUser(@PathVariable("name") String name) {
            Optional<User> user =  userService.findUser(name);

            user.ifPresent(u->{
                redisService.setUser(u);
            });

        return user;
    }


    /**
     * http://localhost:8080/redis/get/sammy
     * @param name
     * @return
     */
    @RequestMapping(value = "/get/{name}", method = RequestMethod.GET)
    public Optional<User> getUser(@PathVariable("name") String name) {
        return redisService.getUser(name);
    }

}
