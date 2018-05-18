package com.sb.service;

import com.sb.annotation.ExecutionInterval;
import com.sb.config.ElasticSearch;
import com.sb.model.User;
import com.sb.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
public class UserService {

    protected final Logger logger = LoggerFactory.getLogger(this.getClass());


    @Autowired
    private UserRepository userRepository;

    @Autowired
    ElasticSearch elasticSearch;

    @ExecutionInterval
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void addUser(String name, int age) throws Exception {
        var user = new User(name, age);
        try {
            userRepository.save(user);
        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * find by redis cache
     *
     * @param name
     * @return
     */
    public Optional<User> findUser(String name) {
        Optional<User> user = Optional.of(userRepository.findUser(name));

        return user;
    }

    /**
     * find by redis cache
     *
     * @param name
     * @return
     */
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void deleteUser(String name) {
        User user = userRepository.findUser(name);
        userRepository.delete(user);
    }


}
