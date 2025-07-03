package com.sushant_task_management.task_user_service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponse {
    private boolean status;
    private String message;
    private String jwt;
    private String tokenType;
    private Long expiresIn;
    private UserInfo userInfo;
}