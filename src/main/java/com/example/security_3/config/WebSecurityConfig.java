package com.example.security_3.config;

import com.example.security_3.model.Role;
import com.example.security_3.model.User;
import com.example.security_3.model.UserRepository;
import com.example.security_3.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.*;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    private final UserService userService;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .authorizeRequests(auth -> auth
                        .antMatchers("/").permitAll()
                        .antMatchers("/admin").hasRole("ADMIN")
                        .anyRequest().authenticated())
                .logout().logoutUrl("/logout").deleteCookies("JSESSIONID");
        http.oauth2Login().userInfoEndpoint().userService(this.oAuth2UserService());
    }

    private OAuth2UserService<OAuth2UserRequest, OAuth2User> oAuth2UserService(){
        OAuth2UserService<OAuth2UserRequest,OAuth2User> oAuth2UserService= new DefaultOAuth2UserService();

        return oAuth2UserRequest -> {
            OAuth2User oAuth2User=oAuth2UserService.loadUser(oAuth2UserRequest);

            Map<String,Object> attributes=oAuth2User.getAttributes();
            String attributeNameKey=oAuth2UserRequest.getClientRegistration()
                    .getProviderDetails().getUserInfoEndpoint().getUserNameAttributeName();
            User user = userService.findUserByName(attributes.get("login").toString())
                    .orElseGet(()->{
                        User nevUser = User.builder()
                                .id(Long.parseLong(attributes.get("id").toString()))
                                .name(attributeNameKey)
                                .role(Role.USER)
                                .login(attributes.get("login").toString())
                                .build();
                        userService.createUser(nevUser);
                        return nevUser;
                    });

            Set<GrantedAuthority> authorities=new HashSet<>();

            authorities.add(new SimpleGrantedAuthority(user.getRole().toString()));

            return new DefaultOAuth2User(authorities,attributes,attributeNameKey);
        };

    }
}


