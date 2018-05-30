package com.sb.service;

import com.sb.annotation.ExecutionInterval;
import com.sb.component.ElasticSearchComponent;
import com.sb.domain.ESDomain;
import com.sb.domain.UserType;
import com.sb.model.User;
import com.sb.repository.UserRepository;
import io.searchbox.client.JestResult;
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
    ElasticSearchComponent elasticSearchComponent;

    @ExecutionInterval
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void addUser(String name, int age) throws Exception {
        var user = new User(name, age);
        try {
            userRepository.save(user);

        } catch (Exception e) {
            throw e;
        }

        sendES(user,UserType.add);
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
        sendES(user,UserType.delete);
    }

    private void sendES(User user,UserType userType){
        Optional<JestResult> jestResult = elasticSearchComponent.indicesExists(ESDomain.user.name());
        if(jestResult.isPresent() && !jestResult.get().isSucceeded()){
            elasticSearchComponent.createIndex(user,ESDomain.user.name(),userType.name());
        }

        elasticSearchComponent.bulkIndex(ESDomain.user.name(),userType.name(),user);

    }

}
