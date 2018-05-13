package com.sb.controller;

import com.sb.model.User;
import com.sb.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import java.util.Optional;

@RestController
@RequestMapping("/user")
public class UserController {
    protected final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    UserService userService;

    /**
     * http://localhost:8080/user/add/sammy/40
     * @param name
     * @param age
     * @return
     */
    @Cacheable(value = "user-cache", key = "#name")
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

    /**
     * http://localhost:8080/user/find/sammy
     * @param name
     * @return
     */
    @Cacheable(value = "user-cache", key = "#name")
    @RequestMapping(value = "/find/{name}", method = RequestMethod.GET)
    public User findUser(@PathVariable("name") String name) {
        logger.info("if show is line , not from cache [{}]",name);
        Optional<User> userOption=   userService.findUser(name);

        return userOption.get();
    }

}
