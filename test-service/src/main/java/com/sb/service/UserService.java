package com.sb.service;

import com.sb.model.User;
import com.sb.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class UserService {

    @Autowired
    private UserRepository userRepository;


    @Transactional(propagation = Propagation.REQUIRED,rollbackFor = Exception.class)
    public void addUser(String name, int age) throws Exception{

        if(age > 90){
            throw new Exception("Too old");
        }
        try {
            userRepository.save(new User(name+age, age));
        }catch(Exception e){
            throw e;
        }
    }
}
