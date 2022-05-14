package hu.game.codenames.service;

import hu.game.codenames.model.User;
import hu.game.codenames.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service("userDetailsService")
@Transactional
@Slf4j
public class MyUserDetailsService implements UserDetailsService{


    private UserRepository userRepository;


    @Autowired
    public MyUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);
            if(user == null){
                throw new UsernameNotFoundException("Could not find user with username (or email): " + username);
        }
        log.info(user.getUsername() + " found");
        return user;
    }

    public boolean usernameCheck(String username){
        User user = userRepository.findByUsername(username);
        if(user == null) {
            return false;
        }
        return true;
    }

    public void registerUser(User user) {
        userRepository.save(user);
        log.info("user registered: " + user);
    }




}
