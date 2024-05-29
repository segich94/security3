package com.example.security_3.model.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserInfoDTO {

        private String name;
        private String login;
}
