package hu.game.codenames.config;


import  hu.game.codenames.model.User;
import hu.game.codenames.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;

@Component
@Slf4j
public class UserDataSetup implements ApplicationListener<ContextRefreshedEvent> {

    private PasswordEncoder passwordEncoder;
    private UserRepository userRepository;


    @Autowired
    public UserDataSetup(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Autowired
    public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    boolean alreadySetup = false;

    @Override
    @Transactional
    public void onApplicationEvent(ContextRefreshedEvent event) {
        if(alreadySetup){
            return;
        }

        User user = new User();
        user.setEnabled(true);
        user.setPassword(passwordEncoder.encode("asdqwe"));
        user.setUsername("test");
        user.setEmail("test@test.com");
        userRepository.save(user);
        alreadySetup = true;
        log.info("test user and data saved");

    }
}
