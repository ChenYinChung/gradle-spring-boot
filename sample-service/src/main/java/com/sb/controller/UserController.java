package com.sb.controller;

import com.sb.annotation.ExecutionInterval;
import com.sb.component.ElasticSearchComponent;
import com.sb.domain.ESDomain;
import com.sb.model.User;
import com.sb.service.UserService;
import io.searchbox.client.JestResult;
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

    @Autowired
    ElasticSearchComponent elasticSearchComponent;

    /**
     * http://localhost:8080/user/add/sammy/40
     *
     * 測試新增相同的資訊時，DB抛出 constraint 時，統一由 @ExceptionHandler 處理
     * 前題是，class 外框要加 @ControllerAdvice 控制全域攔截
     * @param name
     * @param age
     * @return
     */

    @RequestMapping(value = "/add/{name}/{age}", method = RequestMethod.GET)
    @ExecutionInterval
    public String add(@PathVariable("name") String name, @PathVariable("age") int age) throws Exception {
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
     * 加入Cacheable ,相同參數時，只有第一次會完成
     * 之後都由Cache取出
     *
     * @param name
     * @return
     */
    @Cacheable(value = "user-cache", key = "#name")
    @RequestMapping(value = "/find/{name}", method = RequestMethod.GET)
    public User find(@PathVariable("name") String name) {
        logger.info("if show is line , not from cache [{}]",name);
        Optional<User> userOption=   userService.findUser(name);

        return userOption.get();
    }

    /**
     * http://localhost:8080/user/login
     * {"name":"sammy","age":80}
     * @param user
     * @return
     */
    @RequestMapping(value = "/login", method = { RequestMethod.POST})
    public User login(@RequestBody User user) {

        Optional<User> userOption=   userService.findUser(user.getName());

        return userOption.get();
    }


    /**
     * http://localhost:8080/user/deltet
     * @param
     * @return
     */
    @RequestMapping(value = "/delete/{name}", method = { RequestMethod.DELETE})
    public void delete(@PathVariable("name") String name) {

        userService.deleteUser(name);
    }

    @ExceptionHandler(Exception.class)
    public String notFoundException(final Exception e) {

        sendES(e);
        return "Exception occures"+e;
    }

    /**
     *  Send to Elastic Search
     * @param e
     */
    private void sendES(Exception e){
        Optional<JestResult> jestResult = elasticSearchComponent.indicesExists(ESDomain.error.name());
        if(jestResult.isPresent() && !jestResult.get().isSucceeded()){
            elasticSearchComponent.createIndex(e,ESDomain.error.name(),ESDomain.error.name());
        }

        elasticSearchComponent.bulkIndex(ESDomain.error.name(),ESDomain.error.name(),e);

    }
}
