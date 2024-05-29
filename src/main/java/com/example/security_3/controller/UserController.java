package com.example.security_3.controller;

import com.example.security_3.model.dto.UserInfoDTO;
import com.example.security_3.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v2")
@RequiredArgsConstructor
public class UserController {
    private UserService userService;

    @GetMapping("/user")
    public ResponseEntity<String> userPage(){
        return ResponseEntity.ok("NOT admin");
    }

    @GetMapping("/admin")
    public ResponseEntity<String> adminPage(){
        return ResponseEntity.ok("AADDMMIINN");
    }

    @GetMapping
    public ResponseEntity<UserInfoDTO> userInfo(){
        return ResponseEntity.ok(userService.getUserInfo());
    }

    @GetMapping("/oauth/delete")
    public void logout(){
        userService.deleteUser();
    }

}
