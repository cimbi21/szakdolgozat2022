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
    public UserDetails loadUserByUsername(String usernameOrEmail) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(usernameOrEmail);

        if(user == null){
            user = userRepository.findByEmail(usernameOrEmail);

            if(user == null){
                throw new UsernameNotFoundException("Could not find user with username (or email): " + usernameOrEmail);
            }
        }

        log.info(user.getUsername() + " found");
        return user;
    }

    public User myData(String name){
        List<User> originalList = userRepository.findAll();
        System.out.println(originalList);
        List<User> filteredList = originalList.stream()
                .filter(t -> t.getUsername().equals(name))
                .collect(Collectors.toList());
        return filteredList.get(0);
    }

    public void registerUser(User user) {
        user.setEnabled(true);
        userRepository.save(user);
        log.info("user registered: " + user);
    }




}
