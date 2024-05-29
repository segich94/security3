package com.example.security_3.service;

import com.example.security_3.model.User;
import com.example.security_3.model.UserRepository;
import com.example.security_3.model.dto.UserInfoDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {
    private UserRepository userRepository;



    public void createUser(User user){
        userRepository.save(user);
        log.info(user.getName() + " created");
    }

    public Optional<User> findUserByName(String name){
        Optional<User> result =  userRepository.findUserByName(name);
        if (result.isPresent()){
            log.info("User - " + name + " was get to authorize");
        }
        return result;
    }

    public UserInfoDTO getUserInfo(){
        User user = currentUser();

        return UserInfoDTO.builder()
                .name(user.getName())
                .login(user.getLogin())
                .build();


    }

    private User currentUser(){
        OidcUser oidcUser = (OidcUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        Map<String, Object> attributes = oidcUser.getAttributes();
        String name = (String) attributes.get("name");

        return findUserByName(name)
                .orElseThrow(()->new UsernameNotFoundException("User not exsist" + name));
    }

    public void deleteUser(){
        User user = currentUser();
        log.info(user +" revoke");
        userRepository.deleteById(user.getId());
    }
}
