package com.sb.controller;

import com.sb.annotation.ExecutionInterval;
import com.sb.model.User;
import com.sb.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@ControllerAdvice
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
//    @Cacheable(value = "user-cache", key = "#name")
    @RequestMapping(value = "/add/{name}/{age}", method = RequestMethod.GET)
    @ExecutionInterval
    public String addUser(@PathVariable("name") String name, @PathVariable("age") int age) throws Exception {
        logger.info("add {} , {}", name, age);
//        try {
            userService.addUser(name, age);
//        } catch (Exception e) {
//            logger.error("error", e);
//
//            return "Not OK";
//        }
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


    @ExceptionHandler(Exception.class)
    public String notFoundException(final Exception e) {
        return "Exception occures";
    }
}
