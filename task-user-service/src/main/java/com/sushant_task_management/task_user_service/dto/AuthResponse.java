package com.sushant_task_management.task_user_service.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AuthResponse {
    private String jwt;
    private String message;
    private boolean status;
    private String tokenType;
    private Long expiresIn;
    private UserInfo userInfo;
}
