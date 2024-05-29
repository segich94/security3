package com.example.security_3.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "_user")
public class User implements OAuth2User {

    @Id
    @GeneratedValue()
    private Long id;

    private String name;

    private String login;

    private Role role;


    @Override
    public Map<String, Object> getAttributes() {
        Map<String, Object> map = new HashMap<>();
        map.put("id",this.id);
        map.put("name", this.name);
        map.put("login", this.login);
        map.put("role", this.role);
        return map;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singleton(new SimpleGrantedAuthority(this.role.toString()));
    }
}
