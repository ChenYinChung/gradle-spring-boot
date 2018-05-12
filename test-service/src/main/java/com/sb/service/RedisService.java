package com.sb.service;

import com.sb.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class RedisService {

    protected final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    RedisTemplate<String, Object> redisTemplate;

    public void setUser(User user){
        redisTemplate.opsForValue().set(user.getName(),user);
    }

    public Optional<User> getUser(String name){
        return Optional.ofNullable((User)redisTemplate.opsForValue().get(name));
    }
}
